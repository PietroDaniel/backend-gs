package com.pietro.backendgs.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pietro.backendgs.exception.EmailAlreadyExistsException;
import com.pietro.backendgs.exception.GsException;
import com.pietro.backendgs.exception.PasswordMismatchException;
import com.pietro.backendgs.model.dto.JwtResponse;
import com.pietro.backendgs.model.dto.SigninRequest;
import com.pietro.backendgs.model.dto.SignupRequest;
import com.pietro.backendgs.model.entity.Usuario;
import com.pietro.backendgs.model.repository.UsuarioRepository;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String authenticate(Authentication authentication) {
        return jwtService.generateToken(authentication);
    }

    public JwtResponse authenticateUser(SigninRequest signinRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = authenticate(authentication);
        
        Usuario usuario = getUsuarioAutenticado();
        
        return new JwtResponse(
                jwt, 
                usuario.getIdUsuario(), 
                usuario.getNome(), 
                usuario.getEmail());
    }

    public void registerUser(SignupRequest signupRequest) {
        // Verifique se o email já existe
        if (usuarioRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email já está em uso!");
        }

        // Verifique se as senhas coincidem
        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            throw new PasswordMismatchException("As senhas não coincidem!");
        }

        // Crie um novo usuário
        Usuario usuario = new Usuario();
        usuario.setNome(signupRequest.getName());
        usuario.setEmail(signupRequest.getEmail());
        usuario.setSenha(passwordEncoder.encode(signupRequest.getPassword()));
        usuario.setDataNascimento(signupRequest.getBirthDate());

        usuarioRepository.save(usuario);
    }

    public Usuario getUsuarioAutenticado() throws GsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario authenticatedUser = null;

        if (authentication != null && authentication.isAuthenticated()) {
            String login = authentication.getName();
            authenticatedUser = usuarioRepository.findByEmail(login)
                    .orElseThrow(() -> new GsException("Usuário não encontrado.", HttpStatus.BAD_REQUEST));
        }

        if (authenticatedUser == null) {
            throw new GsException("Usuário não encontrado.", HttpStatus.BAD_REQUEST);
        }

        return authenticatedUser;
    }
} 