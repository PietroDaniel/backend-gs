# Backend Gerador de Senhas

API RESTful para o aplicativo de Gerador de Senhas, desenvolvido com Spring Boot.

## Requisitos

- Java 21 ou superior
- Maven 3.6 ou superior
- MySQL 8.0 ou superior
- Node.js 18.x ou superior (apenas para o frontend)
- npm 9.x ou superior (apenas para o frontend)

## Tecnologias e Dependências

### Core
- Spring Boot 3.4.5
- Spring Security
- Spring Data JPA
- Spring Validation
- Spring Boot DevTools

### Banco de Dados
- MySQL Connector J
- JPA/Hibernate
- Hikari Connection Pool

### Segurança
- JSON Web Token (JWT) 0.11.5
  - jjwt-api
  - jjwt-impl
  - jjwt-jackson

### Utilitários
- Lombok para redução de boilerplate code
- H2 Database (disponível para testes)

## Como executar o projeto

### Clonando o repositório

```bash
git clone https://github.com/seu-usuario/atividade-desenv-mobile.git
cd atividade-desenv-mobile
```

### Configurando o banco de dados MySQL

1. Instale o MySQL Server em sua máquina, caso ainda não tenha:
   - **Windows**: Download do instalador em https://dev.mysql.com/downloads/installer/
   - **Linux (Ubuntu/Debian)**: `sudo apt install mysql-server`
   - **macOS**: `brew install mysql`

2. Inicie o serviço MySQL:
   - **Windows**: Através do MySQL Workbench ou do serviço no Windows
     ```bash
     net start mysql
     ```
   - **Linux**: 
     ```bash
     sudo systemctl start mysql
     sudo systemctl enable mysql # para iniciar automaticamente
     ```
   - **macOS**: 
     ```bash
     brew services start mysql
     ```

3. Crie um usuário e configure a senha:
   ```sql
   CREATE USER 'root'@'localhost' IDENTIFIED BY 'root';
   GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost';
   FLUSH PRIVILEGES;
   ```

4. O banco de dados `passworddb` será criado automaticamente ao iniciar a aplicação graças à configuração `createDatabaseIfNotExist=true` no application.properties.

### Configuração do Backend

As configurações do backend estão definidas nos seguintes arquivos:

#### application.properties
```properties
# Configurações do servidor
server.port=8080
server.servlet.context-path=/

# Configurações do banco de dados MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/passworddb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update

# Configurações adicionais do MySQL
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.datasource.hikari.maximum-pool-size=10

# Configurações do Hibernate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Configurações de logging
logging.level.org.springframework.security=DEBUG
logging.level.com.pietro.backendgs=DEBUG
```

#### jwt.properties
```properties
# Configurações JWT personalizadas
app.jwt.secret=minhaSenhaSecretaBemGrandeParaSerUsadaNoAppDeGeradorDeSenhas2024
app.jwt.expiration=86400000  # 24 horas em milissegundos
```

> **Importante**: Em ambiente de produção, altere as credenciais do banco de dados e a chave secreta do JWT.

### Configuração de CORS (Cross-Origin Resource Sharing)

O backend está configurado para aceitar requisições de diferentes origens, especialmente importante para o desenvolvimento web e mobile. As configurações estão em:

#### WebConfig.java
```java
registry.addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        .allowedHeaders("*")
        .exposedHeaders("*")
        .allowCredentials(false)
        .maxAge(3600);
```

#### SecurityConfig.java - Configuração para origens específicas (alternativa)
```java
configuration.setAllowedOrigins(List.of(
    "http://localhost:3000", 
    "http://localhost:8081", 
    "http://127.0.0.1:8081",
    "http://localhost:19006" // Expo Web
));
```

### Executando o Backend

#### Usando Maven Wrapper (recomendado)
```bash
# Navegar para a pasta do backend
cd backend-gs

# Compilação completa com testes
./mvnw clean install

# Executar a aplicação
./mvnw spring-boot:run
```

#### Usando Maven diretamente
```bash
# Navegar para a pasta do backend
cd backend-gs

# Compilar sem executar testes
mvn clean install -DskipTests

# Compilar com testes
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

#### Gerando e executando o JAR
```bash
# Gerar o JAR
mvn clean package

# Executar o JAR
java -jar target/backend-gs-0.0.1-SNAPSHOT.jar

