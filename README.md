# Arthub Microservices

Arthub é uma ferramenta SaaS para o artista digital que deseja elevar seu nível como artista, melhorando sua qualidade, produtividade, sua organização e suas vendas. A plataforma oferece ferramentas de controle e gestão de comissões, artes e sugestões de precificação dos serviços vendidos, além de oferecer um marketplace simples e atraente para que ele possa conseguir clientes. A plataforma tem como foco o seu perfil como artista para atrair mais clientes e elevar as vendas, sempre ajudando a precificar e também oferece cursos internos sobre artes para melhorar sua qualidade profissional.

## Rotas e Endpoints

### Contas de usuário (/useraccount)

#### Rotas Públicas (sem token)

- **Requisitar uma conta**  
  **POST:** `localhost:8080/useraccount/v1/public/requestCreationUserAccount`  
  **JSON Body:**
  ```json
  {
      "email": "email@gmail.com",
      "socialName": "Your name", 
      "username": "@yourUsername",
      "password": "yourPassword@WithSpecialCaracthrs129",
      "userAccountType": "Artist",
      "fullName": "Your full name",
      "dateOfBirth": "2009-10-04"
  }
  ```

- **Autenticação**
  **POST:** `localhost:8080/useraccount/v1/public/login`
  **JSON Body:**
  ```json
  {
      "email": "email@gmail.com",
      "password": "your-passowrd"
  }
  ```

#### Rotas Privadas (com token - Authorization Bearer Token)
- **Saúde do microserviço**
  **GET:** `localhost:8080/useraccount/actuator/health`
  **HEADER - Authorization: Bearer Token**


### Arte (/art)
#### Rotas Privadas (com token - Authorization Bearer Token)
- **Registrar uma arte**
  **POST:** `localhost:8080/art/v1/create`
  **JSON Body:**
  ```json
  {
      "artName": "Your art name.",
      "haveSchedule": true,
      "accountId": "{account-id}",
      "startScheduleDate": "2024-10-04",
      "endScheduleDate": "2024-10-05",
      "artImageRef": [
          {
              "uploadType": "PINTEREST_API",
              "imageBytes": null,
              "imageLink": "https://i.pinimg.com/564x/dc/63/35/dc63358c2084dba8c066fa932b95190f.jpg"
          },
          ...
      ] 
  }
  ```
  >O campo "imageBytes" deve ser utilizado quando o usuário queira fazer o upload de uma imagem do próprio dispostivo, e para isso o uploadType **deve** ser `UPLOAD_DEVICE`. Você só pode utilizar esse tipo de upload quando informar os bytes da imagem. Caso o usuário queira utilizar imagens do pinterest o uploadType deve ser `PINTEREST_API` e o campo "imageLink" se tornará obrigatório.

- **Listar todas as artes registradas do artistas**
  **GET:** `localhost:8080/art/v1/arts?accountId={account-id}`
  **JSON de RESPOSTA:**
  ```json
  {
      "status": 200,
      "hasErrors": false,
      "data": [
          {
              "artId": "art id...",
              "artName": "your art name...",
              "visibility": "PRIVATE",
              "status": "TODO",
              "imgRefs": [
                  {
                      "imgRefId": "image ref id...",
                      "imageLink": "https://i.pinimg.com/564x/dc/63/35/dc63358c2084dba8c066fa932b95190f.jpg"
                  },
                  ...
              ],
              "haveSchedule": true,
              "startScheduleDate": "2024-10-04",
              "endScheduleDate": "2024-10-05",
              "createdAt": "2024-10-06T17:54:50.969849",
              "createdAtText": "1 day ago",
              "lastModified": "2024-10-06T17:54:50.969872",
              "lastModifiedText": "1 day ago"
          },
          ...
      ]
  }
  ```
<hr/>

## Instruções para Execução

### Usando Docker (docker-compose)

1. Certifique-se de ter o Docker e o Docker Compose instalados na sua máquina.

2. Navegue até o diretório onde está localizado o arquivo `docker-compose.yml`.

3. Execute o seguinte comando:

   ```bash
   docker-compose up --build
   ```

Isso construirá e iniciará os microserviços de acordo com a configuração no `docker-compose.yml`.

4. Após a inicialização, você pode acessar os serviços nos seguintes URLs:

   - **Eureka Server**: [http://localhost:8761](http://localhost:8761)
   - **API Gateway**: [http://localhost:8080](http://localhost:8080)

### Usando Script Batch no Windows

Para executar os microserviços sem Docker, você pode usar o script `start_services_win11.bat`. Siga estas instruções:

1. Navegue até o diretório `resources` de cada microserviço e certifique-se de que o arquivo `application_no_docker_development.yml` ou `application_no_docker_development.properties` está presente. Se estiver, o renomeie para `application.yml` ou `application.properties` e garanta que só exista apenas ele com esse nome na pasta.

2. Execute o script `start_services_win11.bat`. Você pode fazer isso através do terminal ou clicando duas vezes no arquivo.

Este script iniciará todos os microserviços em sua máquina local.

## Observações

- Certifique-se de que as configurações de ambiente e as dependências necessárias estejam corretamente configuradas para cada microserviço.
- O Eureka Server deve estar ativo antes de iniciar o API Gateway e os outros microserviços.

## Contribuição

Se você deseja contribuir para o projeto, sinta-se à vontade para abrir um pull request ou relatar problemas. Todas as contribuições são bem-vindas!

## Licença

Este projeto é licenciado sob a MIT License. Consulte o arquivo LICENSE para mais detalhes.

---

Arthub Microservices, o sistema de serviços da plataforma!
