package com.sion.javamsauser.model.ResDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenStorageRequest {
    private String token;
    private Long userId;
}