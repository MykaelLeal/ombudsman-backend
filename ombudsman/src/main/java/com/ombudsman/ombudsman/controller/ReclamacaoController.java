package com.ombudsman.ombudsman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ombudsman.ombudsman.dto.reclamacaoDTO.ReclamacaoRequestDTO;
import com.ombudsman.ombudsman.dto.reclamacaoDTO.ReclamacaoResponseDTO;
import com.ombudsman.ombudsman.entitie.Reclamacao;
import com.ombudsman.ombudsman.service.ReclamacaoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reclamacoes")
public class ReclamacaoController {

    @Autowired
    private ReclamacaoService reclamacaoService;

    // Cadastrar reclamação
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


    // Listar todas as reclamações do usuário autenticado
    @GetMapping("/")
    public ResponseEntity<List<Reclamacao>> getAllReclamacoes() {
        List<Reclamacao> reclamacoes = reclamacaoService.getAllReclamacoes();
        return ResponseEntity.ok(reclamacoes);
    }


    // Busca reclamação por ID
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


    // Atualizar reclamação
    @PutMapping("/{id}")
    public ResponseEntity<Reclamacao> updateReclamacao(@PathVariable Long id, @RequestBody ReclamacaoRequestDTO reclamacaoDTO) {
        Reclamacao reclamacaoAtualizada = reclamacaoService.updateReclamacao(id, reclamacaoDTO.getTitulo(), reclamacaoDTO.getDescricao());
        return ResponseEntity.ok(reclamacaoAtualizada);
    }


    // Deletar reclamação
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReclamacao(@PathVariable Long id) {
        reclamacaoService.deleteReclamacao(id);
        return ResponseEntity.ok("Reclamação deletada com sucesso.");
    }
}

