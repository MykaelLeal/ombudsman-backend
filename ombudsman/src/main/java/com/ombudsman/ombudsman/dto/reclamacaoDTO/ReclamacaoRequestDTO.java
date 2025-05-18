package com.ombudsman.ombudsman.dto.reclamacaoDTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReclamacaoRequestDTO {
    private String titulo;
    private String descricao;
    private LocalDateTime dataCriacao;
}

