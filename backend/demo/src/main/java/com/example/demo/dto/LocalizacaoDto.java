package com.example.demo.dto;

import java.util.UUID;

public class LocalizacaoDto {

	private UUID id;
	private String nome;
	private String descricao;
	private EnderecoDto endereco;

	public LocalizacaoDto() {
	}

	public LocalizacaoDto(UUID id, String nome, String descricao, EnderecoDto endereco) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.endereco = endereco;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public EnderecoDto getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoDto endereco) {
		this.endereco = endereco;
	}
}
