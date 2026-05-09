package com.example.demo.controlador;

import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ClienteDto;
import com.example.demo.entidade.Cliente;
import com.example.demo.mapeador.ClienteMapeador;
import com.example.demo.servico.ClienteServico;

@RestController
@RequestMapping(
    //APIProtegidoConfig.PATH + 
    "/api/cliente")
public class ClienteControlador {
    
    private final ClienteServico clienteServico;
    private final ClienteMapeador clienteMapeador;

    public ClienteControlador(ClienteServico clienteServico, ClienteMapeador clienteMapeador) {
        this.clienteServico = clienteServico;
        this.clienteMapeador = clienteMapeador;
    }
    
    @GetMapping("/obtemPorCpf")
    public ClienteDto obtem(@RequestParam(value = "cpf", defaultValue = "") String cpf) {
        Cliente cliente = clienteServico.obtemPorCpf(cpf);
        return cliente == null ? null : clienteMapeador.paraDto(cliente);
    }

    @PostMapping("/criarCliente")
    public ClienteDto criarCliente(@RequestBody ClienteDto dto) {
        return clienteMapeador.paraDto(clienteServico.criar(clienteMapeador.paraEntidade(dto)));
    }
}
