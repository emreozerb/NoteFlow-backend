package com.noteflow.noteflow_backend.service;

import com.noteflow.noteflow_backend.model.Category;
import com.noteflow.noteflow_backend.model.Folder;
import com.noteflow.noteflow_backend.model.User;
import com.noteflow.noteflow_backend.repository.CategoryRepository;
import com.noteflow.noteflow_backend.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Folder> getFoldersByUser(User user) {
        return folderRepository.findByCategoryUser(user);
    }

    public List<Folder> getFoldersByCategory(Long categoryId, User user) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to category");
        }

        return folderRepository.findByCategory(category);
    }

    public Folder getFolderById(Long id, User user) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        if (!folder.getCategory().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to folder");
        }

        return folder;
    }

    public Folder createFolder(String name, String description, Long categoryId, User user) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to category");
        }

        // Check if folder name already exists in this category
        List<Folder> existingFolders = folderRepository.findByCategory(category);

        // Use the original name in the lambda
        boolean nameExists = existingFolders.stream()
                .anyMatch(f -> f.getName().equalsIgnoreCase(name));

        // Create the final name AFTER the lambda
        String folderName = name;
        if (nameExists) {
            folderName = name + " (2)";
        }

        Folder folder = new Folder();
        folder.setName(folderName);
        folder.setDescription(description);
        folder.setCategory(category);

        return folderRepository.save(folder);
    }

    public Folder updateFolder(Long id, String name, String description, User user) {
        Folder folder = getFolderById(id, user);

        // Check if new name already exists in the category (excluding current folder)
        List<Folder> existingFolders = folderRepository.findByCategory(folder.getCategory());
        boolean nameExists = existingFolders.stream()
                .anyMatch(f -> !f.getId().equals(id) && f.getName().equalsIgnoreCase(name));

        if (nameExists) {
            throw new RuntimeException("Folder name already exists in this category");
        }

        folder.setName(name);
        folder.setDescription(description);
        return folderRepository.save(folder);
    }

    public void deleteFolder(Long id, User user) {
        Folder folder = getFolderById(id, user);
        folderRepository.delete(folder);
    }
}