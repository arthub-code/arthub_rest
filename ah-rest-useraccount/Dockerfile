# Criando a partir de uma imagem do ubuntu (versão mais recente)
FROM ubuntu:latest AS build

# Atualizando os pacotes do meu linux
RUN apt-get update && apt-get install -y openjdk-17-jdk maven

# Definindo o diretório de trabalho
WORKDIR /app-useraccount-ms

# Copiando o meu projeto para o container
COPY . .

# Gerando um .jar do meu app
RUN mvn clean package -DskipTests

# Criando a partir de uma imagem do java 17
FROM openjdk:17-jdk-slim

# Expondo a porta 8082 para uso
EXPOSE 8082

# Copiando o meu jar gerado pelo maven para o container já renomeando para "useraccount-ms.jar"
COPY --from=build /app-useraccount-ms/target/*.jar useraccount-ms.jar

# Executando o jar
ENTRYPOINT ["java", "-jar", "useraccount-ms.jar"]
