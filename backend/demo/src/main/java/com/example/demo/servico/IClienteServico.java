package com.example.demo.servico;

import com.example.demo.entidade.Cliente;

public interface IClienteServico {

    Cliente obtemPorCpf(String cpf);

    Cliente criar(Cliente cliente);
}
