package com.example.demo.mapeador;

import org.springframework.stereotype.Component;

import com.example.demo.dto.OrdemServicoDto;
import com.example.demo.entidade.Cliente;
import com.example.demo.entidade.OrdemServico;
import com.example.demo.repositorio.ClienteRepositorio;

@Component
public class OrdemServicoMapeador {

	private final ClienteRepositorio clienteRepositorio;

	public OrdemServicoMapeador(ClienteRepositorio clienteRepositorio) {
		this.clienteRepositorio = clienteRepositorio;
	}

	public OrdemServicoDto paraDto(OrdemServico os) {
		if (os == null) {
			return null;
		}
		String cpf = os.getClienteCpf();
		if ((cpf == null || cpf.isBlank()) && os.getCliente() != null) {
			cpf = clienteRepositorio.findById(os.getCliente()).map(Cliente::getCpf).orElse(null);
		}
		return new OrdemServicoDto(os.getId(), cpf, os.getStatus(), os.getDescricao(), os.getArea());
	}

	public OrdemServico paraEntidade(OrdemServicoDto dto) {
		if (dto == null) {
			return null;
		}
		OrdemServico os = new OrdemServico();
		os.setId(dto.getId());
		String cpfInformado = dto.getClienteCpf();
		if (cpfInformado != null && !cpfInformado.isBlank()) {
			String trim = cpfInformado.trim();
			String somenteDigitos = trim.replaceAll("\\D", "");
			clienteRepositorio.findByCpf(trim)
					.or(() -> clienteRepositorio.findByCpf(somenteDigitos))
					.ifPresent(c -> {
						os.setCliente(c.getId());
						os.setClienteCpf(c.getCpf());
					});
		}
		os.setStatus(dto.getStatus());
		os.setDescricao(dto.getDescricao());
		os.setArea(dto.getArea());
		return os;
	}
}
