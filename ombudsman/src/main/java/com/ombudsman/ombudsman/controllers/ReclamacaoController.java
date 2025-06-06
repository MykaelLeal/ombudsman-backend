package com.ombudsman.ombudsman.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ombudsman.ombudsman.dtos.manifestacoesDto.RequestDto;
import com.ombudsman.ombudsman.dtos.responseMessageDto.ResponseDto;
import com.ombudsman.ombudsman.entities.Reclamacao;
import com.ombudsman.ombudsman.services.ReclamacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reclamacoes")
public class ReclamacaoController {

    @Autowired
    private ReclamacaoService reclamacaoService;


    @Operation(summary = "Criar uma reclamação", description = "Cria uma nova reclamação com título, descrição e data atual.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reclamação criada com sucesso."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDto<Reclamacao>> createReclamacao(@Valid @RequestBody RequestDto reclamacaoDTO) {
        Reclamacao reclamacao = reclamacaoService.createReclamacao(reclamacaoDTO);

        if (reclamacao == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto<>("Dados inválidos.", null));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ResponseDto<>("Reclamação criada com sucesso.", reclamacao));
    }


    @Operation(summary = "Listar todas as reclamações", description = "Retorna a lista de todas as reclamações.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reclamações listadas com sucesso."),
        @ApiResponse(responseCode = "404", description = "Nenhuma reclamação encontrada.")
    })
    @GetMapping("/")
    public ResponseEntity<ResponseDto<List<Reclamacao>>> getAllReclamacoes() {
        List<Reclamacao> reclamacoes = reclamacaoService.getAllReclamacoes();

        if (reclamacoes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto<>("Nenhuma reclamação encontrada.", reclamacoes));
        }
        return ResponseEntity.ok(new ResponseDto<>("Reclamações listadas com sucesso.", reclamacoes));
    }


    @Operation(summary = "Buscar reclamação por ID", description = "Retorna uma reclamação específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reclamação encontrada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Reclamação não encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Reclamacao>> getReclamacaoById(@PathVariable Long id) {
        Optional<Reclamacao> reclamacaoOpt = reclamacaoService.findById(id);

        return reclamacaoOpt.map(reclamacao ->
            ResponseEntity.ok(new ResponseDto<>("Reclamação encontrada com sucesso.", reclamacao))
        ).orElseGet(() ->
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto<>("Reclamação não encontrada.", null))
        );
    }


    @Operation(summary = "Atualizar uma reclamação", description = "Atualiza os dados de uma reclamação específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reclamação atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Reclamação não encontrada para atualizar.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<Reclamacao>> updateReclamacao(@PathVariable Long id, @RequestBody RequestDto reclamacaoDTO) {
        try {
            Reclamacao reclamacaoAtualizada = reclamacaoService.updateReclamacao(id, reclamacaoDTO);
            return ResponseEntity.ok(new ResponseDto<>("Reclamação atualizada com sucesso.", reclamacaoAtualizada));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto<>("Reclamação não encontrada para atualizar.", null));
        }
    }


    @Operation(summary = "Deletar uma reclamação", description = "Remove uma reclamação pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Reclamação deletada com sucesso.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<String>> deleteReclamacao(@PathVariable Long id) {
        try {
            reclamacaoService.deleteReclamacaoByID(id);
            return ResponseEntity.ok(new ResponseDto<>("Reclamação deletada com sucesso.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto<>(e.getMessage(), null));
        }
    }
    
}
