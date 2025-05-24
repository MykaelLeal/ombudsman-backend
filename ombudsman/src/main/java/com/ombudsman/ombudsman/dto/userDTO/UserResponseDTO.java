package com.ombudsman.ombudsman.dto.userDTO;

import com.ombudsman.ombudsman.entitie.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDTO {
    private String mensagem;
    private User user;

}

