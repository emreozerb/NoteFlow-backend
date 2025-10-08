package com.noteflow.noteflow_backend.security;

import com.noteflow.noteflow_backend.config.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtUtil {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final JwtProperties jwtProperties;

    @Autowired
    public JwtUtil(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, JwtProperties jwtProperties) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(String email, Long userId) {
        Instant now = Instant.now();
        Instant expiry = now.plus(jwtProperties.token().lifetime());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.token().issuer())
                .issuedAt(now)
                .expiresAt(expiry)
                .subject(email)
                .claim("userId", userId)
                .build();

        // ✅ IMPORTANT: Specify the algorithm in the header
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public String extractEmail(String token) {
        return jwtDecoder.decode(token).getSubject();
    }

    public Long extractUserId(String token) {
        return jwtDecoder.decode(token).getClaim("userId");
    }

    public boolean validateToken(String token, String email) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getSubject().equals(email) &&
                    jwt.getExpiresAt() != null &&
                    jwt.getExpiresAt().isAfter(Instant.now());
        } catch (JwtException e) {
            return false;
        }
    }
}