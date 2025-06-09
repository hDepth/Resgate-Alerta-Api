# Estágio de build - usa uma imagem Maven para compilar a aplicação
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo pom.xml e as dependências para o diretório de trabalho
# Isso permite que o Maven baixe as dependências uma vez e as cacheie
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte da aplicação
COPY src ./src

# Compila a aplicação Spring Boot em um JAR executável
RUN mvn clean install -DskipTests

# Estágio de execução - usa uma imagem Java leve para rodar a aplicação
FROM eclipse-temurin:17-jre-alpine

# Define o diretório de trabalho
WORKDIR /app

# Cria um usuário não-root para executar a aplicação
# Substitua 'appuser' pelo nome de usuário que você desejar
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Define uma variável de ambiente (exemplo: para o perfil Spring)
# Pode ser configurado via Docker Compose, mas aqui é um exemplo de uso no Dockerfile
ENV SPRING_PROFILES_ACTIVE=prod

# Copia o JAR executável do estágio de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta que a aplicação Spring Boot usa
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "app.jar"]