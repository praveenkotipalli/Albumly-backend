package com.learn.praveen.payloads.album;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoViewDTO {
    private Long id;
    private String name;
    private String description;
    private String fileName;

    private String download_link;
}
