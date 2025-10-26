package com.noteflow.noteflow_backend.dto.request.folder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFolderRequestDTO {
    @NotBlank(message = "Folder name is required")
    @Size(max = 100, message = "Folder name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Long categoryId;
}
