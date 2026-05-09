package com.example.demo.repositorio;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entidade.Equipamento;

@Repository
public interface EquipamentoRepositorio extends JpaRepository<Equipamento, UUID> {

	Optional<Equipamento> findByCodigo(String codigo);

	@EntityGraph(attributePaths = "localizacao")
	@Query("select e from Equipamento e")
	List<Equipamento> findAllComLocalizacao();

	/**
	 * Persiste ou atualiza o equipamento (delega a {@link #save(Object)}).
	 */
	default Equipamento salvar(Equipamento equipamento) {
		return save(equipamento);
	}

	/**
	 * Busca por código (delega a {@link #findByCodigo(String)}).
	 */
	default Optional<Equipamento> obtemPorCodigo(String codigo) {
		return findByCodigo(codigo);
	}

	/**
	 * Busca por id (delega a {@link #findById(Object)}).
	 */
	default Optional<Equipamento> obtemPorId(UUID id) {
		return findById(id);
	}
}
