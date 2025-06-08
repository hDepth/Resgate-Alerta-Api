package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder; // Adicione esta importação para usar @Builder
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Útil para construir objetos de forma fluida
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    // Não incluir a senha aqui por segurança!
}