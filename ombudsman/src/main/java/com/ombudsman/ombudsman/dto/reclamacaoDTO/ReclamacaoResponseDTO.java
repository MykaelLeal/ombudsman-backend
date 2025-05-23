package com.ombudsman.ombudsman.dto.reclamacaoDTO;

import com.ombudsman.ombudsman.entitie.Reclamacao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReclamacaoResponseDTO {
    private String mensagem;
    private Reclamacao reclamacao;
}
