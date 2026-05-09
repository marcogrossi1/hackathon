package com.example.demo.servico;

import java.util.List;

import com.example.demo.entidade.Localizacao;

public interface ILocalizacaoServico {

	List<Localizacao> listar();

	Localizacao criar(Localizacao localizacao);
}
