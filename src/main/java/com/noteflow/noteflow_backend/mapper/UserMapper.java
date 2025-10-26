package com.noteflow.noteflow_backend.mapper;

import com.noteflow.noteflow_backend.dto.response.UserDTO;
import com.noteflow.noteflow_backend.dto.request.auth.RegisterRequestDTO;
import com.noteflow.noteflow_backend.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null)
            return null;

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName());
    }

    public List<UserDTO> toDTOList(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public User toEntity(RegisterRequestDTO dto) {
        if (dto == null)
            return null;

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setPassword(dto.getPassword());
        return user;
    }
}