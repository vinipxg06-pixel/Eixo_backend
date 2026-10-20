# Eixo · Backend

API REST do **Eixo**, sistema de gestão para oficinas mecânicas desenvolvido como projeto de conclusão do curso **Entra21 – Java** e apresentado na **OktoberTech 2026**.

O backend concentra as regras de negócio, a persistência em MySQL e os endpoints consumidos pelo frontend.

> Frontend: [Eixo_frontend](https://github.com/vinipxg06-pixel/Eixo_frontend)

<p align="left">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="40" title="Java" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="40" title="Spring Boot" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg" width="40" title="MySQL" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/gradle/gradle-original.svg" width="40" title="Gradle" />
</p>

## Sumário
- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Equipe](#equipe)

## Funcionalidades
- **Oficinas e usuários:** cadastro de oficinas, usuários vinculados a cada oficina e login.
- **Clientes e veículos:** cadastro de clientes e veículos, com catálogo de marcas e modelos.
- **Orçamentos:** criação com peças do estoque, aprovação ou recusa.
- **Ordens de serviço:** abertura (manual ou a partir de um orçamento aprovado), inclusão de peças e fechamento.
- **Estoque:** cadastro de peças, entradas e saídas.
- **Fluxo de caixa:** registro de receitas e despesas por categoria.

## Tecnologias
| Categoria | Tecnologia |
| --- | --- |
| Linguagem | Java 21 |
| Framework | Spring Boot 4 (Spring Web MVC) |
| Persistência | Spring Data JPA / Hibernate |
| Banco de dados | MySQL |
| Migrations | Flyway |
| Validação | Bean Validation |
| Mapeamento DTO ↔ entidade | MapStruct |
| Outras bibliotecas | Lombok, Spring Boot Actuator, DevTools |
| Testes | JUnit 5, Mockito, MockMvc |
| Build | Gradle (Kotlin DSL) |

## Arquitetura
O projeto é organizado **por domínio**. Cada módulo tem as próprias camadas:

```
Controller  →  Service  →  Repository  →  MySQL
     ↕             ↕
    DTO         Mapper (MapStruct)
```

```
src/main/java/com/example/eixo/
├── oficina/          # Oficinas
├── usuarios/         # Usuários e login
├── cliente/          # Clientes
├── veiculo/          # Veículos
├── marcasmodelos/    # Marcas e modelos de veículos
├── orcamento/        # Orçamentos
├── ocpecas/          # Peças do orçamento
├── ordemservico/     # Ordens de serviço
├── ospecas/          # Peças da ordem de serviço
├── pecasestoque/     # Estoque de peças
├── fluxocaixa/       # Movimentações do caixa
└── excecao/          # Exceções personalizadas e handler global
```

Dentro de cada módulo:

```
modulo/
├── api/          # Controller e DTOs de request/response
├── service/      # Regras de negócio
├── repository/   # Interfaces Spring Data JPA
├── model/        # Entidades e enums
└── mapper/       # Conversão entidade ↔ DTO
```

## Equipe
- [Bárbara Fabiana de Souza](https://github.com/Barbara-fs)
- [Clara Bianca Kistner](https://github.com/clarakistner)
- [Luiz Filipe Reis](https://github.com/ldzinl)
- [Luiz Henrique Testoni](https://github.com/luiztestonidev)
- [Vinicius dos Santos Soares](https://github.com/vinipxg06-pixel)
