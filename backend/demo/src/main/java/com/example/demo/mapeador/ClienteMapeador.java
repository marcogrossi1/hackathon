package com.example.demo.mapeador;

import org.springframework.stereotype.Component;

import com.example.demo.dto.ClienteDto;
import com.example.demo.entidade.Cliente;

@Component
public class ClienteMapeador {

    public ClienteDto paraDto(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        return new ClienteDto(cliente.getId(), cliente.getNome(), cliente.getCpf());
    }

    public Cliente paraEntidade(ClienteDto dto) {
        if (dto == null) {
            return null;
        }
        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        return cliente;
    }
}
