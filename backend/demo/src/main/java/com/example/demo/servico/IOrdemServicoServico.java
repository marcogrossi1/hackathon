package com.example.demo.servico;

import java.util.List;
import java.util.UUID;

import com.example.demo.entidade.OrdemServico;

public interface IOrdemServicoServico {

	OrdemServico cria(OrdemServico ordemServico);

	void deleta(UUID id);

	OrdemServico atualiza(OrdemServico ordemServico);

	OrdemServico obtemPorId(UUID id);

	List<OrdemServico> lista();
}
