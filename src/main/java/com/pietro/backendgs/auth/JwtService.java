package com.pietro.backendgs.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.pietro.backendgs.model.entity.Usuario;
import com.pietro.backendgs.security.JwtUtils;
import com.pietro.backendgs.security.UserDetailsImpl;

@Service
public class JwtService {

    @Autowired
    private JwtUtils jwtUtils;

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(userDetails.getId());
        return jwtUtils.generateJwtToken(authentication);
    }
} 