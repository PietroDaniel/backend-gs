package com.pietro.backendgs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pietro.backendgs.exception.GsException;
import com.pietro.backendgs.model.entity.Usuario;
import com.pietro.backendgs.model.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder encoder;

	public void cadastrar(Usuario usuario) throws GsException {
		if (usuarioRepository.existsByEmail(usuario.getEmail())) {
			throw new GsException("O e-mail informado já está cadastrado.", HttpStatus.BAD_REQUEST);
		}
		usuarioRepository.save(usuario);
	}

	public Usuario atualizar(Usuario usuarioASerAtualizado) throws GsException {
		Usuario usuarioExistente = usuarioRepository.findById(usuarioASerAtualizado.getIdUsuario())
				.orElseThrow(() -> new GsException("Usuário não encontrado.", HttpStatus.NOT_FOUND));

		usuarioExistente.setNome(usuarioASerAtualizado.getNome());
		usuarioExistente.setEmail(usuarioASerAtualizado.getEmail());

		if (usuarioASerAtualizado.getSenha() != null && !usuarioASerAtualizado.getSenha().isEmpty()
				&& !usuarioASerAtualizado.getSenha().equals("NO_PASSWORD_UPDATE")) {
			usuarioExistente.setSenha(encoder.encode(usuarioASerAtualizado.getSenha()));
		}

		return usuarioRepository.save(usuarioExistente);
	}

	public void excluir(Long idUsuario) throws GsException {
		usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new GsException("Usuário não encontrado.", HttpStatus.NOT_FOUND));
		usuarioRepository.deleteById(idUsuario);
	}

	public Usuario pesquisarPorId(Long id) throws GsException {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new GsException("Usuário não encontrado.", HttpStatus.NOT_FOUND));
	}
}
