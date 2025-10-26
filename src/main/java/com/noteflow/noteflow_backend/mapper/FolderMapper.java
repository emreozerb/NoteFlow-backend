package com.noteflow.noteflow_backend.mapper;

import com.noteflow.noteflow_backend.dto.*;
import com.noteflow.noteflow_backend.dto.request.folder.CreateFolderRequestDTO;
import com.noteflow.noteflow_backend.dto.response.FolderDTO;
import com.noteflow.noteflow_backend.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FolderMapper {

    public FolderDTO toDTO(Folder folder) {
        if (folder == null)
            return null;

        return new FolderDTO(
                folder.getId(),
                folder.getName(),
                folder.getDescription(),
                folder.getCategory().getId(),
                folder.getCategory().getName(),
                folder.getCreatedAt(),
                folder.getUpdatedAt());
    }

    public List<FolderDTO> toDTOList(List<Folder> folders) {
        return folders.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Folder toEntity(CreateFolderRequestDTO dto, Category category) {
        if (dto == null)
            return null;

        Folder folder = new Folder();
        folder.setName(dto.getName());
        folder.setDescription(dto.getDescription());
        folder.setCategory(category);
        return folder;
    }
}
