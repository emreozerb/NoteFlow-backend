package com.noteflow.noteflow_backend.controller;

import com.noteflow.noteflow_backend.model.User;
import com.noteflow.noteflow_backend.security.JwtUtil;
import com.noteflow.noteflow_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Register new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Login user - NOW RETURNS JWT TOKEN
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<User> userOptional = userService.getUserByEmail(loginRequest.getEmail());

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Verify password
                if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                    // Generate JWT token
                    String token = jwtUtil.generateToken(user.getEmail(), user.getId());

                    // Return token and user info
                    LoginResponse response = new LoginResponse(token, user);
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get user by ID - NOW REQUIRES AUTHENTICATION
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id, Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();

            // Users can only get their own profile
            if (!currentUser.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get all users - ADMIN ONLY (for now, returns current user's info)
    @GetMapping
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            return ResponseEntity.ok(currentUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Update user profile - NOW REQUIRES AUTHENTICATION
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();

            // Users can only update their own profile
            if (!currentUser.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            User updatedUser = new User();
            updatedUser.setUsername(request.getUsername());
            updatedUser.setEmail(request.getEmail());
            updatedUser.setFullName(request.getFullName());

            User user = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Update password - NOW REQUIRES AUTHENTICATION
    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id,
            @RequestBody PasswordUpdateRequest request,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();

            // Users can only update their own password
            if (!currentUser.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            userService.updatePassword(id, request.getNewPassword());
            return ResponseEntity.ok("Password updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Delete user - NOW REQUIRES AUTHENTICATION
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();

            // Users can only delete their own account
            if (!currentUser.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Helper classes
    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class LoginResponse {
        private String token;
        private User user;

        public LoginResponse(String token, User user) {
            this.token = token;
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    public static class PasswordUpdateRequest {
        private String newPassword;

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

    public static class UserUpdateRequest {
        private String username;
        private String email;
        private String fullName;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
    }
}