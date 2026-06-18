# Apresentação e Guia de Estudo: RentFlow

Este documento foi elaborado de forma objetiva e rica em detalhes práticos para servir como o seu roteiro de estudos definitivo para a apresentação do sistema **RentFlow** na disciplina de **Programação II** da **UFRPE**.

---

## 1. Mapeamento Geral do Projeto: Pastas e Arquivos

O projeto segue a estrutura padrão do Maven e as convenções do ecossistema Spring Boot. Abaixo está o papel de cada pasta e de seus principais arquivos:

### `src/main/java/com/ufrpe/rentflow`

#### `config/` (Configurações Globais do Sistema)
*   [SecurityConfig.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/config/SecurityConfig.java): Arquivo central de segurança. Define quais rotas são públicas (login, CSS, JS) e quais exigem permissão de Gerente (como `/tabelas-auxiliares` e deleções `/*/excluir/**`). Também gerencia a codificação de senhas com `PasswordEncoder` e implementa o carregamento de usuários customizado via `UserDetailsService`.
*   **Conversores de Enum (Ex: `StatusVeiculoConverter.java`):** Conversores customizados que estendem a classe base `EnumConverterBase` para converter ENUMs Java (como `DISPONIVEL`, `LOCADO`) em caracteres específicos ou inteiros no banco de dados PostgreSQL.

#### `controller/` (Controladores - Camada de Apresentação/Orquestração)
Recebem as requisições HTTP, preparam os dados e direcionam o fluxo do sistema.
*   [HomeController.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/controller/HomeController.java): Mapeia a tela inicial (Dashboard). Ele busca métricas consolidadas (quantidade de carros locados, manutenções ativas) através dos serviços e injeta no template da Home.
*   [ClienteController.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/controller/ClienteController.java) e [VeiculoController.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/controller/VeiculoController.java): Gerenciam as telas e requisições de cadastros, edições e listagens de Clientes e Veículos.
*   [LocacaoController.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/controller/LocacaoController.java): Controla todo o fluxo do aluguel (reserva, cancelamento, entrega/retirada e devolução).
*   [ManutencaoController.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/controller/ManutencaoController.java): Controla a ida e volta de veículos para revisões/consertos.
*   [GlobalControllerAdvice.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/controller/GlobalControllerAdvice.java): Intercepta erros do tipo `RegraNegocioException` globalmente de forma a redirecionar o usuário de volta com uma mensagem amigável no frontend, evitando telas de erro cinzas (Whitelabel).

#### `exception/` (Exceções Customizadas)
*   [RegraNegocioException.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/exception/RegraNegocioException.java): Exceção customizada criada especificamente para sinalizar violações de regras operacionais do sistema (ex: tentar alugar carro com CNH vencida).

#### `model/entity/` (Entidades de Domínio - Onde os Dados são Mapeados)
Classes Java anotadas com `@Entity` que representam as tabelas do banco de dados relacional (JPA/Hibernate).
*   [Cliente.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/model/entity/Cliente.java): Representa clientes PF ou PJ.
*   [Veiculo.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/model/entity/Veiculo.java): Representa a frota.
*   [Locacao.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/model/entity/Locacao.java): Armazena as datas de locação, valores cobrados e chaves de associação.
*   **Outras Entidades:** `Funcionario`, `Seguro`, `Categoria`, `CobrancaExtra`, `Pagamento`, `Vistoria`, `TelefonesCliente` (e sua chave composta `TelefonesClienteId`).

#### `model/enums/` (ENUMs de Negócio)
Garantem padronização de estados e tipos aceitos pelo sistema.
*   `StatusVeiculo` (DISPONIVEL, LOCADO, EM_MANUTENCAO).
*   `StatusLocacao` (RESERVADA, ATIVA, ENCERRADA, CANCELADA).
*   `CargoFuncionario` (GERENTE, ATENDENTE).
*   `FormaPagamento`, `TipoCobranca`, `TipoCombustivel`, etc.

#### `repository/` (Acesso ao Banco de Dados)
Interfaces Java que estendem `JpaRepository`. Não possuem implementação física de código; o Spring Data JPA cria as consultas SQL em tempo de execução.
*   **Exemplo:** `LocacaoRepository` possui métodos customizados e assinaturas abstratas para ler e escrever dados no banco PostgreSQL.

