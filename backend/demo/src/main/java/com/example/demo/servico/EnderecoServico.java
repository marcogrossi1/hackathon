package com.example.demo.servico;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.entidade.Endereco;
import com.example.demo.repositorio.EnderecoRepositorio;

@Service
public class EnderecoServico implements IEnderecoServico {

	private final EnderecoRepositorio enderecoRepositorio;

	public EnderecoServico(EnderecoRepositorio enderecoRepositorio) {
		this.enderecoRepositorio = enderecoRepositorio;
	}

	@Override
	public Endereco salvar(Endereco endereco) {
		return enderecoRepositorio.save(endereco);
	}

	@Override
	public Optional<Endereco> obtemPorId(UUID id) {
		if (id == null) {
			return Optional.empty();
		}
		return enderecoRepositorio.findById(id);
	}
}
