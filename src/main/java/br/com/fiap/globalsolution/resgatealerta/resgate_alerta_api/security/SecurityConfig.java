package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público para o endpoint de login (gerar token)
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        // Permite acesso público para o endpoint de criação de usuário
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        // Permite acesso público aos endpoints do Swagger (documentação da API)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Permite acesso público aos endpoints do Actuator (monitoramento da aplicação)
                        .requestMatchers("/actuator/**").permitAll()
                        // **MODIFICAÇÃO CRÍTICA AQUI:** Permite acesso público para *QUALQUER OUTRA REQUISIÇÃO*
                        // Isso desativa completamente as verificações de autenticação/autorização para todas as rotas restantes.
                        // Faça isso APENAS para fins de teste ou demonstração em ambientes NÃO PRODUTIVOS.
                        .anyRequest().permitAll() // <-- ALTERADO DE .authenticated() PARA .permitAll()
                )
                // Adiciona o filtro JWT antes do filtro de autenticação de usuário/senha padrão do Spring Security
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}