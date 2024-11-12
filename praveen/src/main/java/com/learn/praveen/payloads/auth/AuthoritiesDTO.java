package com.learn.praveen.payloads.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AuthoritiesDTO {

    @NotBlank
    @Schema(description = "Authorities of the user", example = "USER", requiredMode = RequiredMode.REQUIRED)
    private String authorities;
    
}
