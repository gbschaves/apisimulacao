# API de Simulação de Cadastro e Transações Bancárias

![Java](https://img.shields.io/badge/Java-24-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-brightgreen)
![H2 Database](https://img.shields.io/badge/H2%20Database-In%20Memory-orange)
![Swagger UI](https://img.shields.io/badge/Swagger%20UI-2.8.8-blueviolet)
![Maven](https://img.shields.io/badge/Maven-3.9.9-red)
![Lombok](https://img.shields.io/badge/Lombok-Enabled-yellow)
![Spring Security](https://img.shields.io/badge/Spring%20Security-Enabled-green)

---

## 📋 Sumário

- [Visão Geral do Projeto](#-visão-geral-do-projeto)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Como Executar a Aplicação](#-como-executar-a-aplicação)
- [Acessando a Aplicação](#-acessando-a-aplicação)
- [Endpoints da API](#-endpoints-da-api)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Considerações Finais](#-considerações-finais)

---

## 🎯 Visão Geral do Projeto

Esta API foi desenvolvida para simular processos de **cadastro de clientes** e **transações bancárias**. O objetivo é fornecer um ambiente para testar e entender como as operações de cliente e conta podem ser gerenciadas em um sistema backend, incluindo validações de documentos (CPF/CNPJ), gerenciamento de saldo e notificação de transações.

A aplicação utiliza o Spring Boot para facilitar o desenvolvimento, um banco de dados H2 em memória para persistência de dados e Swagger UI para documentação interativa dos endpoints.

---

## 🚀 Tecnologias Utilizadas

* **Java 24**: Linguagem de programação principal.
* **Spring Boot 3.5.0**: Framework para construção de aplicações Java robustas e escaláveis.
* **Spring Data JPA**: Para abstração e persistência de dados.
* **H2 Database**: Banco de dados relacional em memória, ideal para desenvolvimento e testes.
* **Maven 3.9.9**: Ferramenta para gerenciamento de dependências e build.
* **Lombok**: Reduz a verbosidade do código, gerando automaticamente getters, setters e construtores.
* **Springdoc-OpenAPI (Swagger UI)**: Para geração automática e interativa da documentação da API.
* **Spring Security**: Para configuração de segurança e acesso público aos endpoints do Swagger.
* **Caelum Stella Core**: Biblioteca utilizada para validação de CPF e CNPJ.
* **RestTemplate**: Para simular o envio de notificações a um serviço externo.

---

## ✅ Pré-requisitos

Antes de iniciar, certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

* **Java Development Kit (JDK) 24** ou superior.
* **Maven 3.9.9** ou superior.
* Uma IDE de sua escolha (IntelliJ IDEA, Eclipse, VS Code, etc.).

---

## ▶️ Como Executar a Aplicação

Siga os passos abaixo para clonar o repositório e executar a aplicação:

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/gbschaves/apisimulacao.git](https://github.com/gbschaves/apisimulacao.git)
    ```

2.  **Navegue até o diretório do projeto:**
    ```bash
    cd apisimulacao
    ```

3.  **Compile e execute a aplicação usando Maven:**
    ```bash
    mvn spring-boot:run
    ```
    A aplicação será iniciada na porta padrão `8080`.

---

## 🌐 Acessando a Aplicação

### Documentação da API (Swagger UI)
A documentação interativa da API está disponível no Swagger UI, onde você pode testar os endpoints diretamente no navegador.

* **URL:** `http://localhost:8080/swagger-ui.html`

### Console do Banco de Dados (H2)
Você pode acessar o console do H2 para visualizar os dados e tabelas em tempo real.

* **URL:** `http://localhost:8080/h2-console`
* **Credenciais (padrão):**
    * **JDBC URL**: `jdbc:h2:mem:testdb`
    * **User Name**: `dev`
    * **Password**: (deixar em branco)

---

## Endpoints da API

### Clientes

#### `POST /api/cadastro/clientes`
* **Descrição**: Cadastra um novo cliente com informações de endereço e contas bancárias iniciais.
* **Requisição**: `CriarClienteRequestDTO` com os seguintes campos:
    * `nome` (String, obrigatório)
    * `tipoDocumento` (Enum: `CPF` ou `CNPJ`, obrigatório)
    * `documento` (String, obrigatório)
    * `senha` (String, mínimo 6 caracteres, obrigatório)
    * `endereco` (`EnderecoRequestDTO`, obrigatório)
    * `contas` (Lista de `ContaRequestDTO`, opcional)
* **Respostas**:
    * `201 Created`: Cliente cadastrado com sucesso.
    * `400 Bad Request`: Dados inválidos (e.g., CPF com formato incorreto).
    * `409 Conflict`: Documento já cadastrado.

#### `GET /api/listar/clientes`
* **Descrição**: Lista todos os clientes cadastrados no sistema.
* **Respostas**:
    * `200 OK`: Retorna uma lista de `ClienteResponseDTO`.

### Transações

#### `POST /api/transacoes/realizar`
* **Descrição**: Realiza uma transferência monetária entre duas contas.
* **Requisição**: `TransacaoRequestDTO` com os seguintes campos:
    * `idContaOrigem` (Long, obrigatório)
    * `agenciaOrigem` (String, obrigatório)
    * `idContaDestino` (Long, obrigatório)
    * `agenciaDestino` (String, obrigatório)
    * `valor` (BigDecimal, positivo, obrigatório)
* **Respostas**:
    * `200 OK`: Transação realizada com sucesso.
    * `400 Bad Request`: Requisição inválida (e.g., agência não confere).
    * `404 Not Found`: Conta de origem ou destino não encontrada.
    * `422 Unprocessable Entity`: Regra de negócio violada (e.g., saldo insuficiente, conta inativa).

---

## 📂 Estrutura do Projeto

A estrutura do projeto segue as convenções do Spring Boot, com pacotes bem definidos para cada responsabilidade:

```
src
├── main
│   ├── java/com/testest/apisimulacao
│   │   ├── ApisimulacaoApplication.java  # Ponto de entrada
│   │   ├── config/                     # Configurações (Swagger, Security)
│   │   ├── controller/                 # Controladores REST
│   │   ├── dto/                        # Objetos de Transferência de Dados
│   │   ├── entity/                     # Entidades de persistência
│   │   ├── event/                      # Eventos da aplicação
│   │   ├── exception/                  # Tratamento de exceções
│   │   ├── listener/                   # Listeners de eventos
│   │   ├── repository/                 # Repositórios (acesso a dados)
│   │   └── service/                    # Lógica de negócio
│   └── resources
│       ├── application.properties      # Configurações da aplicação
│       └── openapi-examples/           # Exemplos de payloads para Swagger
└── test
```

---

## ✨ Considerações Finais

Este projeto serve como um exemplo prático de uma API RESTful utilizando Spring Boot, com foco em:

* **Desenvolvimento Rápido**: Graças à simplicidade e poder do Spring Boot.
* **Validação de Dados**: Uso de `@Valid` e da biblioteca `Caelum Stella` para garantir a integridade dos dados.
* **Tratamento de Exceções**: Implementação de `@ControllerAdvice` para um tratamento de erros global e consistente.
* **Documentação Interativa**: Facilidade de uso e teste através do `Swagger UI` com exemplos claros.
