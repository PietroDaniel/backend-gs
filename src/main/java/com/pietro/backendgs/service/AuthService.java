package com.pietro.backendgs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pietro.backendgs.exception.EmailAlreadyExistsException;
import com.pietro.backendgs.exception.PasswordMismatchException;
import com.pietro.backendgs.model.dto.JwtResponse;
import com.pietro.backendgs.model.dto.SigninRequest;
import com.pietro.backendgs.model.dto.SignupRequest;
import com.pietro.backendgs.model.entity.User;
import com.pietro.backendgs.model.repository.UserRepository;
import com.pietro.backendgs.security.JwtUtils;
import com.pietro.backendgs.security.UserDetailsImpl;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public JwtResponse authenticateUser(SigninRequest signinRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        return new JwtResponse(
                jwt, 
                userDetails.getId(), 
                userDetails.getName(), 
                userDetails.getEmail());
    }

    public void registerUser(SignupRequest signupRequest) {
        // Verifique se o email já existe
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email já está em uso!");
        }

        // Verifique se as senhas coincidem
        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            throw new PasswordMismatchException("As senhas não coincidem!");
        }

        // Crie um novo usuário
        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setBirthDate(signupRequest.getBirthDate());

        userRepository.save(user);
    }
} 