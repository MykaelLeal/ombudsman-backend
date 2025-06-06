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

import com.ombudsman.ombudsman.dtos.manifestacoesDto.RequestDto;
import com.ombudsman.ombudsman.dtos.responseMessageDto.ResponseDto;
import com.ombudsman.ombudsman.entities.Elogio;
import com.ombudsman.ombudsman.services.ElogioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/elogios")
public class ElogioController {

    @Autowired
    private ElogioService elogioService;


    @Operation(summary = "Criar um elogio", description = "Cria um novo elogio com título, descrição e data atual.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Elogio criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
   @PostMapping("/create")
    public ResponseEntity<ResponseDto<Elogio>> createElogio(@Valid @RequestBody RequestDto elogioDTO) {
        Elogio elogio = elogioService.createElogio(elogioDTO);

        if (elogio == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto<>("Dados inválidos.", null));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ResponseDto<>("Elogio criado com sucesso.", elogio));
    }


    @Operation(summary = "Listar todos os elogios.", description = "Retorna a lista de todos os elogios.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Elogios listados com sucesso."),
        @ApiResponse(responseCode = "404", description = "Nenhum elogio encontrado.")
    })
    @GetMapping("/")
    public ResponseEntity<ResponseDto<List<Elogio>>> getAllElogios() {
       List<Elogio> elogios = elogioService.getAllElogios();

        if (elogios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto<>("Nenhum elogio encontrado.", elogios));
        }
        return ResponseEntity.ok(new ResponseDto<>("Elogios listados com sucesso.", elogios));
    }



    @Operation(summary = "Buscar elogio por ID", description = "Retorna um elogio específico pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Elogio encontrado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Elogio não encontrado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Elogio>> getElogioById(@PathVariable Long id) {
        Optional<Elogio> elogioOpt = elogioService.findById(id);

        return elogioOpt.map(elogio ->
            ResponseEntity.ok(new ResponseDto<>("Elogio encontrado com sucesso.", elogio))
        ).orElseGet(() ->
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto<>("Elogio não encontrado.", null))
        );
    }



    @Operation(summary = "Atualizar um elogio", description = "Atualiza os dados de um elogio específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Elogio atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Elogio não encontrado para atualizar.")
    })
   @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<Elogio>> updateElogio(@PathVariable Long id, @RequestBody RequestDto elogioDTO) {
        Elogio elogioAtualizado = elogioService.updateElogio(id, elogioDTO);

        if (elogioAtualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto<>("Elogio não encontrado para atualizar.", null));
        }
        return ResponseEntity.status(HttpStatus.OK)
               .body(new ResponseDto<>("Elogio atualizado com sucesso.", elogioAtualizado));
               
    }



    @Operation(summary = "Deletar um elogio", description = "Remove um elogio pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Elogio deletado com sucesso.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<String>> deleteElogio(@PathVariable Long id) {
        try {
            elogioService.deleteElogioByID(id);
            return ResponseEntity.ok(new ResponseDto<>("Elogio deletado com sucesso.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto<>(e.getMessage(), null));
        }
    }
    
}


