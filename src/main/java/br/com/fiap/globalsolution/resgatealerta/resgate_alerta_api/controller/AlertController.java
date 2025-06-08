package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.controller;

import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto.AlertRequest;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto.AlertResponse;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/alerts")
@Tag(name = "Alertas", description = "Gerencia o reporte e consulta de alertas de eventos extremos")
public class AlertController {

    @Autowired
    private AlertService alertService;

    // AVISO: userId está sendo passado via Path Variable TEMPORARIAMENTE.
    // Com JWT, o userId será extraído do token de autenticação.
    @PostMapping("/user/{userId}")
    @Operation(summary = "Cria um novo alerta", description = "Reporta um novo evento extremo associado a um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alerta criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<AlertResponse> createAlert(
            @PathVariable Long userId,
            @RequestBody @Valid AlertRequest request) {
        try {
            AlertResponse response = alertService.createAlert(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Lista todos os alertas", description = "Retorna uma lista paginada e filtrável de alertas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de alertas retornada com sucesso")
    })
    public ResponseEntity<Page<AlertResponse>> getAllAlerts(
            @PageableDefault(size = 10, page = 0, sort = "timestamp,desc") Pageable pageable, // Ordena por data decrescente
            @RequestParam(required = false) @Parameter(description = "Filtra por tipo de evento (ex: inundacao)") String eventType,
            @RequestParam(required = false) @Parameter(description = "Filtra por severidade (ex: alta)") String severity) {

        Page<AlertResponse> alerts = alertService.getAllAlerts(eventType, severity, pageable);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca alerta por ID", description = "Retorna os detalhes de um alerta específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerta encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Alerta não encontrado")
    })
    public ResponseEntity<AlertResponse> getAlertById(@PathVariable Long id) {
        try {
            AlertResponse alert = alertService.getAlertById(id);
            return ResponseEntity.ok(alert);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}/user/{userId}") // userId ainda necessário para validação/contexto
    @Operation(summary = "Atualiza um alerta", description = "Atualiza os dados de um alerta existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerta atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Alerta não encontrado")
            // @ApiResponse(responseCode = "403", description = "Sem permissão") // Futuramente
    })
    public ResponseEntity<AlertResponse> updateAlert(
            @PathVariable Long id,
            @PathVariable Long userId,
            @RequestBody @Valid AlertRequest request) {
        try {
            AlertResponse updatedAlert = alertService.updateAlert(id, request, userId);
            return ResponseEntity.ok(updatedAlert);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        // catch (SecurityException e) { throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage()); }
    }

    @DeleteMapping("/{id}/user/{userId}") // userId ainda necessário para validação/contexto
    @Operation(summary = "Exclui um alerta", description = "Remove um alerta do sistema pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Alerta excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Alerta não encontrado")
            // @ApiResponse(responseCode = "403", description = "Sem permissão") // Futuramente
    })
    public ResponseEntity<Void> deleteAlert(
            @PathVariable Long id,
            @PathVariable Long userId) {
        try {
            alertService.deleteAlert(id, userId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        // catch (SecurityException e) { throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage()); }
    }
}