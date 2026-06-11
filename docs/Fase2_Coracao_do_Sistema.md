# Fase 2: O Coração do Sistema (Entidades JPA) ❤️

Olá novamente! Se a Fase 1 foi preparar o terreno, a Fase 2 foi plantar as sementes fundamentais do nosso projeto. Nesta etapa, nós fizemos o **Mapeamento Objeto-Relacional (ORM)**. 

Se o seu professor perguntar: *"O que é ORM?"*, você pode responder que é a ponte que traduz o mundo dos Bancos de Dados Relacionais (tabelas, linhas e colunas) para o mundo da Programação Orientada a Objetos (Classes, Objetos e Atributos). No Java, a ferramenta padrão que faz essa mágica é o **JPA** (Java Persistence API), e o **Hibernate** é quem trabalha nos bastidores implementando essa API.

---

## 1. Mapeando os ENUMs
O banco de dados do RentFlow possui um recurso super avançado e seguro do PostgreSQL: tipos personalizados (`ENUMs`). Por exemplo, o `status_veiculo` só aceita valores específicos. 

Para que o Java entendesse isso sem dar erro de tipo, criamos `Enums` no Java e usamos anotações modernas (introduzidas no Hibernate 6) para "casar" os tipos perfeitamente:
```java
@Enumerated(EnumType.STRING)
@JdbcTypeCode(SqlTypes.NAMED_ENUM) // Isso avisa ao Hibernate: "Ei, isso é um ENUM nativo do Postgres!"
private StatusVeiculo status;
```

---

## 2. A Evolução do Funcionário
Lembra que eu falei na Fase 1 que faltava a senha? Bem, você me informou (com aquela imagem do Supabase) que a tabela `FUNCIONARIOS` evoluiu durante o desenvolvimento em React. Ela ganhou `email`, `login`, `senha` e `foto_perfil` (como uma string base64). 

**Decisão Arquitetural:** Em vez de ignorar, eu adaptei imediatamente a entidade `Funcionario` no Java. Nós usamos a anotação `@Column(columnDefinition = "TEXT")` para a `foto_perfil` porque strings em base64 são gigantes e o tipo padrão de texto do banco de dados (Varchar) normalmente trunca (corta) textos muito longos.

---

## 3. Relacionamentos (As Chaves Estrangeiras)
No `schema.sql`, nós temos chaves estrangeiras. Por exemplo, uma `Locacao` pertence a um `Cliente` e a um `Veiculo`.
No Java, nós não guardamos o "CPF do Cliente" e a "Placa do Veículo" dentro da classe Locação. Nós guardamos o próprio Objeto!

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "placa_veiculo", nullable = false)
private Veiculo veiculo;
```

**O que é esse `FetchType.LAZY`? (Dica de Ouro para a Apresentação)**
Se chamássemos uma locação no banco de dados, o Hibernate automaticamente tentaria buscar o Veículo, que buscaria a Categoria... E isso faria uma "avalanche" de consultas no banco de dados, deixando o sistema absurdamente lento. O `LAZY` (preguiçoso) diz ao Hibernate: *"Me traga apenas os dados da Locação. Só vá buscar os dados do Veículo no banco de dados SE eu explicitamente pedir (`locacao.getVeiculo()`)."* Isso otimiza nossa performance e mostra que nos preocupamos com Clean Code!

---

### Resumo da Ópera
Nesta Fase 2 nós:
1. Traduzimos 9 `ENUMs` do PostgreSQL para Java.
2. Criamos as Classes principais (Nível 0).
3. Atualizamos a classe Funcionario com os dados atualizados de login e senha.
4. Mapeamos as Locações, Vistorias, Pagamentos e Manutenções ligando tudo com `@ManyToOne`.

Nossa base de dados está modelada na memória da aplicação. Na **Fase 3**, nós vamos criar os "Repositórios" (que fazem as consultas automaticamente) e os "Serviços" (que carregam as Regras de Negócio).
