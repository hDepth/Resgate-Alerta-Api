// Exemplo (adapte conforme seu AuthService real)
package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.security;

import br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // Ou a lista de authorities/roles que você usa
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Antes: @Autowired private TokenService tokenService;
    @Autowired // AGORA: Injeta JwtService
    private JwtService jwtService;

    // Método para carregar usuário pelo username (email)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Adapte isso para como seu UserRepository retorna o usuário
        br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.entity.User userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        // Retorna um UserDetails do Spring Security
        return new org.springframework.security.core.userdetails.User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                new ArrayList<>() // Adapte para as roles/authorities reais do seu usuário
        );
    }

    // Adicione um método para gerar o token aqui (se for usado pelo AuthController)
    // Adapte o UserDetails para o seu modelo de usuário se necessário para pegar o ID
    public String getJwtToken(UserDetails userDetails, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId); // Adiciona o ID do usuário como um "claim" no token
        return jwtService.generateToken(claims, userDetails);
    }
}