#### `service/` (Camada de Regras de Negócio - Onde a Lógica Acontece)
Fazem a ponte entre o Controller e o Repository. Tratam as validações lógicas e calculam valores.
*   [LocacaoService.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/service/LocacaoService.java): Executa a lógica de reservas, cancelamentos, vistorias de entrega e cálculos finais no encerramento do contrato.
*   [ManutencaoService.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/service/ManutencaoService.java): Garante que a transição de um veículo para manutenção altere seu status no banco de dados e retorne de forma consistente.

---

### `src/main/resources`
*   `templates/`: Páginas HTML estruturadas com Thymeleaf. O arquivo base `layout.html` monta o menu lateral e barra de navegação. As páginas específicas (como `clientes/lista.html`, `locacoes/lista.html`) injetam apenas o seu conteúdo principal dentro dele.
*   `static/`: Contém arquivos estáticos locais que não mudam (CSS, JavaScript, Imagens e Ícones).
*   `application.properties`: Configurações de inicialização do Spring Boot (como a porta `3000` e parâmetros gerais de banco de dados).

---

## 2. Fluxo de Dados de Ponta a Ponta (End-to-End)

Para explicar o comportamento do código, use o exemplo do **registro de uma locação**:

```
[ Usuário ] ➔ [ View (HTML) ] ➔ [ Controller ] ➔ [ Service ] ➔ [ Repository ] ➔ [ Banco de Dados ]
```

1.  **Ação do Usuário (View):** O usuário preenche o formulário de locação na tela e clica em "Salvar". Isso envia uma requisição `POST` com os dados do formulário para a URL `/locacoes/salvar`.
2.  **Captura da Requisição (Controller):** O [LocacaoController.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/controller/LocacaoController.java) intercepta a requisição. Ele recebe os dados mapeados para o objeto Java `Locacao` (via `@ModelAttribute`) e repassa a responsabilidade chamando `locacaoService.registrarLocacao(locacao)`.
3.  **Processamento Lógico (Service):** O [LocacaoService.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/service/LocacaoService.java) assume o fluxo em uma transação (`@Transactional`):
    *   Busca o objeto `Veiculo` completo no banco através do `veiculoRepository`.
    *   Valida regras de negócio (se o carro está livre, se o cliente é inadimplente, etc.).
    *   Muda o status do veículo: `veiculo.setStatus(StatusVeiculo.LOCADO)`.
    *   Invoca `veiculoRepository.save(veiculo)` e `locacaoRepository.save(locacao)`.
4.  **Escrita Física (Repository/BD):** O Spring Data JPA traduz essas chamadas em comandos SQL (`UPDATE VEICULOS ...` e `INSERT INTO LOCACOES ...`) e executa fisicamente no banco de dados PostgreSQL.
5.  **Resposta:** O `LocacaoController` recebe o retorno do serviço. Adiciona uma mensagem flash de sucesso e retorna um redirecionamento (`redirect:/locacoes`) para recarregar a listagem já com o novo contrato criado.

---

## 3. Os 4 Pilares do POO Aplicados no Código

Ponto crucial para a banca de Programação II. Mostre onde cada conceito foi implementado:

