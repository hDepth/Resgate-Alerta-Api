Resgate Alerta API





###📖 Descrição do Projeto
A Resgate Alerta API é o coração do sistema "Resgate Alerta", uma solução desenvolvida para otimizar a comunicação e resposta em situações de emergência. Seu principal objetivo é conectar a comunidade, permitindo que usuários relatem incidentes rapidamente e recebam informações cruciais sobre emergências em suas áreas de interesse.

###🎯 Propósito e Problema Resolvido
Em momentos de crise, a agilidade na comunicação pode salvar vidas. Esta API foi concebida para resolver o problema da lentidão e ineficiência na propagação de informações durante emergências. Ela fornece uma plataforma robusta para:

Registro e Gerenciamento de Usuários: Permitindo que indivíduos se cadastrem e gerenciem seus perfis de forma segura.
Criação e Consulta de Alertas: Oferecendo mecanismos para que usuários relatem diversos tipos de emergências e consultem alertas existentes.
Integração de Dados: Atuando como a camada de comunicação entre o aplicativo móvel (front-end) e o banco de dados.

###🚀 Tecnologias Utilizadas
A API foi construída com tecnologias modernas e robustas para garantir performance, segurança e escalabilidade:

Spring Boot 3.x: Framework Java para desenvolvimento rápido e eficiente de aplicações.
Java 17: Linguagem de programação robusta e amplamente utilizada.
Spring Data JPA: Simplifica a interação com o banco de dados.
Hibernate: Implementação de ORM (Object-Relational Mapping).
Maven: Ferramenta de gerenciamento de dependências e construção do projeto.
Spring Security: Para autenticação, autorização e proteção dos endpoints da API.
JWT (JSON Web Tokens): Para autenticação segura e stateless.
Jakarta Validation (Bean Validation): Para validação de dados de entrada.
Lombok: Para reduzir o código boilerplate (getters, setters, construtores).
Springdoc OpenAPI (Swagger UI): Para documentação interativa da API.
Banco de Dados Relacional: (Oracle)

###✨ Funcionalidades Principais (Endpoints)

A API oferece os seguintes grupos de funcionalidades:

👤 Usuários (/users)
Criação de Usuário: Registra um novo usuário no sistema. (POST /users)
Listagem de Usuários: Retorna uma lista paginada de todos os usuários. (GET /users)
Detalhes do Usuário: Busca um usuário específico pelo ID. (GET /users/{id})
Atualização de Usuário: Modifica os dados de um usuário existente. (PUT /users/{id})
Exclusão de Usuário: Remove um usuário do sistema. (DELETE /users/{id})
🚨 Alertas (/alerts)
Criação de Alerta: Permite que um usuário crie e envie um novo alerta de emergência. (POST /alerts)
Listagem de Alertas: Retorna uma lista paginada de alertas, com possibilidade de filtros. (GET /alerts)
Detalhes do Alerta: Busca um alerta específico pelo ID. (GET /alerts/{id})
(Outras funcionalidades de alerta, se aplicável, ex: atualização, exclusão, busca por tipo/severidade)
🔒 Autenticação (/auth)
Login de Usuário: Autentica um usuário e retorna um token JWT para acesso seguro à API. (POST /auth/login)

###🛠️ Como Rodar a API Localmente
Siga os passos abaixo para configurar e executar a API em seu ambiente local:

Pré-requisitos:

JDK 17 ou superior instalado.
Maven instalado.
(Opcional) Docker e Docker Compose, se você usar para o banco de dados.
(Opcional) Um cliente Git.
Passos:

Clone o Repositório:

```Bash

git clone https://github.com/hDepth/Resgate-Alerta-Api.git
cd Resgate-Alerta-Api
```
Configuração do Banco de Dados:

Abra o arquivo src/main/resources/application.properties (ou application.yml).
Configure as credenciais e o URL do seu banco de dados. Para bancos de dados externos (PostgreSQL, MySQL, etc.), atualize as propriedades spring.datasource.url, spring.datasource.username, spring.datasource.password.
Compilar e Executar o Projeto:

```Bash

mvn clean install
mvn spring-boot:run
```
Alternativamente, você pode usar sua IDE (IntelliJ IDEA, Eclipse) para executar a classe principal da aplicação (ResgateAlertaApiApplication.java).

Acessar a API:
A API estará rodando em http://localhost:8080 (ou na porta configurada no application.properties).

📚 Documentação da API (Swagger UI)
Após iniciar a API localmente, você pode acessar a documentação interativa da API através do Swagger UI em:

http://localhost:8080/swagger-ui.html
Esta interface permite que você visualize todos os endpoints disponíveis, seus métodos, parâmetros esperados e exemplos de resposta, além de testar as requisições diretamente do navegador.


📧 Autor(es)
Pedro Henrique Jorge de Paula - Rm 558833
Diego Bassalo Canals Silva - Rm 558710
Lucas Solimães RM 558506 2TDSR (Aclimação)

-----
