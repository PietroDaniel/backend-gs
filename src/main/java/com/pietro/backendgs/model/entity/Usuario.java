package com.pietro.backendgs.model.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idUsuario;

	@NotBlank(message = "Por favor, preencha o nome.")
	@Size(min = 3, max = 200, message = "O nome deve ter entre 3 e 200 caracteres.")
	private String nome;

	@NotBlank(message = "Por favor, preencha a senha.")
	@Size(max = 500, message = "A senha deve ter no máximo 500 caracteres.")
	private String senha;

	@NotBlank(message = "Por favor, preencha o e-mail.")
	@Email(message = "Informe um e-mail válido.")
	@Column(unique = true)
	private String email;

	private LocalDate dataNascimento;

	@OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
	@JsonManagedReference(value = "usuario-senha")
	private List<Senha> senhas;
} 