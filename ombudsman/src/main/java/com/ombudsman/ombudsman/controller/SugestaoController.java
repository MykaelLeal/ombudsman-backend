package com.ombudsman.ombudsman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ombudsman.ombudsman.dto.sugestaoDTO.SugestaoRequestDTO;
import com.ombudsman.ombudsman.dto.sugestaoDTO.SugestaoResponseDTO;
import com.ombudsman.ombudsman.entitie.Sugestao;
import com.ombudsman.ombudsman.service.SugestaoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sugestoes")
public class SugestaoController {

    @Autowired
    private SugestaoService sugestaoService;

    // Cadastrar sugestão
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


    // Listar todas as sugestões do usuário autenticado
    @GetMapping("/")
    public ResponseEntity<List<Sugestao>> getAllSugestoes() {
        List<Sugestao> sugestoes = sugestaoService.getAllSugestoes();
        return ResponseEntity.ok(sugestoes);
    }


    // Buscar sugestões por ID
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

    // Atualizar sugestão
    @PutMapping("/{id}")
    public ResponseEntity<Sugestao> updateSugestao(@PathVariable Long id, @RequestBody SugestaoRequestDTO sugestaoDTO) {
        Sugestao sugestaoAtualizada = sugestaoService.updateSugestao(id, sugestaoDTO.getTitulo(), sugestaoDTO.getDescricao());
        return ResponseEntity.ok(sugestaoAtualizada);
    }

    
    // Deletar sugestão
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSugestao(@PathVariable Long id) {
        sugestaoService.deleteSugestao(id);
        return ResponseEntity.ok("Sugestão deletada com sucesso.");
    }
}

