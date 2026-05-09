package com.example.demo.mapeador;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.demo.dto.EquipamentoDto;
import com.example.demo.entidade.Equipamento;
import com.example.demo.repositorio.LocalizacaoRepositorio;

@Component
public class EquipamentoMapeador {

	private final LocalizacaoRepositorio localizacaoRepositorio;

	public EquipamentoMapeador(LocalizacaoRepositorio localizacaoRepositorio) {
		this.localizacaoRepositorio = localizacaoRepositorio;
	}

	public EquipamentoDto paraDto(Equipamento equipamento) {
		if (equipamento == null) {
			return null;
		}
		UUID localizacaoId = equipamento.getLocalizacao() == null ? null : equipamento.getLocalizacao().getId();
		return new EquipamentoDto(
				equipamento.getId(),
				equipamento.getCodigo(),
				equipamento.getNome(),
				equipamento.getDescricao(),
				equipamento.getStatus(),
				localizacaoId);
	}

	public Equipamento paraEntidade(EquipamentoDto dto) {
		if (dto == null) {
			return null;
		}
		Equipamento equipamento = new Equipamento();
		equipamento.setId(dto.getId());
		equipamento.setCodigo(dto.getCodigo());
		equipamento.setNome(dto.getNome());
		equipamento.setDescricao(dto.getDescricao());
		equipamento.setStatus(dto.getStatus());
		if (dto.getLocalizacaoId() != null) {
			equipamento.setLocalizacao(
					localizacaoRepositorio.findById(dto.getLocalizacaoId()).orElse(null));
		}
		return equipamento;
	}
}
