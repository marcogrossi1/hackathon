package com.example.demo.servico;

import java.util.List;

import com.example.demo.entidade.Equipamento;

public interface IEquipamentoServico {

	List<Equipamento> listar();

	Equipamento obtemPorCodigo(String codigo);

	Equipamento criar(Equipamento equipamento);

	Equipamento atualizarStatus(String codigo, String status);
}
