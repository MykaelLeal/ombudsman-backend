package com.ombudsman.ombudsman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ombudsman.ombudsman.dto.sugestaoDTO.SugestaoRequestDTO;
import com.ombudsman.ombudsman.dto.sugestaoDTO.SugestaoResponseDTO;
import com.ombudsman.ombudsman.entitie.Sugestao;
import com.ombudsman.ombudsman.service.SugestaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    public ResponseEntity<SugestaoResponseDTO> createSugestao(@RequestBody SugestaoRequestDTO sugestaoDTO) {
        Sugestao sugestaoCriada = sugestaoService.createSugestao(
                sugestaoDTO.getTitulo(),
                sugestaoDTO.getDescricao(),
                sugestaoDTO.getDataCriacao()
        );
        SugestaoResponseDTO response = new SugestaoResponseDTO("Sugestão criada com sucesso.", sugestaoCriada);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


     @Operation(summary = "Listar todas as sugestões do usuário", description = "Retorna a lista de todas as sugestões cadastradas pelo usuário.")
     @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Sugestões não encontradas.")
    })
    @GetMapping("/")
    public ResponseEntity<List<Sugestao>> getAllSugestoes() {
        List<Sugestao> sugestoes = sugestaoService.getAllSugestoes();
        return ResponseEntity.ok(sugestoes);
    }


    @Operation(summary = "Buscar sugestão por ID", description = "Retorna uma sugestão específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sugestão encontrada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Sugestão não encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SugestaoResponseDTO> getSugestaoById(@PathVariable Long id) {
        Optional<Sugestao> sugestaoOpt = sugestaoService.findById(id);
        if (sugestaoOpt.isPresent()) {
            SugestaoResponseDTO response = new SugestaoResponseDTO("Sugestão encontrada com sucesso.", sugestaoOpt.get());
            return ResponseEntity.ok(response);
        } else {
            SugestaoResponseDTO response = new SugestaoResponseDTO("Sugestão não encontrada.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @Operation(summary = "Atualizar uma Sugestão", description = "Atualiza os dados de uma sugestão específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sugestão atualizada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Sugestão não encontrada.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Sugestao> updateSugestao(@PathVariable Long id, @RequestBody SugestaoRequestDTO sugestaoDTO) {
        Sugestao sugestaoAtualizada = sugestaoService.updateSugestao(id, sugestaoDTO.getTitulo(), sugestaoDTO.getDescricao());
        return ResponseEntity.ok(sugestaoAtualizada);
    }

    
    @Operation(summary = "Deletar uma sugestão", description = "Remove uma sugestão pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sugestão deletada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Sugestão não encontrada.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSugestao(@PathVariable Long id) {
        sugestaoService.deleteSugestao(id);
        return ResponseEntity.ok("Sugestão deletada com sucesso.");
    }
}

