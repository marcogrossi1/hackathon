package com.example.demo.controlador;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LocalizacaoDto;
import com.example.demo.entidade.Localizacao;
import com.example.demo.mapeador.LocalizacaoMapeador;
import com.example.demo.servico.LocalizacaoServico;

@RestController
@RequestMapping(
		// APIProtegidoConfig.PATH +
		"/localizacao")
public class LocalizacaoControlador {

	private final LocalizacaoServico localizacaoServico;
	private final LocalizacaoMapeador localizacaoMapeador;

	public LocalizacaoControlador(LocalizacaoServico localizacaoServico, LocalizacaoMapeador localizacaoMapeador) {
		this.localizacaoServico = localizacaoServico;
		this.localizacaoMapeador = localizacaoMapeador;
	}

	@GetMapping("/listar")
	public List<LocalizacaoDto> listarLocalizacao() {
		return localizacaoServico.listar().stream()
				.map(localizacaoMapeador::paraDto)
				.toList();
	}

	@PostMapping("/criar")
	public LocalizacaoDto criarLocalizacao(@RequestBody LocalizacaoDto dto) {
		Localizacao localizacao = localizacaoMapeador.paraEntidade(dto);
		return localizacaoMapeador.paraDto(localizacaoServico.criar(localizacao));
	}
}
