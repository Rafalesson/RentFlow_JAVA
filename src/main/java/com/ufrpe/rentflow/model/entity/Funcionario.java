package com.ufrpe.rentflow.model.entity;

import com.ufrpe.rentflow.model.enums.CargoFuncionario;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "FUNCIONARIOS")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_func")
    private Integer idFunc;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 14, unique = true)
    private String cpf;

    @Column(nullable = false)
    private CargoFuncionario cargo;

    @Column(length = 200, unique = true)
    private String email;

    @Column(length = 50, unique = true)
    private String login;

    @Column(length = 255)
    private String senha;

    @Column(name = "foto_perfil", columnDefinition = "TEXT")
    private String fotoPerfil;

    // Getters e Setters

    public Integer getIdFunc() {
        return idFunc;
    }

    public void setIdFunc(Integer idFunc) {
        this.idFunc = idFunc;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public CargoFuncionario getCargo() {
        return cargo;
    }

    public void setCargo(CargoFuncionario cargo) {
        this.cargo = cargo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funcionario that = (Funcionario) o;
        return Objects.equals(idFunc, that.idFunc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFunc);
    }

    @Override
    public String toString() {
        return "Funcionario{idFunc=" + idFunc + ", nome='" + nome + "', cargo=" + cargo + "}";
    }
}
