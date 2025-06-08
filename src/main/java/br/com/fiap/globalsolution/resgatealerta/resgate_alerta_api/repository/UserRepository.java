package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.repository;


import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // Método para buscar usuário por email (para autenticação)
}