# Opcionalmente, especificar perfis ou sobrescrever propriedades:
java -jar target/backend-gs-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod --server.port=9000
```

O backend estará disponível em: http://localhost:8080

## Estrutura do Projeto

```
backend-gs/
├── src/main/java/com/pietro/backendgs/
│   ├── auth/                 # Configurações de autenticação
│   ├── config/               # Configurações da aplicação
│   ├── controller/           # Controladores REST
│   ├── exception/            # Manipulação de exceções
│   ├── model/                # Entidades e DTOs
│   ├── repository/           # Interfaces de acesso aos dados
│   ├── security/             # Implementação de segurança JWT
│   ├── service/              # Camada de regra de negócios
│   └── BackendGsApplication.java  # Classe principal
├── src/main/resources/
│   ├── application.properties # Configurações principais
│   └── jwt.properties        # Configurações JWT
```

## Solucionando problemas comuns

### Problemas com o MySQL

1. **Erro de conexão com o MySQL**:
   ```
   Communications link failure
   ```
   Soluções:
   - Verifique se o serviço MySQL está rodando: `service mysql status`
   - Teste a conexão com o comando: `mysql -u root -p`
   - Verifique o firewall: `sudo ufw status` e libere a porta se necessário: `sudo ufw allow 3306`

2. **Erro de autenticação MySQL**:
   ```
   Access denied for user 'root'@'localhost'
   ```
   Soluções:
   - Redefina a senha do usuário root:
     ```sql
     ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';
     FLUSH PRIVILEGES;
     ```
   - Verifique se as credenciais em `application.properties` estão corretas

3. **Erro de timezone MySQL**:
   ```
   The server time zone value is unrecognized
   ```
   Solução:
   - Adicione `serverTimezone=UTC` à URL de conexão (já configurado no application.properties)

### Problemas de CORS

1. **Erros de CORS no frontend**:
   ```
   Access to XMLHttpRequest has been blocked by CORS policy
   ```
   Soluções:
   - Verifique se a configuração de CORS está correta em `WebConfig.java`
   - Para desenvolvimento local, use origens explícitas em vez de "*" se precisar de credentials:
     ```java
     .allowedOrigins("http://localhost:19006", "http://localhost:3000")
     .allowCredentials(true)
     ```
   - No frontend, defina `withCredentials: false` nas configurações do Axios

2. **Erro de credenciais e wildcard CORS**:
   ```
   The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'
   ```
   Solução:
   - Não é possível usar `allowedOrigins("*")` com `allowCredentials(true)`. Escolha origens específicas se precisar de credenciais.

### Problemas de JWT

1. **Token expirado ou inválido**:
   ```
   JWT signature does not match locally computed signature
   ```
   Soluções:
   - Verifique se a configuração de JWT em `jwt.properties` está correta
   - A propriedade `app.jwt.expiration` é em milissegundos (86400000 = 24 horas)
   - O frontend deve enviar o token no formato: `Authorization: Bearer {token}`

2. **Erro de assinatura JWT**:
   ```
   JWT verification failed
   ```
   Solução:
   - Garanta que a mesma chave secreta está sendo usada para assinar e verificar tokens:
     ```properties
     app.jwt.secret=minhaSenhaSecretaBemGrandeParaSerUsadaNoAppDeGeradorDeSenhas2024
     ```

## Comandos úteis para desenvolvimento

```bash
# Verificar logs da aplicação em tempo real
mvn spring-boot:run | grep -i error

# Executar com perfil específico
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Gerar JAR e executar em modo debug
mvn clean package
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar target/backend-gs-0.0.1-SNAPSHOT.jar

# Executar somente testes específicos
mvn test -Dtest=AuthServiceTest

# Limpar cache Maven (útil para resolver problemas de dependências)
mvn dependency:purge-local-repository
```

## Endpoints da API

### Autenticação

- **POST /signin**: Login
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

- **POST /signup**: Cadastro de usuários
  ```json
  {
    "name": "Nome Completo",
    "email": "email@exemplo.com",
    "password": "senha123",
    "confirmPassword": "senha123",
    "birthDate": "2000-01-01"
  }
  ```

### Itens de Senha (necessita autenticação)

- **GET /items**: Listar todos os items de senha do usuário

- **POST /item**: Adicionar um novo item
  ```json
  {
    "name": "Facebook",
    "password": "senhaSegura123"
  }
  ```

- **GET /item/{id}**: Buscar um item de senha por ID

- **DELETE /item/{id}**: Deletar um item de senha 