package com.example.demo.servico;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entidade.Endereco;
import com.example.demo.entidade.Localizacao;
import com.example.demo.repositorio.LocalizacaoRepositorio;

@Service
public class LocalizacaoServico implements ILocalizacaoServico {

	private final LocalizacaoRepositorio localizacaoRepositorio;
	private final EnderecoServico enderecoServico;

	public LocalizacaoServico(LocalizacaoRepositorio localizacaoRepositorio, EnderecoServico enderecoServico) {
		this.localizacaoRepositorio = localizacaoRepositorio;
		this.enderecoServico = enderecoServico;
	}

	@Override
	public List<Localizacao> listar() {
		return localizacaoRepositorio.findAllComEndereco();
	}

	@Override
	public Localizacao criar(Localizacao localizacao) {
		Endereco endereco = localizacao.getEndereco();
		if (endereco == null) {
			throw new IllegalArgumentException("Endereço é obrigatório para criar uma localização.");
		}
		endereco.setLocalizacao(localizacao);
		endereco = enderecoServico.salvar(endereco);
		localizacao.setEndereco(endereco);
		return localizacaoRepositorio.save(localizacao);
	}
}
