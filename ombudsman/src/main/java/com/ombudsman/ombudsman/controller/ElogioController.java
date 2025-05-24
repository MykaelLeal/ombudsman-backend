package com.ombudsman.ombudsman.controller;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.ombudsman.ombudsman.dto.elogioDTO.ElogioRequestDTO;
import com.ombudsman.ombudsman.dto.elogioDTO.ElogioResponseDTO;
import com.ombudsman.ombudsman.entitie.Elogio;
import com.ombudsman.ombudsman.service.ElogioService;

@RestController
@RequestMapping("/elogios")
public class ElogioController {

    @Autowired
    private ElogioService elogioService;

    // Cadastrar elogio
    @PostMapping("/create")
    public ResponseEntity<ElogioResponseDTO> createElogio(@RequestBody ElogioRequestDTO elogioDTO) {
      Elogio elogio = elogioService.createElogio(elogioDTO.getTitulo(), elogioDTO.getDescricao(), LocalDateTime.now());
       ElogioResponseDTO response = new ElogioResponseDTO("Elogio criado com sucesso.", elogio);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // Listar todos os elogios do usuário autenticado
    @GetMapping("/")
    public ResponseEntity<List<Elogio>> getAllElogios() {
        List<Elogio> elogios = elogioService.getAllElogios();
        return ResponseEntity.ok(elogios);
    }


    // Busca elogio por ID
    @GetMapping("/{id}")
    public ResponseEntity<ElogioResponseDTO> getElogioById(@PathVariable Long id) {
        Optional<Elogio> elogioOpt = elogioService.findById(id);
        if (elogioOpt.isPresent()) {
            ElogioResponseDTO response = new ElogioResponseDTO("Elogio encontrado com sucesso.", elogioOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ElogioResponseDTO response = new ElogioResponseDTO("Elogio não encontrado.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    // Atualizar elogio
    @PutMapping("/{id}")
    public ResponseEntity<ElogioResponseDTO> updateElogio(@PathVariable Long id, @RequestBody ElogioRequestDTO elogioDTO) {
        Elogio elogioAtualizado = elogioService.updateElogio(id, elogioDTO.getTitulo(), elogioDTO.getDescricao());
        ElogioResponseDTO response = new ElogioResponseDTO("Elogio atualizado com sucesso.", elogioAtualizado);
        return ResponseEntity.ok(response);
    }


    // Deletar elogio
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteElogio(@PathVariable Long id) {
        elogioService.deleteElogioByID(id);
        Map<String, String> msg = new HashMap<>();
        msg.put("mensagem", "Elogio deletado com sucesso.");
        return ResponseEntity.ok(msg);
    }
    
}