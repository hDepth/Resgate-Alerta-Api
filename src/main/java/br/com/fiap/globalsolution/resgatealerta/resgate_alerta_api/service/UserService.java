package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.service;

import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto.UserRequest;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto.UserResponse;
// NOVO IMPORT: Importa o DTO específico para requisições de atualização
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.dto.UserUpdateRequest;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.entity.User;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.repository.UserRepository;
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.repository.AlertRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AlertRepository alertRepository;


    // Métodos de Conversão (helpers privados)
    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    private User toUserEntity(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }

    // --- Operações CRUD ---

    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário com este email.");
        }
        User user = toUserEntity(request);
        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toUserResponse);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));
        return toUserResponse(user);
    }

    // AQUI ESTÁ A CORREÇÃO: O método 'updateUser' AGORA RECEBE 'UserUpdateRequest'
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) { // ASSINATURA ALTERADA AQUI
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));

        if (!existingUser.getEmail().equals(request.getEmail())) {
            Optional<User> userWithNewEmail = userRepository.findByEmail(request.getEmail());
            if (userWithNewEmail.isPresent() && !userWithNewEmail.get().getId().equals(id)) {
                throw new IllegalArgumentException("O email '" + request.getEmail() + "' já está em uso por outro usuário.");
            }
        }

        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());

        User updatedUser = userRepository.save(existingUser);
        return toUserResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));

        try {
            alertRepository.deleteAllByUser(user);
            System.out.println("Alertas do usuário " + user.getEmail() + " excluídos.");

            userRepository.delete(user);
            System.out.println("Usuário " + user.getEmail() + " excluído com sucesso.");

        } catch (DataIntegrityViolationException e) {
            System.err.println("Erro de integridade de dados ao excluir usuário: " + e.getMessage());
            throw new IllegalStateException("Não foi possível excluir o usuário devido a registros relacionados existentes. Verifique se todas as dependências foram tratadas ou se há FKs sem ON DELETE CASCADE.", e);
        } catch (Exception e) {
            System.err.println("Erro inesperado ao excluir usuário: " + e.getMessage());
            throw new RuntimeException("Erro inesperado ao excluir usuário: " + e.getMessage(), e);
        }
    }
}