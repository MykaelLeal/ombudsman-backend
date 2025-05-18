package com.ombudsman.ombudsman.dto.elogioDTO;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElogioRequestDTO {
    private String titulo;
    private String descricao;
    private LocalDateTime dataCriacao;
}



  