package com.ombudsman.ombudsman.controllers;

import java.util.List;
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

import com.ombudsman.ombudsman.dtos.responseMessageDto.ResponseDto;
import com.ombudsman.ombudsman.dtos.userDto.LoginRecordDto;
import com.ombudsman.ombudsman.dtos.userDto.RecoveryJwtTokenDto;
import com.ombudsman.ombudsman.dtos.userDto.UserRecordDto;
import com.ombudsman.ombudsman.entities.User;
import com.ombudsman.ombudsman.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @Operation(summary = "Autentica o usuário e retorna o token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso."),
        @ApiResponse(responseCode = "400", description = "Credenciais inválidas.")
    })
   @PostMapping("/login")
    public ResponseEntity<ResponseDto<RecoveryJwtTokenDto>> authenticateUser(@RequestBody @Valid LoginRecordDto loginRecordDto) {
        RecoveryJwtTokenDto token = userService.authenticateUser(loginRecordDto);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto<>("Credenciais inválidas.", null));
        }

         return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>("Usuário autenticado com sucesso!", token));

    }


    @Operation(summary = "Criar um usuário", description = "Cria um novo usuário com nome, email e senha.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    @PostMapping
    public ResponseEntity<ResponseDto<User>> createUser(@RequestBody @Valid UserRecordDto userRecordDto) {
        User createdUser = userService.createUser(userRecordDto);

        var response = new ResponseDto<>("Usuário criado com sucesso.", createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Listar todos os usuários", description = "Retorna a lista de todos os usuários cadastrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso."),
        @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado.")
    })
    @GetMapping("/")
    public ResponseEntity<ResponseDto<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto<>("Nenhum usuário encontrado.", users));
        }

        return ResponseEntity.ok(new ResponseDto<>("Usuários listados com sucesso.", users));
    }


    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<User>> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.findById(id);

        return userOpt.map(user ->
            ResponseEntity.ok(new ResponseDto<>("Usuário encontrado com sucesso.", user))
        ).orElseGet(() ->
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto<>("Usuário não encontrado.", null))
        );
    }


    @Operation(summary = "Atualizar um usuário", description = "Atualiza os dados de um usuário específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<User>> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserRecordDto userRecordDto) {

        try {
            User userAtualizado = userService.updateUser(id, userRecordDto);
            return ResponseEntity.ok(new ResponseDto<>("Usuário atualizado com sucesso.", userAtualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto<>("Usuário não encontrado.", null));
        }
    }


   @Operation(summary = "Deletar um usuário", description = "Remove um usuário pelo seu ID.")
   @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso.")
   @DeleteMapping("/{id}")
   public ResponseEntity<ResponseDto<String>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>("Usuário deletado com sucesso.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto<>(e.getMessage(), null));
        }
    }

}
