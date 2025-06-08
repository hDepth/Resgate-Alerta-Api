package br.com.fiap.globalsolution.resgatealerta.resgate_alerta_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement; // <-- Importe esta classe!
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() { // Você pode manter 'springShopOpenAPI' ou usar 'customOpenAPI' como aqui.
        // Mudei para 'customOpenAPI' para consistência com exemplos anteriores.
        final String securitySchemeName = "bearerAuth"; // Nome que será usado para o esquema de segurança JWT

        return new OpenAPI()
                .info(new Info().title("ResgateAlerta API")
                        .description("API RESTful para o aplicativo ResgateAlerta, gerenciando usuários, alertas de eventos extremos e áreas de interesse.")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação da Global Solution FIAP")
                        .url("https://www.fiap.com.br/graduacao/"))
                // Adiciona uma exigência de segurança global para todas as operações da API
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName)) // <-- Esta linha faz o botão "Authorize" aparecer e aplicar a segurança.
                .components(new Components()
                        // Define o esquema de segurança "bearerAuth"
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP) // Tipo de autenticação HTTP
                                .scheme("bearer")             // Esquema Bearer
                                .bearerFormat("JWT")));      // Formato do token é JWT
    }
}