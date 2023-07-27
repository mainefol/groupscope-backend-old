package org.groupscope.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class AuthResponse {

    @NotEmpty
    private String jwtToken;
}
