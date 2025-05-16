package com.pietro.backendgs.model.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    
    @NotBlank(message = "O nome é obrigatório")
    private String name;
    
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "O email é obrigatório")
    private String email;
    
    @NotBlank(message = "A senha é obrigatória")
    private String password;
    
    @NotBlank(message = "A confirmação de senha é obrigatória")
    private String confirmPassword;
    
    @NotNull(message = "A data de nascimento é obrigatória")
    private LocalDate birthDate;
} 