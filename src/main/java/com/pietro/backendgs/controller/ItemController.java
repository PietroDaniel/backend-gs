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
import org.springframework.web.bind.annotation.RestController;

import com.pietro.backendgs.exception.GsException;
import com.pietro.backendgs.model.dto.SenhaRequest;
import com.pietro.backendgs.model.dto.SenhaResponse;
import com.pietro.backendgs.service.SenhaService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ItemController {

    @Autowired
    private SenhaService senhaService;

    @PostMapping("/item")
    public ResponseEntity<?> criarItem(@Valid @RequestBody SenhaRequest senhaRequest) {
        try {
            SenhaResponse senhaResponse = senhaService.criarSenha(senhaRequest);
            return ResponseEntity.ok(senhaResponse);
        } catch (GsException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @GetMapping("/items")
    public ResponseEntity<?> listarItems() {
        try {
            List<SenhaResponse> senhas = senhaService.listarSenhasDoUsuario();
            return ResponseEntity.ok(senhas);
        } catch (GsException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<?> excluirItem(@PathVariable Long id) {
        try {
            senhaService.excluirSenha(id);
            return ResponseEntity.ok().body("Item excluído com sucesso");
        } catch (GsException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
    
    @DeleteMapping("/items")
    public ResponseEntity<?> excluirTodasSenhas() {
        try {
            senhaService.excluirTodasSenhasDoUsuario();
            return ResponseEntity.ok().body("Todas as senhas foram excluídas com sucesso");
        } catch (GsException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
    
    @GetMapping("/item/{id}")
    public ResponseEntity<?> buscarItemPorId(@PathVariable Long id) {
        try {
            SenhaResponse senha = senhaService.procurarPorId(id);
            return ResponseEntity.ok(senha);
        } catch (GsException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
} 