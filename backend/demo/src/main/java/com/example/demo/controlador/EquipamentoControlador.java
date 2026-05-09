package com.example.demo.controlador;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.EquipamentoDto;
import com.example.demo.entidade.Equipamento;
import com.example.demo.mapeador.EquipamentoMapeador;
import com.example.demo.servico.EquipamentoServico;

@RestController
@RequestMapping(
		// APIProtegidoConfig.PATH +
		"/api/equipamento")
public class EquipamentoControlador {

	private final EquipamentoServico equipamentoServico;
	private final EquipamentoMapeador equipamentoMapeador;

	public EquipamentoControlador(EquipamentoServico equipamentoServico, EquipamentoMapeador equipamentoMapeador) {
		this.equipamentoServico = equipamentoServico;
		this.equipamentoMapeador = equipamentoMapeador;
	}

	@GetMapping("/listar")
	public List<EquipamentoDto> listar() {
		return equipamentoServico.listar().stream()
				.map(equipamentoMapeador::paraDto)
				.toList();
	}

	@GetMapping("/obtemPorCodigo")
	public EquipamentoDto obtem(@RequestParam(value = "codigo", defaultValue = "") String codigo) {
		Equipamento equipamento = equipamentoServico.obtemPorCodigo(codigo);
		return equipamento == null ? null : equipamentoMapeador.paraDto(equipamento);
	}

	@PostMapping("/criar")
	public EquipamentoDto criarEquipamento(@RequestBody EquipamentoDto dto) {
		return equipamentoMapeador.paraDto(
				equipamentoServico.criar(equipamentoMapeador.paraEntidade(dto)));
	}

	@PutMapping("/atualizarStatus")
	public EquipamentoDto atualizarStatus(
			@RequestParam("codigo") String codigo,
			@RequestParam("status") String status) {
		Equipamento equipamento = equipamentoServico.atualizarStatus(codigo, status);
		return equipamento == null ? null : equipamentoMapeador.paraDto(equipamento);
	}
}
