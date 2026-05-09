package com.example.demo.dto;

import java.util.UUID;

public class EquipamentoDto {

	private UUID id;
	private String codigo;
	private String nome;
	private String descricao;
	private String status;
	private UUID localizacaoId;

	public EquipamentoDto() {
	}

	public EquipamentoDto(UUID id, String codigo, String nome, String descricao, String status, UUID localizacaoId) {
		this.id = id;
		this.codigo = codigo;
		this.nome = nome;
		this.descricao = descricao;
		this.status = status;
		this.localizacaoId = localizacaoId;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UUID getLocalizacaoId() {
		return localizacaoId;
	}

	public void setLocalizacaoId(UUID localizacaoId) {
		this.localizacaoId = localizacaoId;
	}
}
