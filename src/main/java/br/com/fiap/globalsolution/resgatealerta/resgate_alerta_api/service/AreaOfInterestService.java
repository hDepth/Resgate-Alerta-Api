package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.service;

import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto.AreaOfInterestRequest;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto.AreaOfInterestResponse;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.entity.AreaOfInterest;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.entity.User;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.repository.AreaOfInterestRepository;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AreaOfInterestService {

    @Autowired
    private AreaOfInterestRepository areaOfInterestRepository;

    @Autowired
    private UserRepository userRepository;

    // Métodos de Conversão
    private AreaOfInterestResponse toAreaOfInterestResponse(AreaOfInterest area) {
        return AreaOfInterestResponse.builder()
                .id(area.getId())
                .name(area.getName())
                .centerLatitude(area.getCenterLatitude())
                .centerLongitude(area.getCenterLongitude())
                .radiusKm(area.getRadiusKm())
                .userId(area.getUser().getId())
                .userName(area.getUser().getName())
                .build();
    }

    private AreaOfInterest toAreaOfInterestEntity(AreaOfInterestRequest request, User user) {
        AreaOfInterest area = new AreaOfInterest();
        area.setName(request.getName());
        area.setCenterLatitude(request.getCenterLatitude());
        area.setCenterLongitude(request.getCenterLongitude());
        area.setRadiusKm(request.getRadiusKm());
        area.setUser(user);
        return area;
    }

    // --- Operações CRUD ---

    @Transactional
    public AreaOfInterestResponse createAreaOfInterest(AreaOfInterestRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado para criar área de interesse com ID: " + userId));

        AreaOfInterest area = toAreaOfInterestEntity(request, user);
        AreaOfInterest savedArea = areaOfInterestRepository.save(area);
        return toAreaOfInterestResponse(savedArea);
    }

    @Transactional(readOnly = true)
    public Page<AreaOfInterestResponse> getAreasByUserId(Long userId, Pageable pageable) {
        // Isso pode ser usado para listar as áreas de interesse de um usuário específico
        return areaOfInterestRepository.findByUserId(userId, pageable)
                .map(this::toAreaOfInterestResponse);
    }

    @Transactional(readOnly = true)
    public AreaOfInterestResponse getAreaOfInterestById(Long id) {
        AreaOfInterest area = areaOfInterestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Área de Interesse não encontrada com ID: " + id));
        return toAreaOfInterestResponse(area);
    }

    @Transactional
    public AreaOfInterestResponse updateAreaOfInterest(Long id, AreaOfInterestRequest request, Long userId) {
        AreaOfInterest existingArea = areaOfInterestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Área de Interesse não encontrada com ID: " + id));

        // Opcional: validar se o userId que está tentando atualizar é o mesmo que criou a área
        // if (!existingArea.getUser().getId().equals(userId)) {
        //     throw new SecurityException("Você não tem permissão para atualizar esta área de interesse.");
        // }

        existingArea.setName(request.getName());
        existingArea.setCenterLatitude(request.getCenterLatitude());
        existingArea.setCenterLongitude(request.getCenterLongitude());
        existingArea.setRadiusKm(request.getRadiusKm());

        AreaOfInterest updatedArea = areaOfInterestRepository.save(existingArea);
        return toAreaOfInterestResponse(updatedArea);
    }

    @Transactional
    public void deleteAreaOfInterest(Long id, Long userId) {
        AreaOfInterest existingArea = areaOfInterestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Área de Interesse não encontrada com ID: " + id));

        // Opcional: validar se o userId que está tentando deletar é o mesmo que criou a área
        // if (!existingArea.getUser().getId().equals(userId)) {
        //     throw new SecurityException("Você não tem permissão para deletar esta área de interesse.");
        // }

        areaOfInterestRepository.deleteById(id);
    }
}