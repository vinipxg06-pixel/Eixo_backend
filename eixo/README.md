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
- [Regras de Negócio](#regras-de-negócio)
- [Rotas da API](#rotas-da-api)
- [Formato dos Erros](#formato-dos-erros)
- [Ordenação das listagens](#ordenação-das-listagens)
- [Pendências Conhecidas](#pendências-conhecidas)
- [Versão](#versão)
- [Desenvolvedores](#desenvolvedores)

## Sobre o Projeto
O Eixo centraliza informações e auxilia na organização da rotina de oficinas mecânicas, oferecendo funcionalidades para o gerenciamento de clientes, veículos, orçamentos, ordens de serviço, estoque e fluxo de caixa. Este repositório é responsável por toda a camada de backend: regras de negócio, persistência em banco de dados e disponibilização dos dados via API REST.

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
- **Framework:** Spring Boot 4.1.1 (Web MVC)
- **Gerenciador de Dependências:** Gradle (Kotlin DSL)
- **Persistência:** Spring Data JPA / Hibernate (`ddl-auto=validate`)
- **Migrations:** Flyway (`flyway-mysql`)
- **Banco de Dados:** MySQL
- **Senhas:** BCrypt (`spring-security-crypto` — sem o Spring Security completo)
- **Conversão entidade ↔ DTO:** MapStruct
- **Validação:** Spring Boot Starter Validation (Bean Validation)
- **Monitoramento:** Spring Boot Actuator
- **Outras bibliotecas:** Lombok, Spring Boot DevTools
- **Testes:** JUnit 5, Mockito, AssertJ, MockMvc — testes de unidade das regras de negócio em `src/test`

## Arquitetura
O backend é organizado **por módulo** (cliente, veículo, orçamento...), e cada módulo segue as mesmas camadas:

```
Controller  →  Service  →  Repository  →  Banco de Dados
     ↑            ↓
    DTO  ←——  Mapper (MapStruct)  ←——  Entidade
```

- **api (Controller):** exposição dos endpoints REST; não contém regra de negócio.
- **api/dto:** records de entrada (`...Request`) e saída (`...Response`) com Bean Validation.
- **service:** regras de negócio e transações.
- **model:** entidades JPA. Regras de estado ficam na própria entidade (ex.: `Orcamento.garantirEditavel()`, `PecaEstoque.inativar()`, `MovimentacaoFluxoCaixa.cancelar()`).
- **mapper:** interfaces MapStruct (`paraResposta`, `paraEntidade`), configuradas em `config/MapStructConfig`.
- **repository:** Spring Data JPA.

Convenções de nomes: tudo em português; nos services, `buscarEntidade(id, oficinaId)` devolve a entidade (ou lança 404) e `listar`, `buscar`, `criar`, `atualizar`, `excluir` devolvem DTOs. Operações de negócio têm nome próprio (`aprovar`, `fecharOrdemServico`, `cancelar`, `cancelarMovimentacao`, `inativar`...).

## Pré-requisitos
Antes de começar, você precisa ter instalado:
- [Java JDK 21](https://adoptium.net/pt-BR/temurin/releases?version=21)
- [MySQL](https://dev.mysql.com/downloads/installer/) **ou** [Docker](https://www.docker.com/) (veja "Banco com Docker")
- [Git](https://git-scm.com/install/windows)
- (Opcional) [Bruno](https://www.usebruno.com/downloads) para testar os endpoints

> O projeto usa o **Gradle Wrapper** (`gradlew`/`gradlew.bat`), então não é necessário instalar o Gradle manualmente.

## Como Rodar o Projeto

```bash
# Clone o repositório
git clone https://github.com/vinipxg06-pixel/Eixo_backend.git

# Acesse a pasta do projeto
cd Eixo_backend/eixo

# Configure o .env (veja "Variáveis de Ambiente")
cp .env.example .env

# Rode os testes e suba a aplicação
./gradlew clean test
./gradlew bootRun
```

> No Windows (PowerShell), use `.\gradlew.bat` no lugar de `./gradlew`.

A API estará disponível em `http://localhost:8082` (ou na porta definida em `SERVER_PORT`). Na primeira execução o Flyway cria as tabelas e carrega marcas e modelos.

### Banco com Docker (opcional)
Se não tiver MySQL instalado, o `compose.yaml` sobe um MySQL 8.4 com o banco `eixo`, usando a senha do seu `.env` (`DB_PASSWORD`, usuário `root`):

```bash
docker compose up -d     # sobe o banco
docker compose down      # para o banco (os dados ficam no volume eixo-mysql)
```

> Se já existir um MySQL rodando na porta 3306, pare-o antes ou mude a porta no `compose.yaml` e em `DB_URL`.

## Variáveis de Ambiente
As credenciais do banco **não ficam no código**. O `application.properties` lê os valores de variáveis de ambiente, que podem ser definidas em um arquivo `.env` na pasta `eixo/` (lido automaticamente pelo Spring Boot via `spring.config.import`).

1. Copie o arquivo de exemplo:
   ```bash
   cp .env.example .env        # Windows: copy .env.example .env
   ```
2. Preencha o `.env` com os dados do seu MySQL:

| Variável | Obrigatória | Padrão | Descrição |
| --- | --- | --- | --- |
| `DB_URL` | Não | `jdbc:mysql://localhost:3306/eixo` | URL JDBC do banco |
| `DB_USER` | Não | `root` | Usuário do banco |
| `DB_PASSWORD` | **Sim** | — | Senha do banco |
| `SERVER_PORT` | Não | `8082` | Porta da API |
| `CORS_ORIGENS` | Não | `http://localhost:5500,http://127.0.0.1:5500` | Endereços do frontend autorizados a chamar a API (separados por vírgula) |

Também é possível definir essas variáveis direto no sistema operacional ou na configuração de execução da IDE; elas têm prioridade sobre o `.env`.

> ⚠️ O `.env` está no `.gitignore`. Nunca versione credenciais reais.

## Estrutura de Pastas

```
src/main/java/com/example/eixo/
├── cliente/            # cada módulo tem: api/ (controller), api/dto/, service/, model/, mapper/, repository/
├── veiculo/
├── marcasmodelos/      # marcas e modelos de veículos (somente leitura)
├── oficina/
├── usuarios/           # usuários e login
├── pecasestoque/       # estoque de peças
├── orcamento/          # orçamentos (+ OrcamentoComPecasService)
├── ocpecas/            # peças do orçamento
├── ordemservico/       # ordens de serviço (+ OrdemServicoComPecasService)
├── ospecas/            # peças da OS
├── fluxocaixa/         # lançamentos do caixa
├── compartilhado/      # DTOs usados por mais de um módulo (LinhaPecaRequest)
├── config/             # BCrypt, CORS, MapStruct
└── excecao/            # exceções de negócio e ManipuladorExcecoes (formato único de erro)

src/main/resources/
├── application.properties
└── db/migration/       # migrations do Flyway
```

Arquivos na raiz: `build.gradle.kts`, `settings.gradle.kts`, `compose.yaml` (MySQL opcional), `.env.example`.

## Migrations e Banco de Dados
O projeto utiliza **Flyway** para versionar o banco MySQL. As migrations rodam automaticamente ao iniciar a aplicação e ficam em `src/main/resources/db/migration/`:

| Versão | Arquivo | Conteúdo |
| --- | --- | --- |
| V1 | `V1__estrutura_inicial.sql` | Todas as tabelas, chaves e restrições |
| V2 | `V2__carga_marcas_modelos.sql` | Carga de marcas e modelos de veículos (idempotente) |
| V3 | `V3__unicidade_por_oficina.sql` | Placa, CPF/CNPJ e código de barras passam a ser únicos **por oficina** |
| V4 | `V4__cancelamento_movimentacao_caixa.sql` | Cancelamento lógico de lançamentos do caixa (`cancelada`, `cancelada_em`) |
| V5 | `V5__inativacao_pecas_estoque.sql` | Inativação de peças (`ativa`) — peças com histórico não são excluídas |
| V6 | `V6__cancelamento_ordem_servico.sql` | Status `Cancelada` na OS, com motivo e data do cancelamento |

**Banco novo:** basta criar o schema vazio (`CREATE DATABASE eixo;`) e subir a aplicação — o Flyway aplica V1 e V2.

**Banco já existente (criado antes do Flyway):** com `baseline-on-migrate`, o Flyway registra o banco como versão 1, pula a V1 e aplica a partir da V2. A V2 não duplica marcas/modelos já cadastrados.

Regras para novas migrations:
- Nunca altere uma migration já aplicada; crie uma nova (`V3__descricao.sql`, `V4__...`).
- Não use `USE` nem o nome do schema nos scripts — o banco é definido por `DB_URL`.
- O Hibernate roda com `ddl-auto=validate`: as entidades JPA precisam bater com as tabelas criadas pelas migrations.

## Regras de Negócio

Cada regra é validada no **backend** (a fonte da verdade); o frontend só repete algumas para dar retorno mais rápido ao usuário.

### Oficinas e acesso
- Todos os dados (clientes, veículos, estoque, orçamentos, OS, caixa, usuários) pertencem a uma oficina; as rotas `/oficinas/{oficinaId}/...` só enxergam os registros daquela oficina.
- Senhas são armazenadas com **hash BCrypt**, nunca em texto puro.
- **Senha forte** (cadastro e alteração de usuário): no mínimo 8 caracteres, com ao menos 1 letra maiúscula, 1 número e 1 caractere especial. Senha fora da regra responde 400 com o erro no campo `senha`.
- O login devolve apenas o `oficinaId`. **Pendência conhecida:** ainda não há token de autenticação — o `oficinaId` vem do navegador. A autenticação com JWT (e perfis de acesso) fica para a próxima etapa do projeto.
- O e-mail do usuário é único no sistema inteiro (é por ele que o login descobre a oficina).

### Unicidade (por oficina)
- **Placa**, **CPF/CNPJ** e **código de barras** não se repetem dentro da mesma oficina, mas podem existir em oficinas diferentes.
- CPF/CNPJ e código de barras são opcionais; em branco são gravados como vazio (`NULL`) e não contam como repetidos.

### Clientes e veículos
- Cliente **inativo** não pode receber veículos, orçamentos nem ordens de serviço.
- O veículo do orçamento/OS precisa pertencer ao cliente informado.
- **Placa:** gravada em maiúsculas, sem hífen nem espaços; aceita o padrão antigo (`ABC1234`) e o Mercosul (`ABC1D23`).
- **Formato dos documentos:** CPF/CNPJ só com números, 11 dígitos (CPF) ou 14 (CNPJ); CNPJ da oficina com 14 dígitos; telefone com DDD, 10 ou 11 dígitos. Textos respeitam o tamanho da coluna (nomes, e-mails, código de barras: até 100 caracteres; senha: até 72, limite do BCrypt). Fora disso a API responde 400 indicando o campo.
- **Ano do veículo:** de 1900 até o ano atual + 1 (ano-modelo).
- **Placa e proprietário não mudam na edição do veículo** (a edição altera modelo, cor, combustível, ano e quilometragem).
- **Exclusão:** cliente sem veículos, orçamentos ou OS pode ser excluído; com histórico, deve ser **inativado**. Veículo com orçamentos ou OS não pode ser excluído.

### Estoque
- **Preço unitário é o preço médio**, recalculado a cada entrada: `(estoque atual × preço atual + entrada × preço da entrada) ÷ nova quantidade`.
- A **quantidade** só muda por entrada, saída, orçamento aprovado ou OS; a **edição** da peça altera apenas dados cadastrais (nome, categoria, código de barras, estoque mínimo, unidade).
- **Registro no caixa é opcional** (o usuário escolhe em cada operação):

| Operação | Padrão | Lançamento gerado |
| --- | --- | --- |
| Cadastro com quantidade inicial | marcado | Saída · Compra de peça (quantidade × preço) |
| Entrada de estoque | marcado | Saída · Compra de peça (quantidade × preço da entrada) |
| Retirada manual | desmarcado | Entrada · Venda de peça (quantidade × valor de venda informado) |

- **Exclusão:** peça nunca usada pode ser excluída; peça usada em orçamento, OS ou caixa só pode ser **inativada**. Peça inativa continua no histórico, mas não pode receber unidades novas em orçamentos/OS (pode ser reativada).

### Preço das peças em orçamentos e OS
- **Cada unidade vale o preço vigente da peça no momento em que foi adicionada.** Por isso a mesma peça pode ocupar mais de uma linha, uma por preço.
  Ex.: 1 pneu adicionado a R$ 10 + 1 pneu adicionado depois, quando custava R$ 12 → duas linhas, total **R$ 22**.
- Aumentar a quantidade de uma linha cujo preço gravado é diferente do atual manda as unidades novas para a linha do preço atual; reduzir afeta só a própria linha.
- **Salvamento atômico:** o orçamento/OS e suas peças são enviados numa única requisição e gravados numa transação. Se qualquer etapa falhar (validação, estoque insuficiente, peça inativa), nada é salvo — nem a movimentação de estoque.

### Orçamentos
- Ciclo: **Pendente → Aprovado** ou **Pendente → Recusado**. Só orçamentos pendentes podem ser editados ou excluídos.
- **Aprovar** exige valor maior que zero e estoque suficiente (somando todas as linhas da mesma peça). Se faltar peça, a oficina precisa registrar a entrada no estoque antes de aprovar. A aprovação cria a OS com os **preços do orçamento** (os que o cliente aprovou) e baixa o estoque.
- Orçamento **pendente não reserva** estoque: é uma proposta. Dois orçamentos podem contar com a mesma peça; quem aprovar primeiro leva.
- Orçamento não tem validade: os preços ficam os do momento em que cada peça entrou.

### Ordens de serviço
- Ciclo: **Aberta → Fechada** ou **Aberta → Cancelada**. Fechada e Cancelada são finais: a OS **nunca é reaberta**. Só OS abertas podem ser editadas.
- Adicionar peças baixa o estoque na hora; remover ou reduzir devolve ao estoque.
- **Fechamento com confirmação das peças:** ao fechar, o usuário confirma a quantidade realmente usada de cada peça. Só é possível **reduzir** (até zero); o que não foi usado volta ao estoque e deixa de ser cobrado. Quando a peça tem linhas com preços diferentes, as unidades devolvidas saem das **adicionadas por último**. Peça não informada no fechamento é considerada usada por inteiro.
- **OS sem valor** (ex.: retorno de garantia) pode ser fechada, mas exige confirmação explícita (`confirmarSemCusto: true`) e **não gera lançamento** de R$ 0 no caixa.
- Fechar a OS com valor gera automaticamente uma **entrada** no caixa (categoria Serviço), no valor total, na data do fechamento (fechar = pagamento recebido; não há pagamento pendente ou parcelado).
- **Cancelamento:** exige um motivo da lista padrão (cliente desistiu, valor não autorizado, peça indisponível, defeito não solucionado, aberta por engano, outro). Todas as peças voltam ao estoque e **nada é lançado** no caixa. As linhas de peças continuam na OS como histórico.
- OS vinda de orçamento cancelada: o orçamento **continua Aprovado** e não gera outra OS.
- **Valor aprovado:** a OS vinda de orçamento informa `valorAprovado`. A tela mostra esse valor ao lado do total e avisa quando são diferentes (sem bloquear).

### Fluxo de caixa
- Lançamentos **automáticos** (fechamento de OS, compra/venda de peça) não podem ser editados nem cancelados manualmente.
- Lançamentos manuais **não são apagados**: o "excluir" é um **cancelamento lógico** — o lançamento continua no histórico, marcado como cancelado, e deixa de contar nos totais e no dashboard.
- Lançamentos de valor zero não são gerados.

## Rotas da API

Todas as rotas abaixo de `/oficinas/{oficinaId}` só acessam dados daquela oficina. Exclusões respondem `204 No Content`.

| Recurso | Método e rota | Observação |
| --- | --- | --- |
| Login | `POST /usuarios/login` | Devolve `{ oficinaId }` |
| Usuários | `GET/POST /oficinas/{oficinaId}/usuarios` · `GET/PUT/DELETE .../usuarios/{id}` | |
| Oficinas | `GET/POST /oficinas` · `GET/PUT/DELETE /oficinas/{id}` | |
| Clientes | `GET/POST /oficinas/{oficinaId}/clientes` · `GET/PUT/DELETE .../clientes/{id}` | DELETE só sem histórico |
| Veículos | `GET/POST /oficinas/{oficinaId}/veiculos` | POST recebe `clienteId` e `idModelo` no corpo |
| | `PUT/DELETE .../veiculos/{id}` | PUT recebe `idModelo` no corpo (placa e proprietário não mudam); DELETE só sem orçamentos/OS |
| | `GET /oficinas/{oficinaId}/clientes/{clienteId}/veiculos` | Veículos do cliente |
| Marcas e modelos | `GET /marcas` · `GET /marcas/{id}` · `GET /modelos` · `GET /modelos/{id}` · `GET /modelos/marca/{idMarca}` | Somente leitura |
| Estoque | `GET/POST /oficinas/{oficinaId}/pecas` · `GET/PUT/DELETE .../pecas/{estoqueId}` | PUT altera só dados cadastrais |
| | `PATCH .../pecas/{estoqueId}/adicionar` · `PATCH .../pecas/{estoqueId}/remover` | Entrada e retirada (registro no caixa opcional) |
| | `POST .../pecas/{estoqueId}/inativar` · `POST .../pecas/{estoqueId}/reativar` | |
| Orçamentos | `GET /oficinas/{oficinaId}/orcamentos` · `GET .../orcamentos/{id}` | |
| | `POST .../orcamentos` · `PUT .../orcamentos/{id}` | Recebem as peças (`pecas`) na mesma requisição; o POST também recebe `clienteId` e `veiculoId` |
| | `POST .../orcamentos/{id}/aprovar` · `POST .../orcamentos/{id}/recusar` · `DELETE .../orcamentos/{id}` | |
| | `GET .../orcamentos/{id}/pecas` | Linhas de peças do orçamento |
| Ordens de serviço | `GET /oficinas/{oficinaId}/ordens-servico` · `GET .../ordens-servico/{id}` | |
| | `POST .../ordens-servico` · `PUT .../ordens-servico/{id}` | Recebem as peças (`pecas`) na mesma requisição; o POST também recebe `clienteId` e `veiculoId` |
| | `POST .../ordens-servico/{id}/fechar` | Corpo opcional: `{ pecas: [{ estoqueId, quantidadeUtilizada }], confirmarSemCusto }` |
| | `POST .../ordens-servico/{id}/cancelar` | Corpo: `{ motivo }` (`ClienteDesistiu`, `ValorNaoAutorizado`, `PecaIndisponivel`, `DefeitoNaoSolucionado`, `AbertaPorEngano`, `Outro`) |
| | `GET .../ordens-servico/{id}/pecas` | Linhas de peças da OS |
| Fluxo de caixa | `GET/POST /oficinas/{oficinaId}/fluxo-caixa` · `GET/PUT .../fluxo-caixa/{id}` | |
| | `POST .../fluxo-caixa/{id}/cancelar` | Cancelamento lógico |

> As rotas de peça avulsa continuam disponíveis — `POST .../pecas` (corpo `{ estoqueId, quantidade }`), `PATCH .../pecas/{idLinha}/quantidade` e `DELETE .../pecas/{idLinha}`, tanto em orçamentos quanto em OS —, mas o frontend usa o salvamento único com `pecas`.

Exemplo de corpo para criar orçamento ou OS (`pecas` é o estado final; `idLinha` nulo = peça nova; `pecas: null` = não alterar as peças). Na edição (`PUT`), `clienteId` e `veiculoId` não são enviados:

```json
{
  "clienteId": 4,
  "veiculoId": 9,
  "descricao": "Troca de pneus",
  "maoDeObra": 80.00,
  "pecas": [
    { "idLinha": 12, "estoqueId": 3, "quantidade": 2 },
    { "idLinha": null, "estoqueId": 7, "quantidade": 1 }
  ]
}
```

## Formato dos Erros

Todos os erros seguem o mesmo formato (`ErroPadrao`). O campo `errors` só aparece em erros de validação:

```json
{
  "timestamp": "2026-11-17T12:00:00Z",
  "status": 400,
  "error": "Dados inválidos",
  "message": "Revise os campos informados",
  "path": "/oficinas/1/veiculos/3/10",
  "errors": [{ "field": "placa", "message": "Placa é obrigatória" }]
}
```

| Status | Quando |
| --- | --- |
| 400 | Campo inválido (com `errors`, ex.: senha fora da regra), valor fora de uma lista fixa (com `errors` apontando o campo e os valores aceitos, ex.: `motivo`), JSON malformado, parâmetro da URL inválido |
| 401 | Login com senha incorreta |
| 403 | Login de usuário inativo |
| 404 | Registro não encontrado (ou de outra oficina) |
| 409 | Registro duplicado, registro em uso (exclusão com histórico), transição de estado inválida, conflito no banco |
| 422 | Regra de negócio violada (ex.: estoque insuficiente, cliente inativo, OS sem valor sem confirmação) |
| 500 | Erro inesperado (detalhes só no log do servidor) |

## Ordenação das listagens

As listagens de clientes, veículos (por oficina e por cliente), orçamentos, ordens de serviço e estoque retornam os registros pela data de criação decrescente, com o ID decrescente como desempate. Os registros mais recentes aparecem primeiro.

No caixa, que não possui data de criação, as movimentações são ordenadas pelo ID decrescente, priorizando os últimos cadastros independentemente da data de movimentação informada. A ordenação é aplicada no banco, mantendo os filtros existentes por oficina e cliente.

## Pendências Conhecidas
- **🔴 Crítico: autenticação com token (JWT) e perfis de acesso.** Hoje o backend confia no `oficinaId` da URL, então qualquer pessoa que alcance a API consegue (IDOR): listar todas as oficinas com e-mail e CNPJ (`GET /oficinas`), ler e alterar os dados de qualquer oficina trocando o id na URL, criar usuário em qualquer oficina e entrar com ele, e excluir oficinas (`DELETE /oficinas/{id}`). **Bloqueia a ida para produção.** O que precisa ser feito:
  - o login devolve um token assinado com o `oficinaId` e o perfil do usuário;
  - toda rota `/oficinas/{oficinaId}/...` compara o `oficinaId` da URL com o do token (403 se forem diferentes), ou passa a ler a oficina só do token;
  - `GET /oficinas`, `DELETE /oficinas/{id}` e o cadastro de oficina/usuário ficam restritos (ex.: perfil administrador);
  - limite de tentativas no login;
  - mesma resposta para e-mail inexistente e senha errada (hoje 404 "Usuário não encontrado" x 401 "Senha incorreta", o que permite descobrir e-mails cadastrados).
  O isolamento entre oficinas já está pronto no código: com o `oficinaId` correto, nenhum id de outra oficina (na URL ou no corpo) é alcançado — o JWT só precisa garantir que o `oficinaId` é o do usuário logado.
- **Testes de integração:** as regras de negócio têm testes de unidade (`.\gradlew.bat test`); faltam testes com Spring + banco para o salvamento atômico (rollback), o escopo por oficina nas consultas e o mapeamento de erros HTTP.

## Versão
Consulte o histórico de versões em: [Versões do Eixo](https://github.com/vinipxg06-pixel/Eixo_backend/commits/main/)

## Desenvolvedores
- [Bárbara Fabiana de Souza](https://github.com/Barbara-fs)
- [Clara Bianca Kistner](https://github.com/clarakistner)
- [Luiz Filipe Reis](https://github.com/ldzinl)
- [Luiz Henrique Testoni](https://github.com/luiztestonidev)
- [Vinicius dos Santos Soares](https://github.com/vinipxg06-pixel)
