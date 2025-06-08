package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.repository;

import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.entity.Alert;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.entity.User;
import org.springframework.data.domain.Page; // Importar Page
import org.springframework.data.domain.Pageable; // Importar Pageable
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    // Método para apagar todos os alertas de um usuário (já existente)
    void deleteAllByUser(User user);

    // ADICIONE ESTE MÉTODO PARA CORRIGIR O ERRO:
    // Ele será usado no AlertService para buscar alertas por tipo de evento e severidade, com paginação.
    Page<Alert> findByEventTypeContainingIgnoreCaseAndSeverityContainingIgnoreCase(
            String eventType,
            String severity,
            Pageable pageable
    );
}