package com.ombudsman.ombudsman.dtos.manifestacoesDto;

import jakarta.validation.constraints.NotBlank;

public record RequestDto (

    @NotBlank(message = "O titulo é obrigatório.")
    String titulo,

    @NotBlank(message = "A descrição é obrigatória.")
    String descricao

) {}
