package com.ombudsman.ombudsman.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ombudsman.ombudsman.dto.userDTO.CreateUserDto;
import com.ombudsman.ombudsman.dto.userDTO.LoginUserDTO;
import com.ombudsman.ombudsman.dto.userDTO.RecoveryJwtTokenDto;
import com.ombudsman.ombudsman.dto.userDTO.UserRequestDTO;
import com.ombudsman.ombudsman.dto.userDTO.UserResponseDTO;
import com.ombudsman.ombudsman.entitie.User;
import com.ombudsman.ombudsman.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Faz a autenticação do Usuário
    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@RequestBody LoginUserDTO loginUserDto) {
        RecoveryJwtTokenDto token = userService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


    // Cria o Usuário
    @PostMapping
    public ResponseEntity<Map<String, String>> createUser(@RequestBody CreateUserDto createUserDto) {
        userService.createUser(createUserDto);
        Map<String, String> msg = new HashMap<>();
        msg.put("mensagem", "Usuário criado com sucesso.");
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }


    // Buscar todos os usuários
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    // Buscar o usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO userDTO) {
        User userAtualizado = userService.updateUser(id, userDTO.getNome(), userDTO.getEmail(), userDTO.getSenha());
        UserResponseDTO response = new UserResponseDTO("Usuário atualizado com sucesso.", userAtualizado);
        return ResponseEntity.ok(response);
    }


    // Deletar usuário por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        Map<String, String> msg = new HashMap<>();
        msg.put("mensagem", "Usuário deletado com sucesso.");
        return ResponseEntity.ok(msg);
    }

}