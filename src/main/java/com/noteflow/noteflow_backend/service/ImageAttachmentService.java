package com.noteflow.noteflow_backend.service;

import com.noteflow.noteflow_backend.model.ImageAttachment;
import com.noteflow.noteflow_backend.model.Note;
import com.noteflow.noteflow_backend.model.User;
import com.noteflow.noteflow_backend.repository.ImageAttachmentRepository;
import com.noteflow.noteflow_backend.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ImageAttachmentService {

    @Autowired
    private ImageAttachmentRepository imageAttachmentRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Value("${file.upload.images-dir}")
    private String imagesDir;

    @Value("${file.upload.allowed-image-types}")
    private String allowedImageTypes;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public ImageAttachment uploadImage(Long noteId, MultipartFile file, Integer displayOrder, User user) throws IOException {
        // 1. Validate note exists and user has access
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getFolder().getCategory().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to note");
        }

        // 2. Validate file
        validateFile(file);

        // 3. Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String storedFileName = UUID.randomUUID().toString() + "_" + timestamp + fileExtension;

        // 4. Create directory structure: /uploads/images/{userId}/{noteId}/
        Path uploadPath = Paths.get(imagesDir, user.getId().toString(), noteId.toString());
        Files.createDirectories(uploadPath);

        // 5. Save file
        Path filePath = uploadPath.resolve(storedFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 6. Create database record
        String relativePath = Paths.get(user.getId().toString(), noteId.toString(), storedFileName).toString();
        ImageAttachment imageAttachment = new ImageAttachment();
        imageAttachment.setNote(note);
        imageAttachment.setFileName(originalFilename);
        imageAttachment.setStoredFileName(storedFileName);
        imageAttachment.setFilePath(relativePath);
        imageAttachment.setMimeType(file.getContentType());
        imageAttachment.setFileSize(file.getSize());
        imageAttachment.setDisplayOrder(displayOrder != null ? displayOrder : 0);

        return imageAttachmentRepository.save(imageAttachment);
    }

    public List<ImageAttachment> getImagesByNote(Long noteId, User user) {
        // Validate note exists and user has access
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getFolder().getCategory().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to note");
        }

        return imageAttachmentRepository.findByNote_NoteIdOrderByDisplayOrderAsc(noteId);
    }

    public Resource loadImageAsResource(Long noteId, Long imageId, User user) throws IOException {
        // Validate note exists and user has access
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getFolder().getCategory().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to note");
        }

        // Get image attachment
        ImageAttachment imageAttachment = imageAttachmentRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        if (!imageAttachment.getNote().getNoteId().equals(noteId)) {
            throw new RuntimeException("Image does not belong to this note");
        }

        // Load file as resource
        Path filePath = Paths.get(imagesDir).resolve(imageAttachment.getFilePath());
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("File not found or not readable: " + imageAttachment.getFileName());
        }
    }

    public void deleteImage(Long noteId, Long imageId, User user) throws IOException {
        // Validate note exists and user has access
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getFolder().getCategory().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to note");
        }

        // Get image attachment
        ImageAttachment imageAttachment = imageAttachmentRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        if (!imageAttachment.getNote().getNoteId().equals(noteId)) {
            throw new RuntimeException("Image does not belong to this note");
        }

        // Delete file from filesystem
        Path filePath = Paths.get(imagesDir).resolve(imageAttachment.getFilePath());
        Files.deleteIfExists(filePath);

        // Delete database record
        imageAttachmentRepository.delete(imageAttachment);
    }

    private void validateFile(MultipartFile file) {
        // Check if file is empty
        if (file.isEmpty()) {
            throw new RuntimeException("Cannot upload empty file");
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("File size exceeds maximum limit of 10MB");
        }

        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !isAllowedImageType(contentType)) {
            throw new RuntimeException("Invalid file type. Allowed types: " + allowedImageTypes);
        }
    }

    private boolean isAllowedImageType(String contentType) {
        List<String> allowedTypes = Arrays.asList(allowedImageTypes.split(","));
        return allowedTypes.stream().anyMatch(type -> type.trim().equalsIgnoreCase(contentType));
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

}
