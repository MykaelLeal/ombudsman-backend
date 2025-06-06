package com.ombudsman.ombudsman.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ombudsman.ombudsman.dtos.manifestacoesDto.RequestDto;
import com.ombudsman.ombudsman.dtos.responseMessageDto.ResponseDto;
import com.ombudsman.ombudsman.entities.Sugestao;
import com.ombudsman.ombudsman.services.SugestaoService;

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
    public ResponseEntity<ResponseDto<Sugestao>> createSugestao(@Valid @RequestBody RequestDto sugestaoDTO) {
        Sugestao sugestao = sugestaoService.createSugestao(sugestaoDTO);

        if (sugestao == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto<>("Dados inválidos.", null));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ResponseDto<>("Sugestão criada com sucesso.", sugestao));
    }

    @Operation(summary = "Listar todas as sugestões.", description = "Retorna a lista de todas as sugestões.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sugestões listadas com sucesso."),
        @ApiResponse(responseCode = "404", description = "Nenhuma sugestão encontrada.")
    })
    @GetMapping("/")
    public ResponseEntity<ResponseDto<List<Sugestao>>> getAllSugestoes() {
       List<Sugestao> sugestoes = sugestaoService.getAllSugestoes();

        if (sugestoes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto<>("Nenhuma sugestão encontrada.", sugestoes));
        }
        return ResponseEntity.ok(new ResponseDto<>("Sugestões listadas com sucesso.", sugestoes));
    }

    @Operation(summary = "Buscar sugestão por ID", description = "Retorna uma sugestão específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sugestão encontrada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Sugestão não encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Sugestao>> getSugestaoById(@PathVariable Long id) {
        Optional<Sugestao> sugestaoOpt = sugestaoService.findById(id);

        return sugestaoOpt.map(sugestao ->
            ResponseEntity.ok(new ResponseDto<>("Sugestão encontrada com sucesso.", sugestao))
        ).orElseGet(() ->
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto<>("Sugestão não encontrada.", null))
        );
    }

    @Operation(summary = "Atualizar uma sugestão", description = "Atualiza os dados de uma sugestão específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sugestão atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Sugestão não encontrada para atualizar.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<Sugestao>> updateSugestao(@PathVariable Long id, @RequestBody RequestDto sugestaoDTO) {
        Sugestao sugestaoAtualizada = sugestaoService.updateSugestao(id, sugestaoDTO);

        if (sugestaoAtualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto<>("Sugestão não encontrada para atualizar.", null));
        }
        return ResponseEntity.status(HttpStatus.OK)
               .body(new ResponseDto<>("Sugestão atualizada com sucesso.", sugestaoAtualizada));
    }

    @Operation(summary = "Deletar uma sugestão", description = "Remove uma sugestão pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Sugestão deletada com sucesso.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<String>> deleteSugestao(@PathVariable Long id) {
        try {
            sugestaoService.deleteSugestaoByID(id);
            return ResponseEntity.ok(new ResponseDto<>("Sugestão deletada com sucesso.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto<>(e.getMessage(), null));
        }
    }
}
