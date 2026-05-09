package com.example.demo.servico;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.entidade.OrdemServico;
import com.example.demo.repositorio.ClienteRepositorio;
import com.example.demo.repositorio.OrdemServicoRepositorio;

@Service
public class OrdemServicoServico implements IOrdemServicoServico {

	private final OrdemServicoRepositorio ordemServicoRepositorio;
	private final ClienteRepositorio clienteRepositorio;

	public OrdemServicoServico(OrdemServicoRepositorio ordemServicoRepositorio,
			ClienteRepositorio clienteRepositorio) {
		this.ordemServicoRepositorio = ordemServicoRepositorio;
		this.clienteRepositorio = clienteRepositorio;
	}

	@Override
	public OrdemServico cria(OrdemServico ordemServico) {
		if (ordemServico.getEquipamento() == null) {
			throw new IllegalArgumentException("equipamentoId obrigatório: informe o id de um equipamento cadastrado.");
		}
		return ordemServicoRepositorio.save(ordemServico);
	}

	@Override
	public void deleta(UUID id) {
		ordemServicoRepositorio.deleteById(id);
	}

	@Override
	public OrdemServico atualiza(OrdemServico ordemServico) {
		if (ordemServico == null || ordemServico.getId() == null) {
			return null;
		}
		return ordemServicoRepositorio.obtemPorIdComEquipamento(ordemServico.getId()).map(existente -> {
			if (ordemServico.getCliente() != null) {
				existente.setCliente(ordemServico.getCliente());
				if (ordemServico.getClienteCpf() != null && !ordemServico.getClienteCpf().isBlank()) {
					existente.setClienteCpf(ordemServico.getClienteCpf());
				} else {
					clienteRepositorio.findById(ordemServico.getCliente())
							.ifPresent(c -> existente.setClienteCpf(c.getCpf()));
				}
			}
			if (ordemServico.getEquipamento() != null) {
				existente.setEquipamento(ordemServico.getEquipamento());
			}
			existente.setStatus(ordemServico.getStatus());
			existente.setDescricao(ordemServico.getDescricao());
			existente.setArea(ordemServico.getArea());
			return ordemServicoRepositorio.save(existente);
		}).orElse(null);
	}

	@Override
	public OrdemServico obtemPorId(UUID id) {
		return ordemServicoRepositorio.obtemPorIdComEquipamento(id).orElse(null);
	}

	@Override
	public List<OrdemServico> lista() {
		return ordemServicoRepositorio.findAllComEquipamento();
	}
}
