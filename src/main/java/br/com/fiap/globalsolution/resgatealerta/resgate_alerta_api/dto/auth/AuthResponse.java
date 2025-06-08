package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Construtor sem argumentos
@AllArgsConstructor // Construtor com todos os argumentos
public class AuthResponse {
    private String token;
    private Long userId;
}