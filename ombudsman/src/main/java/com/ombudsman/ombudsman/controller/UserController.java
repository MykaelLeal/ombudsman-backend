package com.ombudsman.ombudsman.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ombudsman.ombudsman.dto.userDTO.CreateUserDto;
import com.ombudsman.ombudsman.dto.userDTO.LoginUserDTO;
import com.ombudsman.ombudsman.dto.userDTO.RecoveryJwtTokenDto;
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
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    // Buscar o usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
       User user = userService.getUserById(id);
       return ResponseEntity.ok(user);
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