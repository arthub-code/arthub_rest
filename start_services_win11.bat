@echo off
setlocal

:: Obter o diretório atual do script
set "ROOT_DIR=%~dp0"
cd /d "%ROOT_DIR%"

:: Iniciar o Eureka Server
echo Iniciando o Eureka Server...
cd ah-rest-eureka-server
start cmd /c "mvn spring-boot:run"

:: Esperar o Eureka Server iniciar
call :wait_for_service "http://localhost:8761/"

:: Iniciar o API Gateway
echo Iniciando o API Gateway...
cd ..\ah-rest-gateway
start cmd /c "mvn spring-boot:run"

:: Esperar o API Gateway iniciar
call :wait_for_service "http://localhost:8080/ping"

:: Iniciar o Microserviço de Usuários
echo Iniciando o Microserviço de Usuários...
cd ..\ah-rest-useraccount
start cmd /c "mvn spring-boot:run"

:: Esperar o microserviço de usuários iniciar
call :wait_for_service "http://localhost:8082/ping"

:: Função para esperar até que um serviço esteja disponível
:wait_for_service
set "url=%~1"
set /a max_attempts=5
set attempt=1

:: Verifica se a URL está vazia
if "%url%"=="" (
    echo Erro: URL não especificada.
    exit /b 1
)

:retry
echo Tentando acessar %url% (Tentativa %attempt% de %max_attempts%)...
curl --silent --fail %url% > nul
if %errorlevel% == 0 (
    echo %url% está disponível.
    exit /b 0
) else (
    if %attempt% lss %max_attempts% (
        timeout /t 5
        set /a attempt+=1
        goto retry
    ) else (
        echo Falha ao acessar %url% após %max_attempts% tentativas.
        exit /b 1
    )
)

:: O script termina aqui. Os serviços continuarão em execução.
endlocal
