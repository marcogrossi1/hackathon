package com.example.demo.entidade;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ordem_servico")
public class OrdemServico {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(nullable = false, updatable = false)
	private UUID id;

	@Column(name = "cliente_id", nullable = false)
	private UUID cliente;

	@Column(name = "cliente_cpf", nullable = false)
	private String clienteCpf;

	@Column(nullable = false)
	private String status;

	private String descricao;

	private String area;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "equipamento_id", nullable = false)
	private Equipamento equipamento;

	public OrdemServico() {
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getCliente() {
		return cliente;
	}

	public void setCliente(UUID cliente) {
		this.cliente = cliente;
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

	public Equipamento getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
	}
}
