package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaOfInterestResponse {
    private Long id;
    private String name;
    private Double centerLatitude;
    private Double centerLongitude;
    private Double radiusKm;
    private Long userId; // Para identificar qual usuário criou a área
    private String userName; // Opcional: para exibir o nome do usuário no app
}