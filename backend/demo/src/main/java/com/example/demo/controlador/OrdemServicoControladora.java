package com.example.demo.controlador;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrdemServicoDto;
import com.example.demo.entidade.OrdemServico;
import com.example.demo.mapeador.OrdemServicoMapeador;
import com.example.demo.servico.OrdemServicoServico;

@RestController
@RequestMapping("/ordemServico")
public class OrdemServicoControladora {

	private final OrdemServicoServico ordemServicoServico;
	private final OrdemServicoMapeador ordemServicoMapeador;

	public OrdemServicoControladora(OrdemServicoServico ordemServicoServico,
			OrdemServicoMapeador ordemServicoMapeador) {
		this.ordemServicoServico = ordemServicoServico;
		this.ordemServicoMapeador = ordemServicoMapeador;
	}

	@PostMapping("/cria")
	public OrdemServicoDto cria(@RequestBody OrdemServicoDto dto) {
		OrdemServico entidade = ordemServicoMapeador.paraEntidade(dto);
		if (entidade.getCliente() == null
				|| entidade.getClienteCpf() == null
				|| entidade.getClienteCpf().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Informe clienteCpf ou clientCpf com o CPF de um cliente já cadastrado (POST /cliente/criarCliente).");
		}
		return ordemServicoMapeador.paraDto(ordemServicoServico.cria(entidade));
	}

	@DeleteMapping("/deleta")
	public ResponseEntity<Void> deleta(@RequestParam("id") UUID id) {
		ordemServicoServico.deleta(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/atualiza")
	public OrdemServicoDto atualiza(@RequestBody OrdemServicoDto dto) {
		OrdemServico entidade = ordemServicoMapeador.paraEntidade(dto);
		OrdemServico atualizado = ordemServicoServico.atualiza(entidade);

		return atualizado == null ? null : ordemServicoMapeador.paraDto(atualizado);
	}

	@GetMapping("/obtemPorId")
	public OrdemServicoDto obtemPorId(@RequestParam("id") UUID id) {
		OrdemServico os = ordemServicoServico.obtemPorId(id);
		
		return os == null ? null : ordemServicoMapeador.paraDto(os);
	}

	@GetMapping("/lista")
	public List<OrdemServicoDto> lista() {
		return ordemServicoServico.lista().stream()
				.map(ordemServicoMapeador::paraDto)
				.collect(Collectors.toList());
	}
}
