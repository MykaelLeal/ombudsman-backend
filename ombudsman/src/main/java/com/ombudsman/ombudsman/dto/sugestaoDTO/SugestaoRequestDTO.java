package com.ombudsman.ombudsman.dto.sugestaoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SugestaoRequestDTO {
    private String titulo;
    private String descricao;
    private LocalDateTime dataCriacao;
}
