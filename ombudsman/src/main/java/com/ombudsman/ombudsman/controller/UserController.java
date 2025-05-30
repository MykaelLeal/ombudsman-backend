package com.ombudsman.ombudsman.controller;

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

import com.ombudsman.ombudsman.dto.userDTO.CreateUserDto;
import com.ombudsman.ombudsman.dto.userDTO.LoginUserDTO;
import com.ombudsman.ombudsman.dto.userDTO.RecoveryJwtTokenDto;
import com.ombudsman.ombudsman.dto.userDTO.UserRequestDTO;
import com.ombudsman.ombudsman.dto.ResponseDTO; 
import com.ombudsman.ombudsman.entitie.User;
import com.ombudsman.ombudsman.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


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
    public ResponseEntity<ResponseDTO<RecoveryJwtTokenDto>> authenticateUser(@RequestBody LoginUserDTO loginUserDto) {
        RecoveryJwtTokenDto token = userService.authenticateUser(loginUserDto);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO<>(400, "Credenciais inválidas.", null));
        }
        return ResponseEntity.ok(new ResponseDTO<>(200, "Autenticação realizada com sucesso.", token));
    }



    @Operation(summary = "Criar um usuário", description = "Cria um novo usuário com nome, email e senha.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    @PostMapping
    public ResponseEntity<ResponseDTO<User>> createUser(@RequestBody CreateUserDto createUserDto) {
        User usuario = userService.createUser(createUserDto);

        if(usuario == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO<>(400, "Dados inválidos.", null));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ResponseDTO<>(201, "Usuário criado com sucesso.", null));
    }



    @Operation(summary = "Listar todos os usuários", description = "Retorna a lista de todos os usuários cadastrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso."),
        @ApiResponse(responseCode = "404", description = "Usuários não encontrados.")
    })
    @GetMapping("/")
    public ResponseEntity<ResponseDTO<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO<>(404, "Nenhum usuário encontrado.", users));
        }
        return ResponseEntity.ok(new ResponseDTO<>(200, "Usuários listados com sucesso.", users));
    }



    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<User>> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.findById(id);

        if (userOpt.isPresent()) {
            return ResponseEntity.ok(new ResponseDTO<>(200, "Usuário encontrado com sucesso.", userOpt.get()));
        }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO<>(404, "Usuário não encontrado.", null));
        }
    


    @Operation(summary = "Atualizar um usuário", description = "Atualiza os dados de um usuário específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<User>> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO userDTO) {
        User userAtualizado = userService.updateUser(id, userDTO.getNome(), userDTO.getEmail(), userDTO.getSenha());
        if (userAtualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO<>(404, "Usuário não encontrado.", null));
        }
        return ResponseEntity.ok(new ResponseDTO<>(200, "Usuário atualizado com sucesso.", userAtualizado));
    }



   @Operation(summary = "Deletar um usuário", description = "Remove um usuário pelo seu ID.")
   @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso.")
   @DeleteMapping("/{id}")
   public ResponseEntity<ResponseDTO<String>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO<>(200, "Usuário deletado com sucesso.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO<>(404, e.getMessage(), null));
        }
    }


}
