package com.learn.praveen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.learn.praveen.config.RsaKeyProperties;

import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.*;


@SpringBootApplication
@CrossOrigin(origins = "*")
@EnableConfigurationProperties(RsaKeyProperties.class)
@SecurityScheme(name = "praveen-api", scheme = "bearer", type = SecuritySchemeType.HTTP,  in = SecuritySchemeIn.HEADER)	
public class PraveenApplication {

	public static void main(String[] args) {
		SpringApplication.run(PraveenApplication.class, args);
	}
}
