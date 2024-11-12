package com.learn.praveen.payloads.album;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDTO {
    
    // private Long id;
    private String name;
    private String description;
    // private String fileName;

    // private String download_link;

}
