package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.service;

import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto.AlertRequest;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto.AlertResponse;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.entity.Alert;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.entity.User;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.repository.AlertRepository;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private UserRepository userRepository; // Necessário para associar o alerta a um usuário

    // Métodos de Conversão
    private AlertResponse toAlertResponse(Alert alert) {
        return AlertResponse.builder()
                .id(alert.getId())
                .eventType(alert.getEventType())
                .description(alert.getDescription())
                .latitude(alert.getLatitude())
                .longitude(alert.getLongitude())
                .severity(alert.getSeverity())
                .timestamp(alert.getTimestamp())
                .userId(alert.getUser().getId()) // Pega o ID do usuário associado
                .userName(alert.getUser().getName()) // Pega o nome do usuário associado
                .build();
    }

    private Alert toAlertEntity(AlertRequest request, User user) {
        Alert alert = new Alert();
        alert.setEventType(request.getEventType());
        alert.setDescription(request.getDescription());
        alert.setLatitude(request.getLatitude());
        alert.setLongitude(request.getLongitude());
        alert.setSeverity(request.getSeverity());
        alert.setUser(user); // Define o usuário que reportou
        // timestamp é gerado automaticamente na entidade com @PrePersist
        return alert;
    }

    // --- Operações CRUD ---

    @Transactional
    public AlertResponse createAlert(AlertRequest request, Long userId) {
        // Encontra o usuário para associar ao alerta
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado para criar o alerta com ID: " + userId));

        Alert alert = toAlertEntity(request, user);
        Alert savedAlert = alertRepository.save(alert);
        return toAlertResponse(savedAlert);
    }

    @Transactional(readOnly = true)
    public Page<AlertResponse> getAllAlerts(
            String eventType, // Filtro por tipo de evento
            String severity,  // Filtro por severidade
            Pageable pageable // Paginação e ordenação
    ) {
        // Se ambos os filtros estiverem vazios, retorna todos os alertas paginados
        if (eventType == null || eventType.isEmpty()) eventType = "";
        if (severity == null || severity.isEmpty()) severity = "";

        // Usa o método do repositório para filtrar e paginar
        return alertRepository.findByEventTypeContainingIgnoreCaseAndSeverityContainingIgnoreCase(
                        eventType, severity, pageable)
                .map(this::toAlertResponse);
    }

    @Transactional(readOnly = true)
    public AlertResponse getAlertById(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alerta não encontrado com ID: " + id));
        return toAlertResponse(alert);
    }

    @Transactional
    public AlertResponse updateAlert(Long id, AlertRequest request, Long userId) {
        Alert existingAlert = alertRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alerta não encontrado com ID: " + id));

        // Opcional: validar se o userId que está tentando atualizar é o mesmo que criou o alerta
        // if (!existingAlert.getUser().getId().equals(userId)) {
        //     throw new SecurityException("Você não tem permissão para atualizar este alerta.");
        // }

        // Atualiza os campos do alerta
        existingAlert.setEventType(request.getEventType());
        existingAlert.setDescription(request.getDescription());
        existingAlert.setLatitude(request.getLatitude());
        existingAlert.setLongitude(request.getLongitude());
        existingAlert.setSeverity(request.getSeverity());
        // timestamp não é atualizado, pois é a data de criação

        Alert updatedAlert = alertRepository.save(existingAlert);
        return toAlertResponse(updatedAlert);
    }

    @Transactional
    public void deleteAlert(Long id, Long userId) {
        Alert existingAlert = alertRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alerta não encontrado com ID: " + id));

        // Opcional: validar se o userId que está tentando deletar é o mesmo que criou o alerta
        // if (!existingAlert.getUser().getId().equals(userId)) {
        //     throw new SecurityException("Você não tem permissão para deletar este alerta.");
        // }

        alertRepository.deleteById(id);
    }
}