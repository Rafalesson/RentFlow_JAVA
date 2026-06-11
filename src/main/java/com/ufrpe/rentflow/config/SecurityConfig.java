package com.ufrpe.rentflow.config;

import com.ufrpe.rentflow.model.entity.Funcionario;
import com.ufrpe.rentflow.model.enums.CargoFuncionario;
import com.ufrpe.rentflow.repository.FuncionarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Permitir acesso aos arquivos estáticos (CSS, JS, Imagens do Bootstrap), rota de login e erro
                .requestMatchers("/css/**", "/js/**", "/img/**", "/login", "/error").permitAll()
                // Limitar acesso a tabelas auxiliares apenas para gerentes
                .requestMatchers("/tabelas-auxiliares/**").hasRole("GERENTE")
                // Limitar exclusão de registros (funcionalidades destrutivas) apenas para gerentes
                .requestMatchers("/*/excluir/**").hasRole("GERENTE")
                // Qualquer outra requisição precisará de autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    // ================================================================
    // TODO: TEMPORÁRIO — Credenciais em memória apenas para desenvolvimento.
    // Em produção, conectar à tabela FUNCIONARIOS do banco de dados.
    // NUNCA usar senhas fracas como "admin" ou "123" em ambiente real.
    // ================================================================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(FuncionarioRepository funcionarioRepository, PasswordEncoder encoder) {
        return username -> {
            Funcionario func = funcionarioRepository.findByLogin(username)
                .orElseGet(() -> {
                    // Auto-provision default users if they don't exist yet in the database
                    Funcionario novo = new Funcionario();
                    novo.setLogin(username);
                    if ("admin".equals(username)) {
                        novo.setNome("Administrador RentFlow");
                        novo.setCpf("111.111.111-11");
                        novo.setCargo(CargoFuncionario.GERENTE);
                        novo.setEmail("admin@rentflow.com");
                        novo.setSenha(encoder.encode("admin"));
                    } else if ("atendente".equals(username)) {
                        novo.setNome("Atendente RentFlow");
                        novo.setCpf("222.222.222-22");
                        novo.setCargo(CargoFuncionario.ATENDENTE);
                        novo.setEmail("atendente@rentflow.com");
                        novo.setSenha(encoder.encode("123"));
                    } else {
                        throw new org.springframework.security.core.userdetails.UsernameNotFoundException("Usuário não encontrado: " + username);
                    }
                    return funcionarioRepository.save(novo);
                });

            return User.builder()
                .username(func.getLogin())
                .password(func.getSenha())
                .roles(func.getCargo().name()) // GERENTE or ATENDENTE
                .build();
        };
    }
}
