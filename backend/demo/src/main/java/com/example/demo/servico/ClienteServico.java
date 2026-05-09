package com.example.demo.servico;

import org.springframework.stereotype.Service;

import com.example.demo.entidade.Cliente;
import com.example.demo.repositorio.ClienteRepositorio;

@Service
public class ClienteServico implements IClienteServico {

    private final ClienteRepositorio clienteRepositorio;

    public ClienteServico(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    @Override
    public Cliente obtemPorCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return null;
        }
        return clienteRepositorio.findByCpf(cpf).orElse(null);
    }

    @Override
    public Cliente criar(Cliente cliente) {
        return clienteRepositorio.save(cliente);
    }
}
