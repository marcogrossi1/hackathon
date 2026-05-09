package com.example.demo.repositorio;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entidade.Endereco;

@Repository
public interface EnderecoRepositorio extends JpaRepository<Endereco, UUID> {

	/**
	 * Persiste ou atualiza o endereço (delega a {@link #save(Object)}).
	 */
	default Endereco salvar(Endereco endereco) {
		return save(endereco);
	}

	/**
	 * Busca por id (delega a {@link #findById(Object)}).
	 */
	default Optional<Endereco> obtemPorId(UUID id) {
		return findById(id);
	}
}
