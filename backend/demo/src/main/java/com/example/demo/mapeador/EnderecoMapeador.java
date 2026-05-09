package com.example.demo.mapeador;

import org.springframework.stereotype.Component;

import com.example.demo.dto.EnderecoDto;
import com.example.demo.entidade.Endereco;

@Component
public class EnderecoMapeador {

	public EnderecoDto paraDto(Endereco endereco) {
		if (endereco == null) {
			return null;
		}
		return new EnderecoDto(
				endereco.getId(),
				endereco.getLogradouro(),
				endereco.getNumero(),
				endereco.getComplemento(),
				endereco.getBairro(),
				endereco.getCidade(),
				endereco.getEstado(),
				endereco.getCep());
	}

	public Endereco paraEntidade(EnderecoDto dto) {
		if (dto == null) {
			return null;
		}
		Endereco endereco = new Endereco();
		endereco.setId(dto.getId());
		endereco.setLogradouro(dto.getLogradouro());
		endereco.setNumero(dto.getNumero());
		endereco.setComplemento(dto.getComplemento());
		endereco.setBairro(dto.getBairro());
		endereco.setCidade(dto.getCidade());
		endereco.setEstado(dto.getEstado());
		endereco.setCep(dto.getCep());
		return endereco;
	}
}
