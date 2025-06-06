package com.ombudsman.ombudsman.services;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ombudsman.ombudsman.dtos.userDto.LoginRecordDto;
import com.ombudsman.ombudsman.dtos.userDto.RecoveryJwtTokenDto;
import com.ombudsman.ombudsman.dtos.userDto.UserRecordDto;
import com.ombudsman.ombudsman.entities.User;
import com.ombudsman.ombudsman.repositories.UserRepository;
import com.ombudsman.ombudsman.security.auth.JwtTokenService;
import com.ombudsman.ombudsman.security.config.SecurityConfiguration;
import com.ombudsman.ombudsman.security.userdetails.UserDetailsImpl;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    // Método responsável por autenticar um usuário e retornar um token JWT
    public RecoveryJwtTokenDto authenticateUser(LoginRecordDto loginUserDto) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Gera um token JWT para o usuário autenticado
        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }
    

    @Transactional
    public User createUser(UserRecordDto createUserDto) {
        // Verifica se o email já existe
        if (userRepository.existsByEmail(createUserDto.email())) {
            throw new RuntimeException("Email já cadastrado.");
        }

        // Cria um novo usuário com os dados fornecidos
        User newUser = User.builder()
            .nome(createUserDto.name())
            .email(createUserDto.email())
            .password(securityConfiguration.passwordEncoder().encode(createUserDto.password()))
            .build();

        // Salva o novo usuário no banco de dados
        return userRepository.save(newUser);
    }


    // Método Responsavel por buscar todos os usuários
    public List<User> getAllUsers() {
        return userRepository.findAll();

    }

    
    // Método Responsavel por buscar os usuários por ID
    public Optional<User> findById(Long id) {
      return userRepository.findById(id);
    }


    // Busca usuários autenticados
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() 
            || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("Usuário não autenticado.");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow();
    }


    // Método responsavel por atualizar o usuário por ID
    @Transactional
    public User updateUser(Long userId, UserRecordDto userRecordDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        user.setNome(userRecordDto.name());
        user.setEmail(userRecordDto.email());
        // Codifica a senha antes de salvar
        String senhaCodificada = securityConfiguration.passwordEncoder().encode(userRecordDto.password());
        user.setPassword(senhaCodificada);

        return userRepository.save(user);
    }



    // Deletar usuário por ID
    @Transactional
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado.");
        }
        userRepository.deleteById(id);
    }


}
