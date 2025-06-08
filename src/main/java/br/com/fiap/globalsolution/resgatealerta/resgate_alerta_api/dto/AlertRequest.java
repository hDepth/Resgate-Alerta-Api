package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AlertRequest {

    @NotBlank(message = "O tipo do evento é obrigatório")
    private String eventType;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
    private String description;

    @NotNull(message = "A latitude é obrigatória")
    @DecimalMin(value = "-90.0", message = "Latitude mínima é -90.0")
    @DecimalMax(value = "90.0", message = "Latitude máxima é 90.0")
    private Double latitude;

    @NotNull(message = "A longitude é obrigatória")
    @DecimalMin(value = "-180.0", message = "Longitude mínima é -180.0")
    @DecimalMax(value = "180.0", message = "Longitude máxima é 180.0")
    private Double longitude;

    @NotBlank(message = "A severidade é obrigatória")
    private String severity;

    // Não precisamos do timestamp aqui, pois ele será gerado na entidade
    // Não precisamos do userId aqui, pois ele virá do contexto de autenticação/URL
}