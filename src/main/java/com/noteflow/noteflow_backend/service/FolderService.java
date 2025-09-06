package com.noteflow.noteflow_backend.service;

import com.noteflow.noteflow_backend.model.Folder;
import com.noteflow.noteflow_backend.model.Category;
import com.noteflow.noteflow_backend.repository.FolderRepository;
import com.noteflow.noteflow_backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Create new folder
    public Folder createFolder(Long categoryId, Long userId, Folder folder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Check user ownership of category
        if (!category.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: Category belongs to different user");
        }

        // Check if folder name already exists in this category
        if (folderRepository.existsByNameAndCategory(folder.getName(), category)) {
            folder.setName(folder.getName() + " (2)");

        }

        folder.setCategory(category);
        return folderRepository.save(folder);
    }

    // Get folder by ID (with user validation)
    public Optional<Folder> getFolderById(Long id, Long userId) {
        Optional<Folder> folder = folderRepository.findById(id);

        if (folder.isPresent() && !folder.get().getCategory().getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: Folder belongs to different user");
        }

        return folder;
    }

    // Get folder with notes
    public Optional<Folder> getFolder(Long id, Long userId) {
        Optional<Folder> folder = folderRepository.findByIdWithNotes(id);

        if (folder.isPresent() && !folder.get().getCategory().getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: Folder belongs to different user");
        }

        return folder;
    }

    // Get all folders for user
    public List<Folder> getFoldersByUser(Long userId) {
        return folderRepository.findByUserId(userId);
    }

    // Delete folder
    public void deleteFolder(Long id, Long userId) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        // Check user ownership
        if (!folder.getCategory().getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: Folder belongs to different user");
        }

        folderRepository.deleteById(id);
    }
}
