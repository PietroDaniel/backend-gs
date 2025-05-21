package com.pietro.backendgs.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Senha {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idSenha;

	private String nome;
	
	private String senha;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	@JsonBackReference("usuario-senha")
	private Usuario usuario;
} 