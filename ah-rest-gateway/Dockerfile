# Criando a partir de uma imagem do ubuntu (versão mais recente)
FROM ubuntu:latest AS build

# Atualizando os pacotes do meu linux
RUN apt-get update && apt-get install -y openjdk-17-jdk maven

# Definindo o diretório de trabalho
WORKDIR /app-gateway

# Copiando o meu projeto para o container
COPY . .

# Gerando um .jar do meu app
RUN mvn clean package -DskipTests

# Criando a partir de uma imagem do java 17
FROM openjdk:17-jdk-slim

# Expondo a porta 8080 para uso
EXPOSE 8080

# Copiando o meu jar gerado pelo maven para o container já renomeando para "app-gateway.jar"
COPY --from=build /app-gateway/target/*.jar app-gateway.jar

# Executando o jar
ENTRYPOINT ["java", "-jar", "app-gateway.jar"]
