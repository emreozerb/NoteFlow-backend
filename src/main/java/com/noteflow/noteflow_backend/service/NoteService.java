package com.noteflow.noteflow_backend.service;

import com.noteflow.noteflow_backend.model.Folder;
import com.noteflow.noteflow_backend.model.Note;
import com.noteflow.noteflow_backend.model.User;
import com.noteflow.noteflow_backend.repository.FolderRepository;
import com.noteflow.noteflow_backend.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private FolderRepository folderRepository;

    public List<Note> getNotesByUser(User user) {
        return noteRepository.findByFolderCategoryUser(user);
    }

    public List<Note> getNotesByFolder(Long folderId, User user) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        if (!folder.getCategory().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to folder");
        }

        return noteRepository.findByFolder(folder);
    }

    public Note getNoteById(Long noteId, User user) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getFolder().getCategory().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to note");
        }

        return note;
    }

    public Note createNote(String title, String content, Long folderId, User user) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        if (!folder.getCategory().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to folder");
        }

        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setFolder(folder);

        return noteRepository.save(note);
    }

    public Note updateNote(Long noteId, String title, String content, User user) {
        Note note = getNoteById(noteId, user);
        note.setTitle(title);
        note.setContent(content);
        return noteRepository.save(note);
    }

    public void deleteNote(Long noteId, User user) {
        Note note = getNoteById(noteId, user);
        noteRepository.delete(note);
    }

    // Additional useful methods
    public Note moveNote(Long noteId, Long newFolderId, User user) {
        Note note = getNoteById(noteId, user);

        Folder newFolder = folderRepository.findById(newFolderId)
                .orElseThrow(() -> new RuntimeException("Target folder not found"));

        if (!newFolder.getCategory().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to target folder");
        }

        note.setFolder(newFolder);
        return noteRepository.save(note);
    }

    public List<Note> searchNotes(User user, String searchTerm) {
        // You'll need to add this method to NoteRepository
        // For now, get all notes and filter in memory (not optimal for production)
        List<Note> allNotes = getNotesByUser(user);
        String lowerSearch = searchTerm.toLowerCase();

        return allNotes.stream()
                .filter(note -> note.getTitle().toLowerCase().contains(lowerSearch) ||
                        (note.getContent() != null && note.getContent().toLowerCase().contains(lowerSearch)))
                .toList();
    }

    public long countNotesByUser(User user) {
        return getNotesByUser(user).size();
    }
}