# Backend Gerador de Senhas

API RESTful para o aplicativo de Gerador de Senhas, desenvolvido com Spring Boot.

## Requisitos

- Java 17 ou superior (Testado com Java 24)
- Maven 3.6 ou superior

## Tecnologias utilizadas

- Spring Boot 3.4.5
- Spring Security
- Spring Data JPA
- JWT para autenticação
- Banco de dados H2 (em memória para desenvolvimento)

## Como executar o projeto

### Clonando o repositório

```bash
git clone https://github.com/seu-usuario/gerador-de-senhas.git
cd gerador-de-senhas/backend-gs
```

### Configurando o banco de dados

Por padrão, o aplicativo usa um banco de dados H2 em arquivo. A configuração está no arquivo `application.properties`:

```properties
spring.datasource.url=jdbc:h2:file:./data/passworddb
spring.datasource.username=sa
spring.datasource.password=password
```

Se quiser usar outro banco de dados (como MySQL), ajuste as configurações no arquivo `application.properties`.

### Executando a aplicação

```bash
# Compilar e executar com Maven
mvn spring-boot:run
```

Ou, se preferir gerar o JAR:

```bash
mvn clean package
java -jar target/backend-gs-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: http://localhost:8080

### Console H2 (para desenvolvimento)

O console do H2 está disponível em: http://localhost:8080/h2-console

Com as seguintes configurações:
- JDBC URL: jdbc:h2:file:./data/passworddb
- Usuário: sa
- Senha: password

## Endpoints da API

### Autenticação

- **POST /api/auth/signup**: Cadastro de usuários
  ```json
  {
    "name": "Nome Completo",
    "email": "email@exemplo.com",
    "password": "senha123",
    "confirmPassword": "senha123",
    "birthDate": "2000-01-01"
  }
  ```

- **POST /api/auth/signin**: Login
  ```json
  {
    "email": "email@exemplo.com",
    "password": "senha123"
  }
  ```
  Retorno: 
  ```json
  {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "id": 1,
    "name": "Nome Completo",
    "email": "email@exemplo.com"
  }
  ```

### Itens de Senha (necessita autenticação)

- **GET /api/items**: Listar todos os items de senha do usuário

- **POST /api/items**: Adicionar um novo item
  ```json
  {
    "name": "Facebook",
    "password": "senhaSegura123"
  }
  ```

- **DELETE /api/items/{id}**: Deletar um item de senha

## Frontend

O frontend do aplicativo está disponível em: [gerador-de-senhas-pietro](../gerador-de-senhas-pietro) 