package com.example.demo.repositorio;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entidade.OrdemServico;

@Repository
public interface OrdemServicoRepositorio extends JpaRepository<OrdemServico, UUID> {

	@EntityGraph(attributePaths = { "equipamento", "equipamento.localizacao" })
	@Query("select o from OrdemServico o where o.id = :id")
	Optional<OrdemServico> obtemPorIdComEquipamento(@Param("id") UUID id);

	@EntityGraph(attributePaths = { "equipamento", "equipamento.localizacao" })
	@Query("select o from OrdemServico o")
	List<OrdemServico> findAllComEquipamento();
}
