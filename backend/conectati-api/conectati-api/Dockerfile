FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copia todos os arquivos do projeto de uma vez para evitar perda de arquivos do wrapper
COPY . .

# Concede permissão e compila o projeto
RUN chmod +x gradlew
RUN ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]