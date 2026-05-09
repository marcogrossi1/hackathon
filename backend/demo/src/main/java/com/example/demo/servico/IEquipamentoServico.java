package com.example.demo.servico;

import com.example.demo.entidade.Equipamento;

public interface IEquipamentoServico {

	Equipamento obtemPorCodigo(String codigo);

	Equipamento criar(Equipamento equipamento);

	Equipamento atualizarStatus(String codigo, String status);
}