### A. Abstração (Classes e Entidades de Domínio)
Modelar um objeto do mundo real trazendo para o sistema apenas seus atributos e comportamentos fundamentais de negócio.
*   **Onde ver:** [Veiculo.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/model/entity/Veiculo.java#L10-L45)
*   **Trecho do código:**
    ```java
    public class Veiculo {
        private String placa;
        private Categoria categoria;
        private String renavam;
        private String marca;
        private String modelo;
        private Integer kmAtual;
        private StatusVeiculo status;
        // ...
    }
    ```
*   **Explicação:** A classe `Veiculo` possui apenas propriedades cruciais para a locação. Atributos físicos irrelevantes para as regras de negócio do sistema (número do chassi do motor, tipo de suspensão, cor dos bancos) foram abstraídos.

### B. Encapsulamento (Proteção do Estado Interno)
Garantir que os atributos de um objeto não sejam modificados arbitrariamente de fora da classe, protegendo suas regras internas.
*   **Onde ver:** Atributos privados e métodos de acesso em [Veiculo.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/model/entity/Veiculo.java#L47-L81) e a lógica de negócios em [LocacaoService.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/service/LocacaoService.java#L35-L56).
*   **Trecho do código:**
    ```java
    // 1. Atributo privado impede acesso direto de fora da classe
    private StatusVeiculo status = StatusVeiculo.DISPONIVEL;

    // 2. Modificação controlada via método em LocacaoService.java
    if (veiculo.getStatus() != StatusVeiculo.DISPONIVEL) {
        throw new RegraNegocioException("O veículo não está disponível para locação no momento.");
    }
    veiculo.setStatus(StatusVeiculo.LOCADO);
    ```
*   **Explicação:** Os atributos são `private` e só podem ser alterados/lidos via métodos específicos. As regras de transição de estado do veículo (como mudar para `LOCADO`) estão encapsuladas no método do serviço, garantindo que o status do carro só seja alterado caso ele cumpra todas as regras de negócio necessárias.

### C. Herança (Reutilização e Especialização)
Permitir que uma classe herde comportamento de outra classe mãe, promovendo reaproveitamento de código.
*   **Onde ver:** [RegraNegocioException.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/exception/RegraNegocioException.java#L4-L10).
*   **Trecho do código:**
    ```java
    public class RegraNegocioException extends RuntimeException {
        public RegraNegocioException(String mensagem) {
            super(mensagem);
        }
    }
    ```
*   **Explicação:** `RegraNegocioException` herda (`extends`) da classe nativa `RuntimeException` da biblioteca padrão do Java. Ela reutiliza toda a infraestrutura de rastreamento de pilha (stack trace) e lançamento de erros do ecossistema, redefinindo apenas o construtor usando o comando `super()` para passar a mensagem de erro.

### D. Polimorfismo (Comportamentos Flexíveis)
Tratar objetos de tipos específicos por meio de suas interfaces comuns ou classes abstratas mães.
*   **Onde ver:** A interface customizada de autenticação em [SecurityConfig.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/config/SecurityConfig.java#L58-L88).
*   **Trecho do código:**
    ```java
    @Bean
    public UserDetailsService userDetailsService(FuncionarioRepository funcionarioRepository, PasswordEncoder encoder) {
        return username -> {
            Funcionario func = funcionarioRepository.findByLogin(username)
                .orElseGet(() -> { ... });

            return User.builder()
                .username(func.getLogin())
                .password(func.getSenha())
                .roles(func.getCargo().name())
                .build();
        };
    }
    ```
*   **Explicação:** O Spring Security depende da interface `UserDetailsService` para realizar login. Nós fornecemos a nossa própria implementação que vai no banco buscar o nosso objeto `Funcionario` e retorna o login/senha. O Spring Security opera o fluxo polimorficamente através dessa interface sem precisar saber a estrutura interna da nossa tabela de funcionários.

---

## 4. Destaques Importantes (O que você DEVE citar para a Banca)

1.  **Associação entre Entidades (Relacionamentos JPA):**
    No arquivo [Locacao.java](file:///c:/Users/Rafael/OneDrive/Documents/Projetos/UFRPE/programacao_ii/RentFlow/src/main/java/com/ufrpe/rentflow/model/entity/Locacao.java#L19-L41), em vez de guardarmos apenas strings ou IDs numéricos isolados, associamos os objetos completos (`private Cliente cliente`, `private Veiculo veiculo`). Isso reflete a **Associação** em POO, permitindo a navegação entre objetos (exemplo: `locacao.getCliente().getNome()`).
2.  **Segurança Baseada em Roles (RBAC):**
    Existem dois perfis no sistema: `GERENTE` e `ATENDENTE`. A verificação é de ponta a ponta:
    *   *Backend:* Em `SecurityConfig.java` as requisições de exclusão de registros `/*/excluir/**` e telas administrativas são protegidas com `.hasRole("GERENTE")`.
    *   *Frontend:* No arquivo HTML `layout.html`, o Thymeleaf Security (`sec:authorize="hasRole('GERENTE')"`) esconde os botões e links de administração se o usuário logado for apenas um atendente.
3.  **Tratamento Centralizado de Exceções (`@ControllerAdvice`):**
    O arquivo `GlobalControllerAdvice.java` captura qualquer ocorrência da exceção `RegraNegocioException` que aconteça em qualquer local do sistema. Ele impede o crash da aplicação ou a exibição de uma página genérica de erro do servidor, redirecionando o usuário de forma amigável com alertas no frontend.
4.  **Auto-Provisionamento de Dados para Testes:**
    No carregador de segurança (`SecurityConfig.java`), se o banco de dados for detectado como vazio, o sistema automaticamente cria os registros para o funcionário `admin` (senha `admin`, nível Gerente) e `atendente` (senha `123`, nível Atendente) no banco. Isso garante que o professor consiga rodar e testar o sistema imediatamente sem precisar de scripts manuais.
