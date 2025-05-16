package com.pietro.backendgs.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pietro.backendgs.exception.ItemNameAlreadyExistsException;
import com.pietro.backendgs.model.dto.PasswordItemRequest;
import com.pietro.backendgs.model.dto.PasswordItemResponse;
import com.pietro.backendgs.model.entity.PasswordItem;
import com.pietro.backendgs.model.entity.User;
import com.pietro.backendgs.model.repository.PasswordItemRepository;
import com.pietro.backendgs.model.repository.UserRepository;
import com.pietro.backendgs.security.UserDetailsImpl;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PasswordItemService {

    @Autowired
    private PasswordItemRepository passwordItemRepository;

    @Autowired
    private UserRepository userRepository;

    public PasswordItemResponse createPasswordItem(PasswordItemRequest passwordItemRequest) {
        User currentUser = getCurrentUser();

        // Verifique se já existe um item com esse nome para o usuário atual
        if (passwordItemRepository.existsByNameAndUser(passwordItemRequest.getName(), currentUser)) {
            throw new ItemNameAlreadyExistsException("Já existe um item com esse nome");
        }

        PasswordItem passwordItem = new PasswordItem();
        passwordItem.setName(passwordItemRequest.getName());
        passwordItem.setPassword(passwordItemRequest.getPassword());
        passwordItem.setUser(currentUser);

        PasswordItem savedItem = passwordItemRepository.save(passwordItem);

        return new PasswordItemResponse(
                savedItem.getId(),
                savedItem.getName(),
                savedItem.getPassword());
    }

    public List<PasswordItemResponse> getAllPasswordItems() {
        User currentUser = getCurrentUser();

        List<PasswordItem> items = passwordItemRepository.findByUser(currentUser);

        return items.stream()
                .map(item -> new PasswordItemResponse(
                        item.getId(),
                        item.getName(),
                        item.getPassword()))
                .collect(Collectors.toList());
    }

    public void deletePasswordItem(Long id) {
        User currentUser = getCurrentUser();

        PasswordItem passwordItem = passwordItemRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado ou não pertence ao usuário atual"));

        passwordItemRepository.delete(passwordItem);
    }
    
    @Transactional
    public void deleteAllPasswordItems() {
        User currentUser = getCurrentUser();
        
        List<PasswordItem> items = passwordItemRepository.findByUser(currentUser);
        passwordItemRepository.deleteAll(items);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }
} 