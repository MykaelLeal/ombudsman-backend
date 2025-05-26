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

import com.ombudsman.ombudsman.security.auth.UserAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserAuthenticationFilter userAuthenticationFilter;

    // Endpoints que NÃO requerem autenticação para serem acessados (públicos)
    public static final String[] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
        "/users/login",          // Login de usuário
        "/users",               // Criação de novo usuário
        "/users/",             // Listar todos os usuários
        "/users/*",           // Buscar usuários por ID
        "/users/*",          //  Deletar usuário por ID

        // Swagger
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html"

                     
    };

   // Endpoints que requerem autenticação para serem acessados (usuário logado)
   public static final String[] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = {

    // Elogios
    "/elogios/",               // Listar elogios (GET)
    "/elogios/create",        // Criar elogio (POST)
    "/elogios/*",            // Buscar elogio por ID (GET)
    "/elogios/*",           // Atualizar elogio (PATCH)
    "/elogios/*",          // Deletar elogio (DELETE)

    // Sugestões
    "/sugestoes/",             // Listar sugestões (GET)
    "/sugestoes/create",      // Criar sugestão (POST)
    "/sugestoes/*",          // Buscar sugestão por ID (GET)
    "/sugestoes/*",         // Atualizar sugestão (PATCH)
    "/sugestoes/*",        // Deletar sugestão (DELETE)

    // Reclamações
    "/reclamacoes/",             // Listar reclamações (GET)
    "/reclamacoes/create",      // Criar reclamação (POST)
    "/reclamacoes/*",          // Buscar reclamação por ID (GET)
    "/reclamacoes/*",         // Atualizar reclamação (PATCH)
    "/reclamacoes/*",        // Deletar reclamação (DELETE)
};



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable()) // Desativa a proteção contra CSRF
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(requests -> requests // Habilita a autorização para as requisições HTTP
                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED).authenticated()
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
