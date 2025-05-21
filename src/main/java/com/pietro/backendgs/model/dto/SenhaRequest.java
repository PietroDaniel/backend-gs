package com.pietro.backendgs.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SenhaRequest {
    
    @NotBlank(message = "O nome do item é obrigatório")
    private String name;
    
    @NotBlank(message = "A senha é obrigatória")
    private String password;
} 