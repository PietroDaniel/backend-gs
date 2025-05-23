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

import com.pietro.backendgs.auth.AuthService;
import com.pietro.backendgs.exception.EmailAlreadyExistsException;
import com.pietro.backendgs.exception.PasswordMismatchException;
import com.pietro.backendgs.model.dto.JwtResponse;
import com.pietro.backendgs.model.dto.SigninRequest;
import com.pietro.backendgs.model.dto.SignupRequest;
import com.pietro.backendgs.model.entity.Usuario;
import com.pietro.backendgs.model.repository.UsuarioRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninRequest signinRequest) {
        log.info("Received signin request for email: {}", signinRequest.getEmail());
        JwtResponse jwtResponse = authService.authenticateUser(signinRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        try {
            log.info("Received signup request: {}", signupRequest);
            authService.registerUser(signupRequest);
            return ResponseEntity.ok().body("Usuário registrado com sucesso!");
        } catch (EmailAlreadyExistsException e) {
            log.error("Email already exists: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (PasswordMismatchException e) {
            log.error("Password mismatch: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error registering user: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Erro ao registrar usuário: " + e.getMessage());
        }
    }
    
    @PostMapping("/signup/alt")
    public ResponseEntity<?> registerUserViaMap(@RequestBody Map<String, Object> requestData) {
        try {
            log.info("Received registration data: {}", requestData);
            
            String name = (String) requestData.get("name");
            String email = (String) requestData.get("email");
            String password = (String) requestData.get("password");
            String confirmPassword = (String) requestData.get("confirmPassword");
            String birthDateStr = (String) requestData.get("birthDate");
            
            log.info("Parsed request values - name: {}, email: {}, birthDate: {}", name, email, birthDateStr);
            
            if (name == null || email == null || password == null || confirmPassword == null || birthDateStr == null) {
                return ResponseEntity.badRequest().body("Todos os campos são obrigatórios");
            }
            
            if (!password.equals(confirmPassword)) {
                return ResponseEntity.badRequest().body("As senhas não coincidem");
            }
            
            if (usuarioRepository.existsByEmail(email)) {
                return ResponseEntity.badRequest().body("Email já está em uso");
            }
            
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
} 