package com.noteflow.noteflow_backend.repository;

import com.noteflow.noteflow_backend.model.Category;
import com.noteflow.noteflow_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);

    List<Category> findByUserOrderByCreatedAtDesc(User user);

    boolean existsByNameAndUser(String name, User user);
}