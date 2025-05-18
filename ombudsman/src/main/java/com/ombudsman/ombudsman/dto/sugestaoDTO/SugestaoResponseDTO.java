package com.ombudsman.ombudsman.dto.sugestaoDTO;

import com.ombudsman.ombudsman.entitie.Sugestao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SugestaoResponseDTO {
    private String mensagem;
    private Sugestao sugestao;
}