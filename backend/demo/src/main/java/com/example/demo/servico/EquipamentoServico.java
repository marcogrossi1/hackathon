package com.example.demo.servico;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entidade.Equipamento;
import com.example.demo.repositorio.EquipamentoRepositorio;

@Service
public class EquipamentoServico implements IEquipamentoServico {

	private final EquipamentoRepositorio equipamentoRepositorio;

	public EquipamentoServico(EquipamentoRepositorio equipamentoRepositorio) {
		this.equipamentoRepositorio = equipamentoRepositorio;
	}

	@Override
	public List<Equipamento> listar() {
		return equipamentoRepositorio.findAllComLocalizacao();
	}

	@Override
	public Equipamento obtemPorCodigo(String codigo) {
		if (codigo == null || codigo.isBlank()) {
			return null;
		}
		return equipamentoRepositorio.obtemPorCodigo(codigo).orElse(null);
	}

	@Override
	public Equipamento criar(Equipamento equipamento) {
		if (equipamento.getLocalizacao() == null) {
			throw new IllegalArgumentException("localizacaoId inválido ou localização não encontrada.");
		}
		return equipamentoRepositorio.salvar(equipamento);
	}

	@Override
	public Equipamento atualizarStatus(String codigo, String status) {
		if (status == null || status.isBlank()) {
			throw new IllegalArgumentException("status é obrigatório.");
		}
		Equipamento equipamento = obtemPorCodigo(codigo);
		if (equipamento == null) {
			return null;
		}
		equipamento.setStatus(status.trim());
		return equipamentoRepositorio.salvar(equipamento);
	}
}
