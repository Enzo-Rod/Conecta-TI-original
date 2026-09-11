# 1. Altera a imagem de compilação para Java 25
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

# Copia todos os arquivos do projeto
COPY . .

# Concede permissão e compila o projeto ignorando testes
RUN chmod +x gradlew
RUN ./gradlew clean build -x test --no-daemon

# 2. Altera a imagem de execução para Java 25
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copia o arquivo .jar gerado na etapa anterior
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]