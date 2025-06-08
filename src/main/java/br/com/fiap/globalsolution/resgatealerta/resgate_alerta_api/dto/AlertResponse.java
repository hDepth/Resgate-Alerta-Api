package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertResponse {
    private Long id;
    private String eventType;
    private String description;
    private Double latitude;
    private Double longitude;
    private String severity;
    private LocalDateTime timestamp;
    private Long userId; // Para identificar qual usuário criou o alerta
    private String userName; // Opcional: para exibir o nome do usuário no app
}