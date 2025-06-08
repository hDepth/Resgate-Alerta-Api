package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AreaOfInterestRequest {

    @NotBlank(message = "O nome da área de interesse é obrigatório")
    @Size(max = 100, message = "O nome da área deve ter no máximo 100 caracteres")
    private String name;

    @NotNull(message = "A latitude central é obrigatória")
    @DecimalMin(value = "-90.0", message = "Latitude mínima é -90.0")
    @DecimalMax(value = "90.0", message = "Latitude máxima é 90.0")
    private Double centerLatitude;

    @NotNull(message = "A longitude central é obrigatória")
    @DecimalMin(value = "-180.0", message = "Longitude mínima é -180.0")
    @DecimalMax(value = "180.0", message = "Longitude máxima é 180.0")
    private Double centerLongitude;

    @NotNull(message = "O raio é obrigatório")
    @Positive(message = "O raio deve ser um valor positivo")
    private Double radiusKm;

    // userId virá do contexto de autenticação/URL
}