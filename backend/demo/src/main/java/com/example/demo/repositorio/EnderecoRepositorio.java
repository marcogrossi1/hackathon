package com.example.demo.repositorio;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entidade.Endereco;

@Repository
public interface EnderecoRepositorio extends JpaRepository<Endereco, UUID> {
}
