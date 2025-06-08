package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_RA_ALERT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alert_seq")
    @SequenceGenerator(name = "alert_seq", sequenceName = "SQ_RA_ALERT", allocationSize = 1) // Para Oracle Sequence
    private Long id;

    @NotBlank(message = "O tipo do evento é obrigatório")
    private String eventType; // Ex: "inundacao", "incendio"

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
    private String severity; // Ex: "baixa", "media", "alta"

    @NotNull(message = "A data e hora do alerta são obrigatórias")
    private LocalDateTime timestamp; // Data e hora do reporte

    @ManyToOne // Relacionamento ManyToOne com User
    @JoinColumn(name = "user_id", nullable = false) // Coluna da chave estrangeira
    @NotNull(message = "O usuário que reportou o alerta é obrigatório")
    private User user;

    // Construtor para facilitar a criação de alertas com timestamp automático
    @PrePersist // Garante que o timestamp seja definido antes de persistir
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}