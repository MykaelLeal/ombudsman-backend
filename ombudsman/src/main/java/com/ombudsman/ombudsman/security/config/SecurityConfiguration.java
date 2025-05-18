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
        "/users/login",       // Login de usuário
        "/users"              // Criação de novo usuário
    };

   // Endpoints que requerem autenticação para serem acessados (usuário logado)
   public static final String[] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = {

    // Elogios
    "/api/elogios",             // Listar elogios (GET)
    "/api/elogios/create",      // Criar elogio (POST)
    "/api/elogios/{id}",        // Buscar elogio por ID (GET)
    "/api/elogios/{id}",        // Atualizar elogio (PUT)
    "/api/elogios/{id}",        // Deletar elogio (DELETE)

    // Sugestões
    "/api/sugestoes",             // Listar sugestões (GET)
    "/api/sugestoes/create",      // Criar sugestão (POST)
    "/api/sugestoes/{id}",        // Buscar sugestão por ID (GET)
    "/api/sugestoes/{id}",        // Atualizar sugestão (PUT)
    "/api/sugestoes/{id}",        // Deletar sugestão (DELETE)

    // Reclamações
    "/api/reclamacoes",             // Listar reclamações (GET)
    "/api/reclamacoes/create",      // Criar reclamação (POST)
    "/api/reclamacoes/{id}",        // Buscar reclamação por ID (GET)
    "/api/reclamacoes/{id}",        // Atualizar reclamação (PUT)
    "/api/reclamacoes/{id}",        // Deletar reclamação (DELETE)
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
