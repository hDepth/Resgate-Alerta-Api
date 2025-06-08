package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.config; // Ou o pacote onde você o colocou

import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.entity.User; // Caminho para sua classe User
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.repository.UserRepository; // Caminho para seu UserRepository
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.entity.AreaOfInterest; // Caminho para sua classe AreaOfInterest
import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.repository.AreaOfInterestRepository; // Caminho para seu AreaOfInterestRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AreaOfInterestRepository areaOfInterestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existem usuários para não criar duplicados a cada reinício
        if (userRepository.count() == 0) {
            System.out.println("Populando banco de dados com dados iniciais...");

            // Usuário 1
            User user1 = new User();
            user1.setName("Joao Silva");
            user1.setEmail("joao@example.com");
            user1.setPassword(passwordEncoder.encode("senha123")); // Criptografa a senha!
            userRepository.save(user1);
            System.out.println("Usuário 'joao@example.com' criado.");

            // Usuário 2
            User user2 = new User();
            user2.setName("Maria Souza");
            user2.setEmail("maria@example.com");
            user2.setPassword(passwordEncoder.encode("senha456"));
            userRepository.save(user2);
            System.out.println("Usuário 'maria@example.com' criado.");

            // Exemplo de como popular Areas de Interesse para um usuário
            // Verifique se a tabela de AreaOfInterest está vazia para não duplicar
            if (areaOfInterestRepository.count() == 0) {
                AreaOfInterest area1 = new AreaOfInterest();
                area1.setName("São Paulo - Zonas Leste");
                area1.setCenterLatitude(-23.5505); // <-- ALTERADO para centerLatitude
                area1.setCenterLongitude(-46.6333); // <-- ALTERADO para centerLongitude
                area1.setRadiusKm(5.0); // <-- ALTERADO para radiusKm (5.0 km)
                area1.setUser(user1); // Associa ao user1 (João)
                areaOfInterestRepository.save(area1);
                System.out.println("Área de interesse 'São Paulo - Zonas Leste' criada para João.");

                AreaOfInterest area2 = new AreaOfInterest();
                area2.setName("Rio de Janeiro - Copacabana");
                area2.setCenterLatitude(-22.9712);
                area2.setCenterLongitude(-43.1850);
                area2.setRadiusKm(2.5); // 2.5 km
                area2.setUser(user2); // Associa ao user2 (Maria)
                areaOfInterestRepository.save(area2);
                System.out.println("Área de interesse 'Rio de Janeiro - Copacabana' criada para Maria.");
            } else {
                System.out.println("Banco de dados já contém Áreas de Interesse. Pulando população inicial.");
            }

        } else {
            System.out.println("Banco de dados já contém usuários. Pulando a população inicial de usuários e áreas.");
        }
    }
}