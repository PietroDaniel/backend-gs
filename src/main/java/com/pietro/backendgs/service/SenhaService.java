package com.pietro.backendgs.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pietro.backendgs.auth.AuthService;
import com.pietro.backendgs.exception.GsException;
import com.pietro.backendgs.model.dto.SenhaRequest;
import com.pietro.backendgs.model.dto.SenhaResponse;
import com.pietro.backendgs.model.entity.Senha;
import com.pietro.backendgs.model.entity.Usuario;
import com.pietro.backendgs.model.repository.SenhaRepository;

@Service
public class SenhaService {

	@Autowired
	private SenhaRepository senhaRepository;
	
	@Autowired
	private AuthService authService;

	public Senha criarSenha(Senha senha) throws GsException {
		Usuario usuarioAutenticado = authService.getUsuarioAutenticado();
		senha.setUsuario(usuarioAutenticado);
		return senhaRepository.save(senha);
	}
	
	public SenhaResponse criarSenha(SenhaRequest senhaRequest) throws GsException {
		Usuario usuarioAutenticado = authService.getUsuarioAutenticado();
		
		// Verificar se já existe um item com o mesmo nome para o usuário
		if (senhaRepository.existsByNomeAndUsuario(senhaRequest.getName(), usuarioAutenticado)) {
			throw new GsException("Já existe um item com este nome para o usuário.", HttpStatus.BAD_REQUEST);
		}
		
		Senha senha = new Senha();
		senha.setNome(senhaRequest.getName());
		senha.setSenha(senhaRequest.getPassword());
		
		senha.setUsuario(usuarioAutenticado);
		
		Senha senhaSalva = senhaRepository.save(senha);
		return convertToResponse(senhaSalva);
	}

	public void excluirSenha(Long senhaId) throws GsException {
		Usuario usuarioAutenticado = authService.getUsuarioAutenticado();
		// Verify that the password exists and belongs to the current user before deleting
		senhaRepository.findByIdSenhaAndUsuario(senhaId, usuarioAutenticado)
				.orElseThrow(() -> new GsException("Senha não encontrada ou não pertence ao usuário atual.", HttpStatus.NOT_FOUND));
		
		// Delete físico: remover o registro do banco de dados
		senhaRepository.deleteById(senhaId);
	}

	public List<SenhaResponse> listarSenhasDoUsuario() throws GsException {
		Usuario usuarioAutenticado = authService.getUsuarioAutenticado();
		List<Senha> senhas = senhaRepository.findByUsuario(usuarioAutenticado);
		
		return senhas.stream()
				.map(this::convertToResponse)
				.collect(Collectors.toList());
	}

	public SenhaResponse procurarPorId(Long senhaId) throws GsException {
		Usuario usuarioAutenticado = authService.getUsuarioAutenticado();
		Senha senha = senhaRepository.findByIdSenhaAndUsuario(senhaId, usuarioAutenticado)
				.orElseThrow(() -> new GsException("Esta senha não foi encontrada ou não pertence ao usuário atual!", HttpStatus.NOT_FOUND));
		
		return convertToResponse(senha);
	}
	
	public void excluirTodasSenhasDoUsuario() throws GsException {
		Usuario usuarioAutenticado = authService.getUsuarioAutenticado();
		List<Senha> senhasDoUsuario = senhaRepository.findByUsuario(usuarioAutenticado);
		
		if (senhasDoUsuario.isEmpty()) {
			throw new GsException("Nenhuma senha encontrada para exclusão.", HttpStatus.NOT_FOUND);
		}
		
		senhaRepository.deleteAll(senhasDoUsuario);
	}
	
	private SenhaResponse convertToResponse(Senha senha) {
		SenhaResponse response = new SenhaResponse();
		response.setId(senha.getIdSenha());
		response.setName(senha.getNome());
		response.setPassword(senha.getSenha());
		return response;
	}
} 