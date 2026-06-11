# Fase 1: Fundação do RentFlow 🌱

Olá! Bem-vindo à nossa primeira documentação arquitetural do projeto **RentFlow**. O objetivo deste documento é explicar em linguagem simples e acessível o que fizemos nesta fase, por que fizemos, e como as engrenagens começam a girar no mundo Java com Spring Boot.

Essa documentação é perfeita para você dar uma lida antes da apresentação para o seu professor!

---

## 1. Por que usamos Maven?
No ecossistema Java, nós temos muitas "bibliotecas" (códigos de outras pessoas que usamos para facilitar a vida, como conexão com banco, frameworks web, etc). 
A nossa escolha foi usar o **Maven**. Ele é o maestro do nosso projeto. No arquivo `pom.xml`, nós apenas listamos o que precisamos (ex: "Me dê o Spring Web", "Me dê o Thymeleaf", "Me dê o driver do PostgreSQL") e o Maven vai na internet, faz o download automático e configura tudo para nós. 

**Vantagem na apresentação:** O Maven usa o formato XML, que é super descritivo. Se o professor perguntar onde estão as dependências do projeto, basta abrir o `pom.xml`. Nós escolhemos ele em vez do Gradle porque sua curva de aprendizado é menor e ele é o padrão mais tradicional da indústria Java.

---

## 2. Como o Spring Boot sabe onde está o Banco de Dados?
No mundo React/Node.js, você tinha um arquivo `.env` para esconder as senhas. No Spring Boot, o padrão (para configurações locais e simplificadas) é usar o arquivo `application.properties` (que fica na pasta `src/main/resources`).

Nele nós configuramos:
```properties
spring.datasource.url=jdbc:postgresql://aws-1-sa-east-1.pooler.supabase.com:5432/postgres
spring.datasource.username=postgres.mxfgyfxcviitbnllkatv
spring.datasource.password=DB2026_ufrpe
spring.jpa.hibernate.ddl-auto=none
```

### O pulo do gato: `ddl-auto=none`
No JPA (a ferramenta do Java que transforma tabelas em Objetos), há um recurso mágico que *cria tabelas automaticamente* se elas não existirem. Porém, no nosso caso, **o banco já existe e está populado**. Se deixássemos o Spring tentar criar, daria conflito. Usar `none` diz ao Spring: *"Ei, não mexa no banco. As tabelas já estão lá, apenas se conecte"*.

---

## 3. Segurança Autêntica (Spring Security)
Você pediu um sistema de autenticação *real*. Em Java, quem resolve isso com maestria é o **Spring Security**. 
Criamos uma classe chamada `SecurityConfig`. Ela atua como um "Leão de Chácara" (Filtro) na porta da nossa aplicação. 

**O que ela faz agora?**
Ela bloqueia TODAS as páginas. Se alguém tentar entrar na URL, será barrado e redirecionado para uma tela de login. 

**Decisão de Arquitetura Temporária:**
Se você olhar no banco de dados, na tabela `FUNCIONARIOS`, temos o nome, o CPF e o cargo, mas **não temos uma coluna de senha**. Para não travar a fase 1 alterando o banco, nós colocamos 2 usuários "em memória" no código (`admin` e `atendente`). 
Nas próximas fases, para ter a autenticação 100% realística usando banco, nós precisaremos dar um comando SQL para adicionar a coluna `senha` na tabela `FUNCIONARIOS` e ensinar o Spring a ler essa tabela na hora do login.

---

### Resumo da Ópera
Nesta Fase 1 nós:
1. Preparamos o terreno (Maven).
2. Ligamos a mangueira de dados (Conexão PostgreSQL no Supabase).
3. Colocamos uma fechadura na porta (Spring Security).

Estamos prontos para a Fase 2, onde vamos construir as "Entidades" (Mapeamento Objeto-Relacional). 🚀
