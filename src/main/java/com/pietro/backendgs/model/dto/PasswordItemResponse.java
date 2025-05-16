package com.pietro.backendgs.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordItemResponse {
    private Long id;
    private String name;
    private String password;
} 