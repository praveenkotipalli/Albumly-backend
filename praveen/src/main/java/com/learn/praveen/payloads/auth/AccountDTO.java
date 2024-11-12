package com.learn.praveen.payloads.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountDTO {

    @Email
    @Schema(description = "Email of the user", example = "praveen@gmail.com", requiredMode = RequiredMode.REQUIRED)
    private String email;

    @Size(min = 6, max = 20)
    @Schema(description = "Password of the user", example = "password", requiredMode = RequiredMode.REQUIRED, minLength = 6, maxLength = 20)    
    private String password;
}
