package com.example.demo.mapeador;

import org.springframework.stereotype.Component;

import com.example.demo.dto.LocalizacaoDto;
import com.example.demo.entidade.Endereco;
import com.example.demo.entidade.Localizacao;

@Component
public class LocalizacaoMapeador {

	private final EnderecoMapeador enderecoMapeador;

	public LocalizacaoMapeador(EnderecoMapeador enderecoMapeador) {
		this.enderecoMapeador = enderecoMapeador;
	}

	public LocalizacaoDto paraDto(Localizacao localizacao) {
		if (localizacao == null) {
			return null;
		}
		return new LocalizacaoDto(
				localizacao.getId(),
				localizacao.getNome(),
				localizacao.getDescricao(),
				enderecoMapeador.paraDto(localizacao.getEndereco()));
	}

	public Localizacao paraEntidade(LocalizacaoDto dto) {
		if (dto == null) {
			return null;
		}
		Localizacao localizacao = new Localizacao();
		localizacao.setId(dto.getId());
		localizacao.setNome(dto.getNome());
		localizacao.setDescricao(dto.getDescricao());

		Endereco endereco = enderecoMapeador.paraEntidade(dto.getEndereco());
		localizacao.setEndereco(endereco);
		if (endereco != null) {
			endereco.setLocalizacao(localizacao);
		}
		return localizacao;
	}
}
