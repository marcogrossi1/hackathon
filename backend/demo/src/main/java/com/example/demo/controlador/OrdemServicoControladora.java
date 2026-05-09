package com.example.demo.controlador;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/ordemServico")
public class OrdemServicoControladora {

	private static final Logger log = LoggerFactory.getLogger(OrdemServicoControladora.class);
	private static final URI WEBHOOK_NOVO_STATUS_URI = URI
			.create("https://ploudos.app.n8n.cloud/webhook-test/receber-mensagem");
	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(5))
			.build();

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
		if (entidade.getEquipamento() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Informe equipamentoId com o id de um equipamento já cadastrado (POST /equipamento/criarEquipamento).");
		}
		try {
			return ordemServicoMapeador.paraDto(ordemServicoServico.cria(entidade));
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

	@DeleteMapping("/deleta")
	public ResponseEntity<Void> deleta(@RequestBody UUID id) {
		ordemServicoServico.deleta(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/atualiza")
	public OrdemServicoDto atualiza(@RequestBody OrdemServicoDto dto) {
		OrdemServico entidade = ordemServicoMapeador.paraEntidade(dto);
		OrdemServico atualizado = ordemServicoServico.atualiza(entidade);

		if (atualizado != null) {
			notificaNovoStatus(atualizado);
		}

		return atualizado == null ? null : ordemServicoMapeador.paraDto(atualizado);
	}

	private void notificaNovoStatus(OrdemServico ordem) {
		String novoStatus = ordem.getStatus();
		UUID id = ordem.getId();
		try {
			String body = "{\"id\":" + jsonStringOrNull(id == null ? null : id.toString())
					+ ",\"status\":" + jsonStringOrNull(novoStatus)
					+ ",\"mensagem\":" + jsonStringOrNull("Status da ordem " + id + " atualizado para " + novoStatus)
					+ "}";
			HttpRequest request = HttpRequest.newBuilder(WEBHOOK_NOVO_STATUS_URI)
					.timeout(Duration.ofSeconds(5))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.build();
			HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.discarding())
					.whenComplete((response, error) -> {
						if (error != null) {
							log.warn("Falha ao notificar webhook de novo status para ordem {}: {}", id,
									error.getMessage());
						} else if (response.statusCode() >= 400) {
							log.warn("Webhook de novo status retornou {} para ordem {}", response.statusCode(), id);
						}
					});
		} catch (Exception e) {
			log.warn("Erro ao preparar notificação de novo status para ordem {}: {}", id, e.getMessage());
		}
	}

	private static String jsonStringOrNull(String value) {
		if (value == null) {
			return "null";
		}
		StringBuilder sb = new StringBuilder(value.length() + 2);
		sb.append('"');
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			switch (c) {
				case '"' -> sb.append("\\\"");
				case '\\' -> sb.append("\\\\");
				case '\b' -> sb.append("\\b");
				case '\f' -> sb.append("\\f");
				case '\n' -> sb.append("\\n");
				case '\r' -> sb.append("\\r");
				case '\t' -> sb.append("\\t");
				default -> {
					if (c < 0x20) {
						sb.append(String.format("\\u%04x", (int) c));
					} else {
						sb.append(c);
					}
				}
			}
		}
		sb.append('"');
		return sb.toString();
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
