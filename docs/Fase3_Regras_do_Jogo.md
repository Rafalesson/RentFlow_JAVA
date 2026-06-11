# Fase 3: As Regras do Jogo e a Orientação a Objetos (POO) 🧠

Olá! Nesta fase nós construímos os **Repositórios** e os **Serviços** da nossa aplicação. 
Você me perguntou se estamos usando POO (Programação Orientada a Objetos), já que é um requisito fundamental do professor. A resposta é: **Sim, 100%!** O ecossistema Spring Boot no Java é o maior playground de POO que existe.

Vou te explicar exatamente como a POO está sendo aplicada aqui para você poder argumentar com segurança na apresentação:

---

## 1. Abstração e Herança (Repositórios)
Se você abrir a classe `LocacaoRepository`, verá o seguinte código:
```java
public interface LocacaoRepository extends JpaRepository<Locacao, Integer> { }
```
**Onde está a POO aqui?**
- **Abstração:** Nós não escrevemos como o banco salva, deleta ou busca os dados (nenhum comando `INSERT` ou `SELECT`). Nós apenas declaramos uma *Interface*. 
- **Herança (`extends`):** Ao herdar de `JpaRepository`, nós ganhamos "de graça" mais de 20 métodos prontos do Spring para gerenciar a classe `Locacao`. A classe filha herda todo o comportamento poderoso da classe mãe.

## 2. Composição e Encapsulamento (Serviços)
Abra a classe `LocacaoService.java`. É nela que mora a regra de negócio (ex: o carro não pode ser alugado se estiver em manutenção).
```java
public class LocacaoService {
    private final LocacaoRepository locacaoRepository;
    private final VeiculoRepository veiculoRepository;
    // ...
}
```
**Onde está a POO aqui?**
- **Encapsulamento (`private final`):** O Serviço blinda seus repositórios contra alterações externas. Nenhuma outra classe pode mexer no repositório dele diretamente.
- **Composição:** O `LocacaoService` não sabe acessar o banco, mas ele "TEM UM" (`has-a`) repositório de locação e "TEM UM" repositório de veículo. Ele delega tarefas para esses objetos.

## 3. Lançamento de Exceções Customizadas (Herança)
Para tratar os erros de forma elegante (ex: Cliente tentando locar um carro indisponível), criamos a classe `RegraNegocioException`:
```java
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
```
**Onde está a POO aqui?**
Nós criamos uma "Exceção filha". Ela herda o comportamento de erro do Java (`RuntimeException`), mas adiciona o nosso próprio sabor e clareza. Quando o professor vir isso, vai notar que você dominou a criação de tipos específicos para problemas de negócio.

## 4. O Comportamento do Objeto
No React/Node.js clássico, é comum a gente pegar um "JSON" solto, mudar uma variável e mandar pro banco.
No Java, nós operamos **sempre sobre os Objetos**:
```java
veiculo.setStatus(StatusVeiculo.locado); // Mudança de estado interna do objeto (POO)
veiculoRepository.save(veiculo);
```
O objeto `veiculo` é o dono do seu próprio estado. Ao invés de manipular dados crus, nós pedimos gentilmente ao objeto para mudar seu status e, depois, pedimos ao repositório para persisti-lo.

---

### Resumo da Fase 3
Construímos a camada de inteligência (`Service`) isolada da camada de dados (`Repository`), usando fortemente Encapsulamento, Composição, Abstração e Herança. 
Com isso, o backend (Cérebro) está robusto! 

A **Fase 4 e 5** são as finais: onde vamos criar os `Controllers` (para receber os cliques do navegador) e o **Thymeleaf + Bootstrap** (para a interface web bonita e responsiva). 
