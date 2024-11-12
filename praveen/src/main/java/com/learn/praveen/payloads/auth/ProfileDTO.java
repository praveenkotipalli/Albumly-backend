package com.learn.praveen.payloads.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileDTO {

    private Long id;
    
    private String email;

    private String Authorities;
    
}
