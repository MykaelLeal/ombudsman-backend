package com.ombudsman.ombudsman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ombudsman.ombudsman.dto.RequestDTO;
import com.ombudsman.ombudsman.dto.ResponseDTO;
import com.ombudsman.ombudsman.entitie.Reclamacao;
import com.ombudsman.ombudsman.service.ReclamacaoService;

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
    public ResponseEntity<ResponseDTO<Reclamacao>> createElogio(@Valid @RequestBody RequestDTO reclamacaoDTO) {
        Reclamacao reclamacao = reclamacaoService.createReclamacao(
            reclamacaoDTO.getTitulo(), 
            reclamacaoDTO.getDescricao(), 
            reclamacaoDTO.getData());

        if (reclamacao == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO<>(400, "Dados inválidos.", null));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ResponseDTO<>(201, "Reclamação criada com sucesso.", reclamacao));
    }



    @Operation(summary = "Listar todas as reclamações do usuário", description = "Retorna a lista de todas as reclamações cadastradas pelo usuário.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reclamações listadas com sucesso."),
        @ApiResponse(responseCode = "404", description = "Nenhuma reclamação encontrada.")
    })
    @GetMapping("/")
    public ResponseEntity<ResponseDTO<List<Reclamacao>>> getAllReclamacoes() {
       List<Reclamacao> reclamacoes = reclamacaoService.getAllReclamacoes();

        if (reclamacoes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO<>(404, "Nenhuma reclamação encontrada.", reclamacoes));
        }
        return ResponseEntity.ok(new ResponseDTO<>(200, "Reclamações listadas com sucesso.", reclamacoes));
    }



    @Operation(summary = "Buscar reclamação por ID", description = "Retorna uma reclamação específica pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reclamação encontrada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Reclamação não encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Reclamacao>> getReclamacaoById(@PathVariable Long id) {
        Optional<Reclamacao> reclamacaoOpt = reclamacaoService.findById(id);

        if (reclamacaoOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO<>(200, "Reclamação encontrada com sucesso.", reclamacaoOpt.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO<>(404, "Reclamação não encontrada.", null));
    }



    @Operation(summary = "Atualizar uma reclamação", description = "Atualiza os dados de uma reclamação específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reclamação atualizada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Reclamação não encontrada para atualizar.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<Reclamacao>> updateElogio(@PathVariable Long id, @RequestBody RequestDTO reclamacaoDTO) {
        Reclamacao reclamacaoAtualizada = reclamacaoService.updateReclamacao(
            id,
            reclamacaoDTO.getTitulo(),
            reclamacaoDTO.getDescricao()
        );

        if (reclamacaoAtualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO<>(404, "Reclamação não encontrada para atualizar.", null));
        }
        return ResponseEntity.status(HttpStatus.OK)
               .body(new ResponseDTO<>(200, "Reclamação atualizada com sucesso.", reclamacaoAtualizada));
               
    }



    @Operation(summary = "Deletar uma reclamação", description = "Remove uma reclamação pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Reclamação deletada com sucesso.")
   @DeleteMapping("/{id}")
   public ResponseEntity<ResponseDTO<String>> deleteReclamacao(@PathVariable Long id) {
        try {
            reclamacaoService.deleteReclamacao(id);
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO<>(200, "Reclamação deletada com sucesso.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO<>(404, e.getMessage(), null));
        }
    }


}

