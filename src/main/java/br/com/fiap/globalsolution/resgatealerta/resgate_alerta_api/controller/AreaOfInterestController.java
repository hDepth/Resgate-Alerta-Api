package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.controller;

import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto.AreaOfInterestRequest;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto.AreaOfInterestResponse;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.service.AreaOfInterestService;
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
@RequestMapping("/areas-of-interest")
@Tag(name = "Áreas de Interesse", description = "Gerencia as áreas de interesse definidas pelos usuários")
public class AreaOfInterestController {

    @Autowired
    private AreaOfInterestService areaOfInterestService;

    // AVISO: userId está sendo passado via Path Variable TEMPORARIAMENTE.
    // Com JWT, o userId será extraído do token de autenticação.
    @PostMapping("/user/{userId}")
    @Operation(summary = "Cria uma nova área de interesse", description = "Associa uma nova área de interesse a um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Área de interesse criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<AreaOfInterestResponse> createAreaOfInterest(
            @PathVariable Long userId,
            @RequestBody @Valid AreaOfInterestRequest request) {
        try {
            AreaOfInterestResponse response = areaOfInterestService.createAreaOfInterest(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Lista áreas de interesse por usuário", description = "Retorna uma lista paginada das áreas de interesse de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de áreas retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Page<AreaOfInterestResponse>> getAreasByUserId(
            @PathVariable Long userId,
            @PageableDefault(size = 10, page = 0, sort = "name") Pageable pageable) {
        try {
            // Pode-se adicionar uma verificação se o usuário existe antes de chamar o service
            Page<AreaOfInterestResponse> areas = areaOfInterestService.getAreasByUserId(userId, pageable);
            return ResponseEntity.ok(areas);
        } catch (EntityNotFoundException e) { // Se o serviço lançar por não encontrar o usuário
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca área de interesse por ID", description = "Retorna os detalhes de uma área de interesse específica pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Área de interesse encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Área de interesse não encontrada")
    })
    public ResponseEntity<AreaOfInterestResponse> getAreaOfInterestById(@PathVariable Long id) {
        try {
            AreaOfInterestResponse area = areaOfInterestService.getAreaOfInterestById(id);
            return ResponseEntity.ok(area);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}/user/{userId}") // userId ainda necessário para validação/contexto
    @Operation(summary = "Atualiza uma área de interesse", description = "Atualiza os dados de uma área de interesse existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Área de interesse atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Área de interesse não encontrada")
    })
    public ResponseEntity<AreaOfInterestResponse> updateAreaOfInterest(
            @PathVariable Long id,
            @PathVariable Long userId,
            @RequestBody @Valid AreaOfInterestRequest request) {
        try {
            AreaOfInterestResponse updatedArea = areaOfInterestService.updateAreaOfInterest(id, request, userId);
            return ResponseEntity.ok(updatedArea);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}/user/{userId}") // userId ainda necessário para validação/contexto
    @Operation(summary = "Exclui uma área de interesse", description = "Remove uma área de interesse do sistema pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Área de interesse excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Área de interesse não encontrada")
    })
    public ResponseEntity<Void> deleteAreaOfInterest(
            @PathVariable Long id,
            @PathVariable Long userId) {
        try {
            areaOfInterestService.deleteAreaOfInterest(id, userId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}