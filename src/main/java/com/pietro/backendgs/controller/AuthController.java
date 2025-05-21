package com.pietro.backendgs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pietro.backendgs.auth.AuthService;
import com.pietro.backendgs.exception.EmailAlreadyExistsException;
import com.pietro.backendgs.exception.PasswordMismatchException;
import com.pietro.backendgs.model.dto.JwtResponse;
import com.pietro.backendgs.model.dto.SigninRequest;
import com.pietro.backendgs.model.dto.SignupRequest;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninRequest signinRequest) {
        log.info("Received signin request for email: {}", signinRequest.getEmail());
        JwtResponse jwtResponse = authService.authenticateUser(signinRequest);
        return ResponseEntity.ok(jwtResponse);
    }
    
    @PostMapping("/api/signin")
    public ResponseEntity<?> authenticateUserApi(@Valid @RequestBody SigninRequest signinRequest) {
        log.info("Received API signin request for email: {}", signinRequest.getEmail());
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
    
    @PostMapping("/api/signup")
    public ResponseEntity<?> registerUserApi(@RequestBody SignupRequest signupRequest) {
        try {
            log.info("Received API signup request: {}", signupRequest);
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
} 