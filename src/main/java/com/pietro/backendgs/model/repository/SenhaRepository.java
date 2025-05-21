package com.pietro.backendgs.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pietro.backendgs.model.entity.Senha;
import com.pietro.backendgs.model.entity.Usuario;

@Repository
public interface SenhaRepository extends JpaRepository<Senha, Long> {
    List<Senha> findByUsuario(Usuario usuario);
    Optional<Senha> findByIdSenhaAndUsuario(Long idSenha, Usuario usuario);
    boolean existsByNomeAndUsuario(String nome, Usuario usuario);
} 