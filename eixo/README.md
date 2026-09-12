# Eixo - Backend

API/backend do **Eixo**, sistema de gerenciamento para oficinas mecânicas, desenvolvido como projeto de conclusão do curso de Java do programa Entra21. Este repositório contém a lógica de negócio, persistência de dados e endpoints REST consumidos pelo frontend da aplicação.

> Repositório do frontend: [Eixo Frontend](https://github.com/vinipxg06-pixel/Eixo_frontend)

## Conteúdo
- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Como Rodar o Projeto](#como-rodar-o-projeto)
- [Variáveis de Ambiente](#variáveis-de-ambiente)
- [Estrutura de Pastas](#estrutura-de-pastas)
- [Migrations e Banco de Dados](#migrations-e-banco-de-dados)
- [Versão](#versão)
- [Desenvolvedores](#desenvolvedores)

## Sobre o Projeto
O Eixo centraliza informações e auxilia na organização da rotina de oficinas mecânicas, oferecendo funcionalidades para o gerenciamento de ordens de serviço, orçamentos e estoque. Este repositório é responsável por toda a camada de backend: regras de negócio, persistência em banco de dados e disponibilização dos dados via API REST.

## Tecnologias Utilizadas
<p align="left">
  <img 
    src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" 
    width="40"
    title="Java"
  />
  <img 
    src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" 
    width="40"
    title="Spring Boot"
  />
  <img 
    src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg" 
    width="40"
    title="MySQL"
  />
  <img 
    src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/gradle/gradle-original.svg" 
    width="40"
    title="Gradle"
  />
</p>

- **Linguagem:** Java 21
- **Framework:** Spring Boot 4.1.1
- **Gerenciador de Dependências:** Gradle (Kotlin DSL)
- **Persistência:** Spring Data JPA / Hibernate
- **Migrations:** Flyway (`flyway-mysql`)
- **Banco de Dados:** MySQL
- **Autenticação:** Spring Security
- **Monitoramento:** Spring Boot Actuator
- **Validação:** Spring Boot Starter Validation
- **Outras bibliotecas:** Lombok, Spring Boot DevTools, Spring Boot Docker Compose
- **Testes:** JUnit 5 (JUnit Platform)

## Arquitetura
O backend segue uma arquitetura em camadas:

```
Controller  →  Service  →  Repository  →  Banco de Dados
```

- **Controller:** exposição dos endpoints REST
- **Service:** regras de negócio
- **Repository:** acesso e persistência de dados
- **DTO:** objetos de transferência de dados entre as camadas
- **Model/Entity:** representação das tabelas do banco de dados

## Pré-requisitos
Antes de começar, você precisa ter instalado:
- [Java JDK 21](https://adoptium.net/pt-BR/temurin/releases?version=21)
- [MySQL](https://dev.mysql.com/downloads/installer/) (ou Docker, já que o projeto suporta Spring Boot Docker Compose)
- [Git](https://git-scm.com/install/windows)
- (Opcional) [Bruno](https://www.usebruno.com/downloads) para testar os endpoints

> O projeto usa o **Gradle Wrapper** (`gradlew`/`gradlew.bat`), então não é necessário instalar o Gradle manualmente.

## Como Rodar o Projeto

```bash
# Clone o repositório
git clone https://github.com/vinipxg06-pixel/Eixo_backend.git

# Acesse a pasta do projeto
cd Eixo_backend/eixo

# Configure as variáveis de ambiente (veja a seção abaixo)

# Instale as dependências e rode a aplicação
./gradlew clean build
./gradlew bootRun
```

> No Windows, use `gradlew.bat` no lugar de `./gradlew`.

A API estará disponível em: `http://localhost:<porta>`

### Subindo o banco de dados com Docker (opcional)
O projeto inclui suporte a `spring-boot-docker-compose`, que sobe automaticamente os containers definidos no `compose.yaml` ao rodar a aplicação:

```bash
./gradlew bootRun
```

O Spring Boot detecta o `compose.yaml` e sobe o container do MySQL automaticamente.

## Variáveis de Ambiente
Configure o arquivo `application.properties` ou `application.yml` com as informações do seu ambiente:

```properties
# Banco de Dados
spring.datasource.url=jdbc:mysql://localhost:3306/eixo_db
spring.datasource.username=<seu_usuario>
spring.datasource.password=<sua_senha>

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Porta da aplicação
server.port=8080
```

> ⚠️ Nunca versione credenciais reais. Utilize um arquivo `.env` ou variáveis de ambiente do sistema em produção.

## Estrutura de Pastas

```
src/main/java/com/example/eixo/
├── controller/     # Endpoints REST
├── service/        # Regras de negócio
├── repository/     # Acesso a dados (JPA)
├── model/          # Entidades do banco de dados
├── dto/            # Objetos de transferência de dados
├── config/         # Configurações (segurança, CORS, etc.)
└── exception/      # Tratamento de exceções

src/main/resources/
└── db/migration/   # Scripts de migration do Flyway
```

Arquivos de configuração na raiz: `build.gradle.kts`, `settings.gradle.kts`, `compose.yaml` (containers do banco de dados).

## Migrations e Banco de Dados
O projeto utiliza **Flyway** para versionamento e controle das migrations do banco de dados MySQL. Os scripts de migration ficam em:

```
src/main/resources/db/migration/
```

Seguindo o padrão de nomenclatura do Flyway: `V1__descricao.sql`, `V2__descricao.sql`, etc.

As migrations são executadas automaticamente ao iniciar a aplicação, aplicando apenas as versões ainda não rodadas no banco.

## Versão
Consulte o histórico de versões em: [Versões do Eixo](https://github.com/vinipxg06-pixel/Eixo_backend/commits/main/)

## Desenvolvedores
- [Bárbara Fabiana de Souza](https://github.com/Barbara-fs)
- [Clara Bianca Kistner](https://github.com/clarakistner)
- [Luiz Filipe Reis](https://github.com/ldzinl)
- [Luiz Henrique Testoni](https://github.com/luiztestonidev)
- [Vinicius dos Santos Soares](https://github.com/vinipxg06-pixel)