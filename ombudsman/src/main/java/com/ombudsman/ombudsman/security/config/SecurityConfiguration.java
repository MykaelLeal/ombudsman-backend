package com.ombudsman.ombudsman.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.School.SchoolReportSystem.security.auth.UserAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserAuthenticationFilter userAuthenticationFilter;

    // Endpoints que NÃO requerem autenticação para serem acessados (públicos)
    public static final String[] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
        "/users/login",       // Login de usuário
        "/users"              // Criação de novo usuário
    };

    // Endpoints que requerem autenticação para serem acessados (usuário logado)
    public static final String[] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = {
        "/disciplinas/",             // Listar disciplinas (GET)
        "/disciplinas/create",      // Criar disciplina (POST)
        "/disciplinas/{id}",       // Buscar disciplina por ID (GET)
        "/disciplinas/{id}",      // Atualizar disciplina (PUT)
        "/disciplinas/{id}",     // Deletar disciplina (DELETE)


        "/notas/create",                                     // Criar nota (POST)
        "/notas/aluno",                                     // Listar notas do aluno (GET)
        "/notas/disciplina/{id}",                          // Listar notas de uma disciplina (GET)
        "/notas/{id}",                                    // Atualizar nota por ID (PUT)
        "/notas/{id}",                                   // Deletar nota por ID (DELETE)
        "/notas/disciplina/{disciplinaId}/aluno/{alunoId}",  // Atualizar nota por disciplina e aluno (PUT)

    };

    // Endpoints acessíveis apenas por usuários com permissão de Aluno
    public static final String[] ENDPOINTS_ALUNO = {
        "/users/test/aluno",
        "/notas/aluno",                  // Visualizar suas notas
        "/disciplinas/"                 // Visualizar disciplinas disponíveis
    };

    // Endpoints acessíveis apenas por usuários com permissão de Professor
    public static final String[] ENDPOINTS_PROFESSOR = {
        "/users/test/professor",
        "/disciplinas/create",                  // Criar disciplina (POST)
        "/disciplinas/{id}",                   // Buscar disciplina por ID (GET)
        "/disciplinas/{id}",                  // Atualizar disciplina por ID (PUT)
        "/notas/create",                     // Criar nota (POST)
        "/notas/aluno",                     // Listar notas do aluno (GET)

    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable()) // Desativa a proteção contra CSRF
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(requests -> requests // Habilita a autorização para as requisições HTTP
                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED).authenticated()
                .requestMatchers(ENDPOINTS_PROFESSOR).hasRole("PROFESSOR") 
                .requestMatchers(ENDPOINTS_ALUNO).hasRole("ALUNO")
                .anyRequest().denyAll()).addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
