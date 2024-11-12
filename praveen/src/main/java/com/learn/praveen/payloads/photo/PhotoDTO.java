package com.learn.praveen.payloads.photo;

// import io.swagger.v3.oas.annotations.media.Schema;
// import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
// import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDTO {
    
    
    private Long id;

    private String name;

    private String description;

    private String fileName;

    private String download_link;
}
