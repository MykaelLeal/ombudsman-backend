package com.ombudsman.ombudsman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ombudsman.ombudsman.dto.reclamacaoDTO.ReclamacaoRequestDTO;
import com.ombudsman.ombudsman.dto.reclamacaoDTO.ReclamacaoResponseDTO;
import com.ombudsman.ombudsman.entitie.Reclamacao;
import com.ombudsman.ombudsman.service.ReclamacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    public ResponseEntity<ReclamacaoResponseDTO> createReclamacao(@RequestBody ReclamacaoRequestDTO reclamacaoDTO) {
        Reclamacao reclamacaoCriada = reclamacaoService.createReclamacao(
                reclamacaoDTO.getTitulo(),
                reclamacaoDTO.getDescricao(),
                reclamacaoDTO.getDataCriacao()
        );
        ReclamacaoResponseDTO response = new ReclamacaoResponseDTO("Reclamação criada com sucesso.", reclamacaoCriada);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Listar todas as reclamações do usuário", description = "Retorna a lista de todas as reclamações cadastradas pelo usuário.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Reclamações não encontradas.")
    })
    @GetMapping("/")
    public ResponseEntity<List<Reclamacao>> getAllReclamacoes() {
        List<Reclamacao> reclamacoes = reclamacaoService.getAllReclamacoes();
        return ResponseEntity.ok(reclamacoes);
    }


    @Operation(summary = "Buscar reclamação por ID", description = "Retorna uma reclamação específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reclamação encontrada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Reclamação não encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReclamacaoResponseDTO> getReclamacaoById(@PathVariable Long id) {
        Optional<Reclamacao> reclamacaoOpt = reclamacaoService.findById(id);
        if (reclamacaoOpt.isPresent()) {
            ReclamacaoResponseDTO response = new ReclamacaoResponseDTO("Reclamação encontrada com sucesso.", reclamacaoOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ReclamacaoResponseDTO response = new ReclamacaoResponseDTO("Reclamação não encontrada.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @Operation(summary = "Atualizar uma reclamação", description = "Atualiza os dados de uma reclamação específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reclamação atualizada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Reclamação não encontrada.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Reclamacao> updateReclamacao(@PathVariable Long id, @RequestBody ReclamacaoRequestDTO reclamacaoDTO) {
        Reclamacao reclamacaoAtualizada = reclamacaoService.updateReclamacao(id, reclamacaoDTO.getTitulo(), reclamacaoDTO.getDescricao());
        return ResponseEntity.ok(reclamacaoAtualizada);
    }


    @Operation(summary = "Deletar uma reclamação", description = "Remove uma reclamação pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reclamação deletada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Reclamação não encontrada.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReclamacao(@PathVariable Long id) {
        reclamacaoService.deleteReclamacao(id);
        return ResponseEntity.ok("Reclamação deletada com sucesso.");
    }
}

