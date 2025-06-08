package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.repository;


import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.entity.AreaOfInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface AreaOfInterestRepository extends JpaRepository<AreaOfInterest, Long> {
    Page<AreaOfInterest> findByUserId(Long userId, Pageable pageable);
    // Você pode adicionar métodos para buscar por nome ou coordenadas se fizer sentido para seu app.
}