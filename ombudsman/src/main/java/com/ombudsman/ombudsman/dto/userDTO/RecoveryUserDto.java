package com.ombudsman.ombudsman.dto.userDTO;

import java.util.List;

import com.ombudsman.ombudsman.enums.RoleName;

public record RecoveryUserDto(

        Long id,
        String email,
        List<RoleName> roles

) {
}
