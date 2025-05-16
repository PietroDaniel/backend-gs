package com.pietro.backendgs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pietro.backendgs.exception.EmailAlreadyExistsException;
import com.pietro.backendgs.exception.PasswordMismatchException;
import com.pietro.backendgs.model.dto.JwtResponse;
import com.pietro.backendgs.model.dto.SigninRequest;
import com.pietro.backendgs.model.dto.SignupRequest;
import com.pietro.backendgs.service.AuthService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninRequest signinRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(signinRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            authService.registerUser(signupRequest);
            return ResponseEntity.ok().body("Usuário registrado com sucesso!");
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (PasswordMismatchException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 