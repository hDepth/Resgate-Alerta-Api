package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_RA_AREA_INTEREST")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaOfInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "area_interest_seq")
    @SequenceGenerator(name = "area_interest_seq", sequenceName = "SQ_RA_AREA_INTEREST", allocationSize = 1) // Para Oracle Sequence
    private Long id;

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
    private Double radiusKm; // Raio da área de interesse em quilômetros

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "O usuário associado à área de interesse é obrigatório")
    private User user;
}