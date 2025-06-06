package com.ombudsman.ombudsman.dtos.responseMessageDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    
    private String message;
    private T data;
}
