// src/main/java/br/com/fiap/globalsolution/resgatealerta/resgate_alerta_api/dto/UserUpdateRequest.java
package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor; // Necessário para Lombok
import lombok.Data;         // Necessário para Lombok
import lombok.NoArgsConstructor; // Necessário para Lombok

@Data // Lombok: gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Construtor sem argumentos para serialização/desserialização
@AllArgsConstructor // Construtor com todos os argumentos para conveniência
public class UserUpdateRequest {

    @NotBlank(message = "O nome é obrigatório para atualização")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String name;

    @NotBlank(message = "O email é obrigatório para atualização")
    @Email(message = "Formato de email inválido")
    private String email;

    // A senha é omitida aqui porque a atualização de senha é, idealmente, um processo separado
    // e não deve ser exigida toda vez que o nome ou email são atualizados.
}