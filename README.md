-----

# API de Simulação de Cadastro e Transações Bancárias

    

-----

## 📋 Sumário

  - [Visão Geral do Projeto](https://www.google.com/search?q=%23-vis%C3%A3o-geral-do-projeto)
  - [Tecnologias Utilizadas](https://www.google.com/search?q=%23-tecnologias-utilizadas)
  - [Principais Funcionalidades](https://www.google.com/search?q=%23-principais-funcionalidades)
  - [Fluxo da Aplicação](https://www.google.com/search?q=%23-fluxo-da-aplica%C3%A7%C3%A3o)
  - [Como Executar](https://www.google.com/search?q=%23-como-executar)
  - [Acessando a Aplicação](https://www.google.com/search?q=%23-acessando-a-aplica%C3%A7%C3%A3o)
  - [Estrutura do Projeto](https://www.google.com/search?q=%23-estrutura-do-projeto)

-----

## 🎯 Visão Geral do Projeto

Esta API RESTful, desenvolvida com **Java** e **Spring Boot**, simula operações bancárias essenciais, como cadastro de clientes e transferências entre contas. O projeto foi desenhado para ser um exemplo prático de uma arquitetura de microsserviços robusta, com separação de responsabilidades, tratamento de exceções global e documentação interativa.

A regra de negócio principal para transações é atômica: a transferência de saldo e a notificação para ambos os clientes são tratadas como uma única operação. **Se o serviço externo de notificação falhar, a transação inteira é revertida (rollback)**, garantindo a consistência dos dados.

-----

## 🚀 Tecnologias Utilizadas

  - **Java 24**: Linguagem de programação principal.
  - **Spring Boot 3.5.0**: Framework para construção de aplicações Java robustas e escaláveis.
  - **Spring Data JPA**: Para abstração e persistência de dados.
  - **Spring Security**: Para configuração de segurança da aplicação.
  - **H2 Database**: Banco de dados relacional em memória para agilidade no desenvolvimento.
  - **Maven**: Ferramenta para gerenciamento de dependências e build.
  - **Lombok**: Reduz a verbosidade do código Java.
  - **Springdoc-OpenAPI**: Para geração automática da documentação interativa da API com Swagger UI.
  - **Caelum Stella Core**: Biblioteca para validação de documentos brasileiros (CPF e CNPJ).

-----

## ✨ Principais Funcionalidades

  - **Cadastro de Clientes**: Permite criar clientes com CPF ou CNPJ, com validação de formato e unicidade.
  - **Criação de Contas**: Associa uma ou mais contas a um cliente no momento do cadastro.
  - **Transferência entre Contas**: Realiza transferências financeiras com validação de saldo, status da conta e agência.
  - **Notificação Externa**: Simula o envio de notificações para um serviço externo após cada transação.
  - **Transações Atômicas**: Garante que a transferência e a notificação ocorram com sucesso, ou a operação inteira é desfeita.
  - **Tratamento Global de Exceções**: Fornece respostas de erro claras e padronizadas para o cliente da API.

-----

## 🔄 Fluxo da Aplicação

### Fluxo de Cadastro de Cliente

1.  **Requisição**: O cliente envia um `POST` para `/api/cadastro/clientes` com um `CriarClienteRequestDTO` no corpo.
2.  **Controller (`ClienteController`)**: Recebe a requisição, valida os dados de entrada (`@Valid`) e a encaminha para o `ClienteService`.
3.  **Service (`ClienteService`)**:
      - Valida o formato do documento (CPF/CNPJ) e sua unicidade no banco.
      - Criptografa a senha do cliente.
      - Mapeia o DTO para as entidades `Cliente`, `Endereco` e `Conta`.
      - Salva o novo cliente e suas contas associadas no banco de dados em uma única transação.
4.  **Resposta**: O `ClienteService` retorna um `ClienteResponseDTO` com os dados do cliente criado, e o `ClienteController` envia a resposta com status `201 Created`.

### Fluxo de Transação

1.  **Requisição**: O cliente envia um `POST` para `/api/transacoes/realizar` com um `TransacaoRequestDTO`.
2.  **Controller (`TransacaoController`)**: Recebe a requisição, valida os dados e a repassa para o `TransacaoService`.
3.  **Service (`TransacaoService`)**:
      - Inicia um bloco transacional (`@Transactional`).
      - Busca as contas de origem e destino.
      - Valida as regras de negócio (contas ativas, saldo suficiente, agências correspondentes).
      - Atualiza os saldos em memória.
      - Chama o `NotificacaoService` para notificar ambas as contas.
4.  **Notificação (`NotificacaoService`)**:
      - Tenta enviar a notificação para o endpoint externo (`https://run.mocky.io...`).
      - **Se a chamada falhar**: Lança uma `ResponseStatusException`.
5.  **Resultado da Transação**:
      - **Se a notificação for bem-sucedida**: O `TransacaoService` completa o método sem erros, e o `@Transactional` **confirma (commit)** as alterações de saldo no banco.
      - **Se a notificação falhar**: A exceção lançada pelo `NotificacaoService` interrompe o `TransacaoService`, e o `@Transactional` **reverte (rollback)** as alterações de saldo. Nada é salvo no banco.
6.  **Resposta**:
      - **Em caso de sucesso**: O `TransacaoController` retorna uma resposta `200 OK`.
      - **Em caso de falha**: O `GlobalExceptionHandler` captura a exceção e retorna uma resposta de erro padronizada (ex: `503 Service Unavailable`).

-----

## ▶️ Como Executar

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/gbschaves/apisimulacao.git
    ```
2.  **Navegue até o diretório do projeto:**
    ```bash
    cd apisimulacao
    ```
3.  **Compile e execute com Maven:**
    ```bash
    mvn spring-boot:run
    ```
    A aplicação será iniciada na porta `8080`.

-----

## 🌐 Acessando a Aplicação

### Documentação da API (Swagger UI)

Acesse a documentação interativa para visualizar e testar todos os endpoints.

  * **URL:** `http://localhost:8080/swagger-ui.html`

### Console do Banco de Dados (H2)

Acesse o console do H2 para visualizar os dados em tempo real.

  * **URL:** `http://localhost:8080/h2-console`
  * **Credenciais:**
      * **JDBC URL**: `jdbc:h2:mem:testdb`
      * **User Name**: `dev`
      * **Password**: (deixar em branco)

-----

## 📂 Estrutura do Projeto

```
src/main
├── java/com/testest/apisimulacao
│   ├── ApisimulacaoApplication.java  # Ponto de entrada da aplicação
│   ├── config/                     # Classes de configuração (Swagger, Security, Beans)
│   ├── controller/                 # Controladores REST que expõem os endpoints
│   ├── dto/                        # Data Transfer Objects para controlar os dados de entrada e saída da API
│   ├── entity/                     # Entidades JPA que mapeiam as tabelas do banco
│   ├── event/                      # Classes de eventos para arquitetura assíncrona (atualmente inativas)
│   ├── exception/                  # Handler global de exceções e classes de erro
│   ├── listener/                   # Listeners de eventos (atualmente inativos)
│   ├── repository/                 # Interfaces do Spring Data JPA para acesso ao banco
│   └── service/                    # Classes que contêm a lógica de negócio
└── resources
    ├── application.properties      # Configurações principais da aplicação
    └── openapi-examples/           # Exemplos de JSON para a documentação do Swagger
```
