package com.noteflow.noteflow_backend.dto.response.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.noteflow.noteflow_backend.dto.response.UserDTO;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private UserDTO user;
}