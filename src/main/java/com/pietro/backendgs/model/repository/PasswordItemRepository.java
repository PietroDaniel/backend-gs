package com.pietro.backendgs.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pietro.backendgs.model.entity.PasswordItem;
import com.pietro.backendgs.model.entity.User;

@Repository
public interface PasswordItemRepository extends JpaRepository<PasswordItem, Long> {
    List<PasswordItem> findByUser(User user);
    Optional<PasswordItem> findByIdAndUser(Long id, User user);
    Boolean existsByNameAndUser(String name, User user);
} 