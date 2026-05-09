package com.example.demo.repositorio;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entidade.Cliente;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, UUID> {

    Optional<Cliente> findByCpf(String cpf);
}
