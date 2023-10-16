package org.groupscope.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    @NotEmpty
    private String jwtToken;

    @NotEmpty
    private String refreshToken;
}
