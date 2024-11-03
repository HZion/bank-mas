package com.sion.javamsaredis.ResDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidateResponse {
    private boolean valid;
    private Long userId;
}