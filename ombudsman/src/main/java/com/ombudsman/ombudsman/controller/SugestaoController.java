package com.ombudsman.ombudsman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ombudsman.ombudsman.dto.RequestDTO;
import com.ombudsman.ombudsman.dto.ResponseDTO;
import com.ombudsman.ombudsman.entitie.Sugestao;
import com.ombudsman.ombudsman.service.SugestaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sugestoes")
public class SugestaoController {

    @Autowired
    private SugestaoService sugestaoService;

   
    @Operation(summary = "Criar uma sugestão", description = "Cria uma nova sugestão com título, descrição e data atual.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sugestão criada com sucesso."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<Sugestao>> createSugestao(@Valid @RequestBody RequestDTO sugestaoDTO) {
        Sugestao sugestao = sugestaoService.createSugestao(
            sugestaoDTO.getTitulo(),
            sugestaoDTO.getDescricao(),
            sugestaoDTO.getData()
        );

        if (sugestao == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO<>(400, "Dados inválidos.", null));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ResponseDTO<>(201, "Sugestão criada com sucesso.", sugestao));
    }



    @Operation(summary = "Listar todas as sugestões do usuário", description = "Retorna a lista de todas as sugestões cadastradas pelo usuário.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sugestões listadas com sucesso."),
        @ApiResponse(responseCode = "404", description = "Nenhuma sugestão encontrada.")
    })
    @GetMapping("/")
    public ResponseEntity<ResponseDTO<List<Sugestao>>> getAllSugestoes() {
        List<Sugestao> sugestoes = sugestaoService.getAllSugestoes();

        if (sugestoes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO<>(404, "Nenhuma sugestão encontrada.", sugestoes));
        }
        return ResponseEntity.ok(new ResponseDTO<>(200, "Sugestões listadas com sucesso.", sugestoes));
    }



    @Operation(summary = "Buscar sugestão por ID", description = "Retorna uma sugestão específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sugestão encontrada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Sugestão não encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Sugestao>> getSugestaoById(@PathVariable Long id) {
        Optional<Sugestao> sugestaoOpt = sugestaoService.findById(id);

        if (sugestaoOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO<>(200, "Sugestão encontrada com sucesso.", sugestaoOpt.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ResponseDTO<>(404, "Sugestão não encontrada.", null));
    }



    @Operation(summary = "Atualizar uma sugestão", description = "Atualiza os dados de uma sugestão específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sugestão atualizada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Sugestão não encontrada para atualizar.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<Sugestao>> updateSugestao(@PathVariable Long id, @RequestBody RequestDTO sugestaoDTO) {
        Sugestao sugestaoAtualizada = sugestaoService.updateSugestao(
            id,
            sugestaoDTO.getTitulo(),
            sugestaoDTO.getDescricao()
        );

        if (sugestaoAtualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO<>(404, "Sugestão não encontrada para atualizar.", null));
        }
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseDTO<>(200, "Sugestão atualizada com sucesso.", sugestaoAtualizada));
    }



    @Operation(summary = "Deletar uma sugestão", description = "Remove uma sugestão pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Sugestão deletada com sucesso.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteSugestao(@PathVariable Long id) {
        try {
            sugestaoService.deleteSugestao(id);
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO<>(200, "Sugestão deletada com sucesso.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO<>(404, e.getMessage(), null));
        }
    }

}