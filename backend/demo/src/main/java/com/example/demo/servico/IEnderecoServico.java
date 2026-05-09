package com.example.demo.servico;

import java.util.Optional;
import java.util.UUID;

import com.example.demo.entidade.Endereco;

public interface IEnderecoServico {

	Endereco salvar(Endereco endereco);

	Optional<Endereco> obtemPorId(UUID id);
}
