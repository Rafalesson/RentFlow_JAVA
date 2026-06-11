package com.ufrpe.rentflow.model.entity;

import com.ufrpe.rentflow.model.enums.CategoriaCnh;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "CLIENTES")
public class Cliente {

    @Id
    @Column(length = 14)
    private String cpf;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, length = 200, unique = true)
    private String email;

    @Column(nullable = false)
    private Boolean inadimplente = false;

    @Column(name = "cnh_numero", nullable = false, length = 20, unique = true)
    private String cnhNumero;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "cnh_categoria", nullable = false)
    private CategoriaCnh cnhCategoria;

    @Column(name = "cnh_validade", nullable = false)
    private LocalDate cnhValidade;

    @Column(name = "endereco_rua", nullable = false, length = 200)
    private String enderecoRua;

    @Column(name = "endereco_numero", nullable = false, length = 20)
    private String enderecoNumero;

    @Column(name = "endereco_bairro", nullable = false, length = 100)
    private String enderecoBairro;

    @Column(name = "endereco_cidade", nullable = false, length = 100)
    private String enderecoCidade;

    @Column(name = "endereco_estado", nullable = false, length = 2)
    private String enderecoEstado;

    @Column(name = "endereco_cep", nullable = false, length = 9)
    private String enderecoCep;

    // Getters and Setters

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getInadimplente() { return inadimplente; }
    public void setInadimplente(Boolean inadimplente) { this.inadimplente = inadimplente; }

    public String getCnhNumero() { return cnhNumero; }
    public void setCnhNumero(String cnhNumero) { this.cnhNumero = cnhNumero; }

    public CategoriaCnh getCnhCategoria() { return cnhCategoria; }
    public void setCnhCategoria(CategoriaCnh cnhCategoria) { this.cnhCategoria = cnhCategoria; }

    public LocalDate getCnhValidade() { return cnhValidade; }
    public void setCnhValidade(LocalDate cnhValidade) { this.cnhValidade = cnhValidade; }

    public String getEnderecoRua() { return enderecoRua; }
    public void setEnderecoRua(String enderecoRua) { this.enderecoRua = enderecoRua; }

    public String getEnderecoNumero() { return enderecoNumero; }
    public void setEnderecoNumero(String enderecoNumero) { this.enderecoNumero = enderecoNumero; }

    public String getEnderecoBairro() { return enderecoBairro; }
    public void setEnderecoBairro(String enderecoBairro) { this.enderecoBairro = enderecoBairro; }

    public String getEnderecoCidade() { return enderecoCidade; }
    public void setEnderecoCidade(String enderecoCidade) { this.enderecoCidade = enderecoCidade; }

    public String getEnderecoEstado() { return enderecoEstado; }
    public void setEnderecoEstado(String enderecoEstado) { this.enderecoEstado = enderecoEstado; }

    public String getEnderecoCep() { return enderecoCep; }
    public void setEnderecoCep(String enderecoCep) { this.enderecoCep = enderecoCep; }

    // POO: equals e hashCode baseados na chave natural (CPF)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente that = (Cliente) o;
        return Objects.equals(cpf, that.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    @Override
    public String toString() {
        return "Cliente{cpf='" + cpf + "', nome='" + nome + "'}";
    }
}
