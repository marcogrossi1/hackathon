package com.example.demo.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;

public class OrdemServicoDto {

	private UUID id;
	@JsonAlias({ "clientCpf" })
	private String clienteCpf;
	private String status;
	private String descricao;
	private String area;

	public OrdemServicoDto() {
	}

	public OrdemServicoDto(UUID id, String clienteCpf, String status, String descricao, String area) {
		this.id = id;
		this.clienteCpf = clienteCpf;
		this.status = status;
		this.descricao = descricao;
		this.area = area;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getClienteCpf() {
		return clienteCpf;
	}

	public void setClienteCpf(String clienteCpf) {
		this.clienteCpf = clienteCpf;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
}
