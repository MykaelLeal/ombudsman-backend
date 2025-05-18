package com.ombudsman.ombudsman.dto.elogioDTO;

import com.ombudsman.ombudsman.entitie.Elogio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ElogioResponseDTO {
    private String mensagem;
    private Elogio elogio;
}

