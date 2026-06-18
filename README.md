# RentFlow — Sistema de Locação de Veículos

O RentFlow é um sistema web para gerenciamento de locação de veículos, desenvolvido como projeto prático para a disciplina de Programação II na UFRPE (Universidade Federal Rural de Pernambuco).

A aplicação foi projetada para otimizar os fluxos de trabalho de locadoras de veículos, oferecendo um controle completo desde a gestão de frotas e cadastros de clientes até o cálculo dinâmico de contratos de locação e manutenções preventivas.

---

## 🌐 Demonstração Online

A aplicação está hospedada e disponível para testes no Render:

*   **Link de Acesso:** [https://rentflow-java.onrender.com/login](https://rentflow-java.onrender.com/login)

> [!NOTE]
> Como o sistema está hospedado na camada gratuita do Render, o servidor entra em modo de hibernação após um período de inatividade. Por conta disso, o primeiro acesso pode demorar cerca de **50 segundos** para carregar enquanto a instância é inicializada. Os acessos seguintes serão instantâneos.

---

## Tecnologias Utilizadas

A stack tecnológica do projeto consiste em:

*   **Linguagem:** Java 17
*   **Framework Principal:** Spring Boot 3.2.5
    *   *Spring Data JPA* (Mapeamento Objeto-Relacional e persistência)
    *   *Spring Security* (Autenticação e autorização baseadas em perfis de acesso)
    *   *Spring Web* (Arquitetura MVC)
    *   *Spring Validation* (Validação de entrada de dados)
*   **Template Engine:** Thymeleaf (com suporte a *Thymeleaf Layout Dialect* para componentes reutilizáveis e *Thymeleaf Extras Spring Security* para controle visual de permissões)
*   **Banco de Dados:** PostgreSQL (Pronto para conexão local ou em nuvem via Supabase)
*   **Interface Gráfica:** HTML5, CSS3 Customizado, JavaScript e Bootstrap
*   **Gerenciador de Dependências:** Maven
*   **Containerização:** Docker (Suporte a builds multi-stage)

---

## Principais Funcionalidades

### Painel Operacional (Dashboard)
*   Visualização de métricas da frota (veículos totais, disponíveis, locados, em manutenção).
*   Visualização de clientes inadimplentes e locações ativas.
*   Lista das locações mais recentes do sistema.

### Gestão de Frota
*   Cadastro completo de veículos com informações de marca, modelo, placa, ano, cor, quilometragem, combustível e categoria.
*   Controle de status em tempo real (Disponível, Locado, Em Manutenção).

### Controle de Clientes
*   Cadastro de clientes, suportando Pessoa Física (CPF) e Pessoa Jurídica (CNPJ).
*   Mapeamento de múltiplos telefones de contato por cliente.

### Ciclo de Locações
*   Abertura de contratos vinculando cliente, veículo, seguro contratado e funcionário responsável.
*   Cálculo dinâmico automatizado de tarifas (diárias, custos de seguro e taxas extras).
*   Controle de vistorias de entrega e devolução.
*   Fechamento financeiro com registro da forma de pagamento e cobranças extras (multas, atrasos, danos).

### Gestão de Manutenção
*   Registro e agendamento de manutenções preventivas e corretivas.
*   Controle de custos por manutenção para monitorar a rentabilidade dos veículos.

### Controle de Acesso e Permissões
*   Autenticação via Spring Security com diferentes níveis de autorização.
*   Perfis de acesso para Atendentes e Gerentes.
*   Apenas usuários com perfil Gerente têm permissão para acessar áreas administrativas (Tabelas Auxiliares) e realizar ações de exclusão.

---

## Credenciais para Teste

O sistema auto-provisiona usuários de teste caso não existam no banco de dados. Você pode utilizar as seguintes credenciais na tela de login:

| Perfil | Usuário (`username`) | Senha (`password`) | Permissões |
| :--- | :--- | :--- | :--- |
| **Gerente** | `admin` | `admin` | Acesso total ao sistema, incluindo exclusões e tabelas auxiliares. |
| **Atendente** | `atendente` | `123` | Acesso a cadastros e locações. Permissões de exclusão bloqueadas. |

---

## Como Rodar o Projeto

Você pode executar o projeto localmente de duas formas: usando o Maven ou através do Docker.

### Pré-requisitos
*   Java 17 instalado (caso execute localmente).
*   PostgreSQL rodando localmente ou uma instância externa.

---

### Opção 1: Executando Localmente (Maven)

1.  **Configurar o Banco de Dados:**
    Por padrão, o projeto busca o banco de dados em `localhost:5432/rentflow`. Caso precise alterar ou queira rodar conectado ao seu banco, crie um arquivo chamado `application-local.properties` em `src/main/resources/` (este arquivo já está configurado no `.gitignore` para sua segurança):
    ```properties
    spring.datasource.url=jdbc:postgresql://seu-host:5432/seu-banco
    spring.datasource.username=seu-usuario
    spring.datasource.password=sua-senha
    ```

2.  **Iniciar a Aplicação:**
    *   **Sem arquivo de configurações local (padrão localhost):**
        ```bash
        ./mvnw spring-boot:run
        ```
    *   **Usando o arquivo `application-local.properties` (recomendado):**
        ```bash
        ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
        ```

3.  **Acessar a Aplicação:**
    Abra o navegador e acesse [http://localhost:3000](http://localhost:3000).

---

### Opção 2: Executando via Docker

O projeto já inclui um `Dockerfile` configurado com build multi-stage, otimizando o tamanho da imagem de produção.

1.  **Construir a Imagem Docker:**
    ```bash
    docker build -t rentflow .
    ```

2.  **Rodar o Container:**
    Substitua os valores das variáveis pelas credenciais de conexão do seu banco de dados PostgreSQL:
    ```bash
    docker run -d -p 3000:3000 \
      -e DB_URL=jdbc:postgresql://host.docker.internal:5432/rentflow \
      -e DB_USERNAME=postgres \
      -e DB_PASSWORD=sua_senha \
      --name rentflow-app \
      rentflow
    ```

3.  **Acessar a Aplicação:**
    Acesse [http://localhost:3000](http://localhost:3000).

---

## Estrutura de Diretórios

*   `src/main/java/com/ufrpe/rentflow`
    *   `config/`: Configurações do Spring Security e conversores.
    *   `controller/`: Controladores MVC que gerenciam as rotas e regras de fluxo da interface.
    *   `exception/`: Classes de tratamento de exceções de negócio.
    *   `model/entity/`: Entidades JPA que representam as tabelas do banco de dados.
    *   `model/enums/`: Enums para representação consistente de status e tipos.
    *   `repository/`: Interfaces do Spring Data JPA para comunicação direta com o banco.
    *   `service/`: Camada de regras de negócio e validações lógicas do sistema.
*   `src/main/resources`
    *   `static/css/`: Folhas de estilo personalizadas (style.css).
    *   `static/images/`: Assets visuais e avatares do sistema.
    *   `templates/`: Páginas HTML estruturadas com Thymeleaf.
