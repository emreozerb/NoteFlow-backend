package com.noteflow.noteflow_backend.mapper;

import com.noteflow.noteflow_backend.dto.request.note.CreateNoteRequestDTO;
import com.noteflow.noteflow_backend.dto.response.NoteDTO;
import com.noteflow.noteflow_backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NoteMapper {

    @Autowired
    private ImageAttachmentMapper imageAttachmentMapper;

    public NoteDTO toDTO(Note note) {
        if (note == null)
            return null;

        String baseUrl = getBaseUrl();

        return new NoteDTO(
                note.getNoteId(),
                note.getTitle(),
                note.getContent(),
                note.getFolder().getId(),
                note.getFolder().getName(),
                note.getFolder().getCategory().getId(),
                note.getFolder().getCategory().getName(),
                note.getCreatedAt(),
                note.getUpdatedAt(),
                imageAttachmentMapper.toDTOList(note.getImages(), baseUrl));
    }

    public List<NoteDTO> toDTOList(List<Note> notes) {
        return notes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Note toEntity(CreateNoteRequestDTO dto, Folder folder) {
        if (dto == null)
            return null;

        Note note = new Note();
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        note.setFolder(folder);
        return note;
    }

    private String getBaseUrl() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        }
        return "http://localhost:8080"; // Fallback
    }
}
