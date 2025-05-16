package com.pietro.backendgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pietro.backendgs.exception.ItemNameAlreadyExistsException;
import com.pietro.backendgs.model.dto.PasswordItemRequest;
import com.pietro.backendgs.model.dto.PasswordItemResponse;
import com.pietro.backendgs.service.PasswordItemService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/items")
public class PasswordItemController {

    @Autowired
    private PasswordItemService passwordItemService;

    @PostMapping
    public ResponseEntity<?> createPasswordItem(@Valid @RequestBody PasswordItemRequest passwordItemRequest) {
        try {
            PasswordItemResponse response = passwordItemService.createPasswordItem(passwordItemRequest);
            return ResponseEntity.ok(response);
        } catch (ItemNameAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PasswordItemResponse>> getAllPasswordItems() {
        List<PasswordItemResponse> items = passwordItemService.getAllPasswordItems();
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePasswordItem(@PathVariable Long id) {
        try {
            passwordItemService.deletePasswordItem(id);
            return ResponseEntity.ok().body("Item excluído com sucesso");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllPasswordItems() {
        try {
            passwordItemService.deleteAllPasswordItems();
            return ResponseEntity.ok().body("Todos os itens foram excluídos com sucesso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao excluir os itens: " + e.getMessage());
        }
    }
} 