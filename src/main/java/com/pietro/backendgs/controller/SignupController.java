package com.pietro.backendgs.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pietro.backendgs.model.entity.Usuario;
import com.pietro.backendgs.model.repository.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin("*")
@Slf4j
public class SignupController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> requestData) {
        try {
            log.info("Received registration data: {}", requestData);
            
            String name = (String) requestData.get("name");
            String email = (String) requestData.get("email");
            String password = (String) requestData.get("password");
            String confirmPassword = (String) requestData.get("confirmPassword");
            String birthDateStr = (String) requestData.get("birthDate");
            
            log.info("Parsed request values - name: {}, email: {}, birthDate: {}", name, email, birthDateStr);
            
            // Validações básicas
            if (name == null || email == null || password == null || confirmPassword == null || birthDateStr == null) {
                return ResponseEntity.badRequest().body("Todos os campos são obrigatórios");
            }
            
            if (!password.equals(confirmPassword)) {
                return ResponseEntity.badRequest().body("As senhas não coincidem");
            }
            
            if (usuarioRepository.existsByEmail(email)) {
                return ResponseEntity.badRequest().body("Email já está em uso");
            }
            
            // Converter a data
            LocalDate birthDate;
            try {
                if (birthDateStr.contains("/")) {
                    birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } else {
                    birthDate = LocalDate.parse(birthDateStr);
                }
            } catch (DateTimeParseException e) {
                log.error("Erro ao converter data: {}", e.getMessage());
                return ResponseEntity.badRequest().body("Formato de data inválido. Use DD/MM/AAAA ou AAAA-MM-DD");
            }
            
            // Criar e salvar usuário
            Usuario usuario = new Usuario();
            usuario.setNome(name);
            usuario.setEmail(email);
            usuario.setSenha(passwordEncoder.encode(password));
            usuario.setDataNascimento(birthDate);
            
            usuarioRepository.save(usuario);
            
            return ResponseEntity.ok().body("Usuário registrado com sucesso!");
        } catch (Exception e) {
            log.error("Erro ao registrar usuário: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Erro ao registrar usuário: " + e.getMessage());
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUserAlternative(@RequestBody Map<String, Object> requestData) {
        log.info("Received registration data at /register endpoint: {}", requestData);
        // Redirecionar para o método principal
        return registerUser(requestData);
    }
} 