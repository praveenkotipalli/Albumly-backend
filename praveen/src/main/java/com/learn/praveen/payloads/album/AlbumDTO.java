package com.learn.praveen.payloads.album;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {

    @NotBlank
    @Schema(description = "Name of the album", example = "Album 1", requiredMode = RequiredMode.REQUIRED)
    private String name;
    
    @NotBlank
    @Schema(description = "Description of the album", example = "This is a description",  requiredMode = RequiredMode.REQUIRED)
    private String description;
    
}
