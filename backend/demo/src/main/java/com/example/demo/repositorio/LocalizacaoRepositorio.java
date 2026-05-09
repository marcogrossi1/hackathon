package com.example.demo.repositorio;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entidade.Localizacao;

@Repository
public interface LocalizacaoRepositorio extends JpaRepository<Localizacao, UUID> {

	@EntityGraph(attributePaths = "endereco")
	@Query("select l from Localizacao l")
	List<Localizacao> findAllComEndereco();
}
