# Backend Gerador de Senhas

API RESTful desenvolvida com Spring Boot para o aplicativo Gerador de Senhas.

## 📋 Índice

- [Requisitos](#-requisitos)
- [Tecnologias](#-tecnologias)
- [Endpoints da API](#-endpoints-da-api)
- [Instalação e Configuração](#-instalação-e-configuração)
- [Banco de Dados](#-banco-de-dados)
- [Executando o Projeto](#-executando-o-projeto)
- [Solução de Problemas](#-solução-de-problemas)

## 🚀 Requisitos

- Java 21 ou superior
- Maven 3.6 ou superior
- MySQL 8.0 ou superior
- Node.js 18.x ou superior (para o frontend)
- npm 9.x ou superior (para o frontend)

## 🛠️ Tecnologias

### Core
- Spring Boot 3.4.5
- Spring Security
- Spring Data JPA
- Spring Validation
- Spring Boot DevTools

### Banco de Dados
- MySQL Connector J
- JPA/Hibernate
- H2 Database (para testes)

### Segurança
- JSON Web Token (JWT) 0.11.5

### Utilitários
- Lombok
- Maven

## 🔗 Endpoints da API

### Autenticação

#### POST /signin
- **Payload**: 
  ```json
  {
    "email": "string",
    "senha": "string"
  }
  ```
- **Response**: 
  ```json
  {
    "token": "string"
  }
  ```
- **Validações**:
  - Credenciais devem ser válidas

#### POST /signup
- **Payload**:
  ```json
  {
    "email": "string",
    "nome": "string",
    "dataNascimento": "YYYY-MM-DD",
    "senha": "string",
    "confirmacaoSenha": "string"
  }
  ```
- **Validações**:
  - Email deve ser válido
  - Email não pode estar em uso
  - Senhas devem ser iguais
  - Senha é armazenada criptografada

### Gerenciamento de Itens

#### POST /item
- **Payload**:
  ```json
  {
    "nome": "string",
    "senha": "string"
  }
  ```
- **Validações**:
  - Nome do item não pode estar em uso

#### DELETE /item/:id
- **Parâmetro**: ID do item na URL

#### GET /items
- **Response**: Lista de itens do usuário

## 💻 Instalação e Configuração

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/atividade-desenv-mobile.git
   cd atividade-desenv-mobile/backend-gs
   ```

2. Instale as dependências:
   ```bash
   mvn clean install
   ```

## 🗄️ Banco de Dados

### Instalação do MySQL

#### Windows
1. Baixe o instalador em https://dev.mysql.com/downloads/installer/
2. Execute o instalador e siga as instruções
3. Inicie o serviço: `net start mysql`

#### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql
```

#### macOS
```bash
brew install mysql
brew services start mysql
```

### Configuração do Banco

1. Acesse o MySQL:
   ```bash
   mysql -u root -p
   ```

2. Configure o usuário:
   ```sql
   CREATE USER 'root'@'localhost' IDENTIFIED BY 'root';
   GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost';
   FLUSH PRIVILEGES;
   ```

> O banco `passworddb` será criado automaticamente ao iniciar a aplicação.

## ▶️ Executando o Projeto

### Usando Maven Wrapper
```bash
./mvnw spring-boot:run
```

### Usando Maven
```bash
mvn spring-boot:run
```

O servidor estará disponível em: http://localhost:8080

## ❗ Solução de Problemas

### Erro de Conexão MySQL
```
Communications link failure
```
**Solução**: 
- Verifique se o MySQL está rodando: `service mysql status`
- Teste a conexão: `mysql -u root -p`

### Erro de Autenticação MySQL
```
Access denied for user 'root'@'localhost'
```
**Solução**:
```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';
FLUSH PRIVILEGES;
```

### Erro de CORS
```
Access to XMLHttpRequest has been blocked by CORS policy
```
**Solução**: 
- Verifique as configurações CORS em `WebConfig.java`
- Configure o frontend para usar `withCredentials: false`

## 📝 Licença

Este projeto está sob a licença BSD Zero Clause.

## 👥 Autor

- **Pietro** - [GitHub](https://github.com/seu-usuario) 