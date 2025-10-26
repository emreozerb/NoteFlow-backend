package com.noteflow.noteflow_backend.mapper;

import com.noteflow.noteflow_backend.dto.*;
import com.noteflow.noteflow_backend.dto.request.note.CreateNoteRequestDTO;
import com.noteflow.noteflow_backend.dto.response.NoteDTO;
import com.noteflow.noteflow_backend.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NoteMapper {

    public NoteDTO toDTO(Note note) {
        if (note == null)
            return null;

        return new NoteDTO(
                note.getNoteId(),
                note.getTitle(),
                note.getContent(),
                note.getFolder().getId(),
                note.getFolder().getName(),
                note.getFolder().getCategory().getId(),
                note.getFolder().getCategory().getName(),
                note.getCreatedAt(),
                note.getUpdatedAt());
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
}
