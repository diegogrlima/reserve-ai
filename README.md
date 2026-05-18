# Reserve-ai

API REST para gestão de reservas de quartos desenvolvida com Java, Spring Boot, JPA, Flyway e MapStruct.

## Objetivo

Centralizar o cadastro e a manutenção de usuários, quartos e reservas em uma API com regras de negócio claras, validações no backend e persistência versionada.

## Problema Que Resolve

Em sistemas de hospedagem, é comum existir:

- cadastro duplicado de usuários
- cadastro duplicado de quartos
- reservas conflitantes
- regras espalhadas pela aplicação
- evolução desorganizada do schema do banco

Esta API resolve isso com:

- validação centralizada
- serviços dedicados por caso de uso
- tratamento global de erros
- migrations com Flyway
- estrutura organizada por camadas

## Funcionalidades

### Usuários

- cadastro de usuário
- listagem paginada de usuários
- e-mail único

### Quartos

- cadastro de quarto
- listagem paginada de quartos
- busca por ID
- atualização por ID
- remoção por ID
- número do quarto único
- bloqueio de remoção quando o quarto já está reservado

### Reservas

- cadastro de reserva
- listagem paginada de reservas
- atualização por ID
- cancelamento por ID
- bloqueio de reserva duplicada para o mesmo usuário
- bloqueio de reserva duplicada para o mesmo quarto
- status `CONFIRMED` e `CANCELED`

### Infraestrutura

- DTOs de entrada e saída
- MapStruct para mapeamento entre camadas
- tratamento global de exceções
- paginação com Spring Data
- versionamento do banco com Flyway

## Arquitetura

O projeto segue uma organização em camadas:

```text
Controller -> Service -> Repository -> Database
```

Essa separação mantém as responsabilidades claras e facilita evolução, manutenção e testes.

## Tecnologias

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- Spring Security
- Flyway
- MySQL
- MapStruct
- Lombok
- Maven

## Endpoints

### Users

```http
POST /users
GET /users?page=0&size=10&sort=name,asc
```

Payload de criação:

```json
{
  "name": "Diego Lima",
  "email": "diego@email.com"
}
```

### Rooms

```http
POST /rooms
GET /rooms?page=0&size=10&sort=roomNumber,asc
GET /rooms/{id}
PUT /rooms/{id}
DELETE /rooms/{id}
```

Payload de criação:

```json
{
  "roomNumber": "101",
  "roomType": "STANDARD",
  "dailyRate": 199.90
}
```

Payload de atualização:

```json
{
  "roomNumber": "102",
  "roomType": "DELUXE",
  "dailyRate": 250.00
}
```

### Bookings

```http
POST /bookings
GET /bookings?page=0&size=10
PUT /bookings/{id}
PATCH /bookings/{id}/cancel
```

Payload de criação:

```json
{
  "userId": 1,
  "roomId": 10
}
```

Payload de atualização:

```json
{
  "userId": 2,
  "roomId": 11
}
```

## Regras De Negócio

- o e-mail do usuário deve ser único
- o número do quarto deve ser único
- um usuário não pode possuir mais de uma reserva
- um quarto não pode possuir mais de uma reserva
- não é permitido remover um quarto já reservado
- cancelar uma reserva altera o status para `CANCELED`

## Tratamento De Erros

A API usa tratamento global de exceções com `@RestControllerAdvice`.

Formato padrão de erro:

```json
{
  "timestamp": "2026-05-17T12:00:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Room already has a booking: 10",
  "path": "/rooms/10"
}
```

## Como Executar

### Pré-requisitos

- Java 21
- Maven 3.9+
- MySQL 8+

### Configuração do banco

Variáveis de ambiente suportadas:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Se não forem informadas, a aplicação usa os valores definidos em `application.yaml`.

### Execução

```bash
mvn clean compile
mvn spring-boot:run
```

## Desafio Ao Desenvolver

Os principais pontos de atenção neste projeto foram:

- manter as regras de negócio coerentes entre create, update e delete
- evitar duplicação de reservas sem depender só do banco
- organizar controllers, services, DTOs e exceptions de forma previsível
- evoluir o schema com migrations sem quebrar o restante da aplicação

## O Que Foi Aprendido

- modelagem de domínio com entidades e relacionamentos JPA
- uso de DTOs para separar contrato de API e modelo interno
- MapStruct para reduzir código repetitivo de conversão
- validação com Jakarta Validation
- tratamento centralizado de exceções
- paginação com Spring Data
- evolução de banco com Flyway
- organização por camadas para facilitar manutenção

## Autor

Diego Lima
