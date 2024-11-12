package com.learn.praveen.payloads.album;

import java.util.List;

// import java.awt.List;

import com.learn.praveen.model.Account;
// import com.learn.praveen.model.Album;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
// import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AlbumViewDTO {

    private Long id;

    @NotBlank
    @Schema(description = "Name of the album", example = "Album 1", requiredMode = RequiredMode.REQUIRED)
    private String name;
    
    @NotBlank
    @Schema(description = "Description of the album", example = "This is a description",  requiredMode = RequiredMode.REQUIRED)
    private String description;

    private String account_mail;

    private List<?> photos;
    

    // @AllArgsConstructor
    // public AlbumViewDTO(Long id, String name, String description, Account account, List<PhotoDTO> photos) {
    //     this.id = id;
    //     this.name = name;
    //     this.description = description;
    //     this.account = account;
    //     this.photos = photos;
    // }

    // Remove the duplicate constructor

    // private List<PhotoDTO> photos;

    
    
}
