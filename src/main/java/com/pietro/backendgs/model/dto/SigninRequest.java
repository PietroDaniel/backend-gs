package com.pietro.backendgs.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequest {
    
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "O email é obrigatório")
    private String email;
    
    @NotBlank(message = "A senha é obrigatória")
    private String password;
} 