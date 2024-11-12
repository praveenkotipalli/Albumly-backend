package com.learn.praveen.controller;

import java.util.List;
// import java.security.Principal;
import java.util.Optional;

import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

// import org.hibernate.internal.util.collections.ConcurrentReferenceHashMap.Option;
// import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
// import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learn.praveen.model.Account;
import com.learn.praveen.model.Album;
import com.learn.praveen.model.Photo;

import java.io.File;
import org.springframework.http.HttpHeaders;
// import java.nio.Buffer;
import java.nio.file.Files;
import java.awt.image.BufferedImage;
import com.learn.praveen.payloads.album.AlbumDTO;
import com.learn.praveen.payloads.album.AlbumViewDTO;
// import com.learn.praveen.payloads.album.AlbumViewDTO;
import com.learn.praveen.payloads.auth.AccountViewDTO;
import com.learn.praveen.payloads.photo.PhotoDTO;
import com.learn.praveen.service.AccountService;
import com.learn.praveen.service.AlbumService;
import com.learn.praveen.service.PhotoService;
import com.learn.praveen.utils.AppUtils.AppUtils;
import com.learn.praveen.utils.constants.AlbumError;
// import com.learn.praveen.utils.constants.PhotoAddedSuccess;
import com.learn.praveen.utils.constants.AlbumSuccess;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/albums")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600) 
@Tag(name = "Album Controller", description = "Controller for Album management")
@Slf4j
public class AlbumController {

    static final String PHOTOS_FOLDER_NAME = "photos";
    static final String THUMBNAIL_FOLDER_NAME = "thumbnails";
    static final int THUMBNAIL_WIDTH = 300;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private AccountService  accountService;

    @Autowired
    private PhotoService photoService;


    @PostMapping(value = "/add", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "Album added successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Operation(summary = "Add a new album", description = "Add a new album to the system")
    @SecurityRequirement(name = "praveen-api")
    public ResponseEntity<?> addAlbum(@Valid @RequestBody AlbumDTO albumDTO, Authentication authentication) {
        try {

            Album album = new Album();
            album.setName(albumDTO.getName());
            album.setDescription(albumDTO.getDescription());
            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
           
            Account account = optionalAccount.get();
            album.setAccount(account);
            
            album = albumService.save(album);

            AccountViewDTO accountViewDTO = new AccountViewDTO(album.getId(), album.getName(), album.getDescription());  
            
            return ResponseEntity.ok(accountViewDTO);

        } catch (Exception e) {
            log.debug(AlbumError.ADD_ALBUM_ERROR+" "+e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } 
        
    }

    @GetMapping(value = "/", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "List of albums")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Operation(summary = "List of albums", description = "Add a new album to the system")
    @SecurityRequirement(name = "praveen-api")
    public List<AlbumViewDTO> albums(Authentication authentication) {

        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);

        Account account = optionalAccount.get();
        List<AlbumViewDTO> albums = new ArrayList<>();
        

        for(Album album: albumService.findByAccountId(account.getId())) { 

            List<PhotoDTO> photos = new ArrayList<>();

            for(Photo photo: photoService.findByAlbumId(album.getId())) {
                // PhotoDTO photoDTO = new PhotoDTO();
                // photoDTO.setId(photo.getId());
                // photoDTO.setName(photo.getName());
                // photoDTO.setDescription(photo.getDescription());
                // photoDTO.setFileName(photo.getFileName());
                // photoDTO.setDownload_link(link.replace("{album_id}", String.valueOf(album.getId())).replace("{photo_id}", String.valueOf(photo.getId())));
                String link = "/"+album.getId()+ "/photos/"+photo.getId()+"/download-photo";

                photos.add(new PhotoDTO(photo.getId(), photo.getName(), photo.getDescription(), photo.getFileName(), link ));

                
            }
            // AlbumViewDTO albumViewDTO = new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), album.getAccount(), photos);
            albums.add(new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(),album.getAccount().getEmail(), photos));
            
        }
        return albums;
    }


    @GetMapping(value = "/{album_id}", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "List of albums")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Operation(summary = "List of albums", description = "Add a new album to the system")
    @SecurityRequirement(name = "praveen-api")
    public List<AlbumViewDTO> album_by_id (Authentication authentication, @PathVariable("album_id") long album_id){
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();
        List<AlbumViewDTO> albums = new ArrayList<>();
        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album;
        if(optionalAlbum.isPresent()){
            album = optionalAlbum.get();
            if(album.getAccount().getId() != account.getId()){
                return albums;
            }
        } else {
            return albums;
        }

        List<PhotoDTO> photos = new ArrayList<>();

        for(Photo photo: photoService.findByAlbumId(album.getId())) {
            String link = "/"+album.getId()+ "/photos/"+photo.getId()+"/download-photo";
            photos.add(new PhotoDTO(photo.getId(), photo.getName(), photo.getDescription(), photo.getFileName(), link ));
        }

        albums.add(new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), album.getAccount().getEmail(), photos));
        return albums;
    }

    @GetMapping(value = "/admin", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "List of albums by admin")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Operation(summary = "List of albums by admin", description = "List of albums by admin")
    @SecurityRequirement(name = "praveen-api")
    public List<AlbumViewDTO> albums_admin(Authentication authentication) {
        List<AlbumViewDTO> albums = new ArrayList<>();

        for(Album album: albumService.findAll()) { 
            albums.add(new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), album.getAccount().getEmail(), null));   
        }
        return albums;
    }



    @PostMapping(value = "/{album_id}/upload-photos", consumes = {"multipart/form-data"})
    @Operation(summary = "Upload photo", description = "Upload photo to album")
    @SecurityRequirement(name = "praveen-api")
    public ResponseEntity<List<HashMap<String, List<String>>>> photos(@Valid PhotoDTO photoDTO ,@RequestPart(required = true) MultipartFile[] files, @PathVariable long album_id, Authentication authentication) {

        String email = authentication.getName();



        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();

        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album ;

        if(optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
            if(album.getAccount().getId() != account.getId()) {
                return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(null);
            }
        } 
        else {
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<String> fileNamesWithSuccess = new ArrayList<>();
        List<String> fileNamesWithErrors = new ArrayList<>();

        
        Arrays.asList(files).stream().forEach(file -> {
            String contentType = file.getContentType();
            if(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/jpg")) {
                fileNamesWithSuccess.add(file.getOriginalFilename());

                int length = 10;
                boolean useLetters = true;
                boolean useNumbers = false;


                try {
                    String fileName = file.getOriginalFilename();
                    String randomString = org.apache.commons.lang3.RandomStringUtils.random(length, useLetters, useNumbers);
                    String generatedString = randomString + file.getOriginalFilename();

                    String final_photo_name = generatedString;

                    String absolute_fileLocation = AppUtils.get_photo_upload_path(final_photo_name, PHOTOS_FOLDER_NAME, album_id);
                    Path path = Paths.get(absolute_fileLocation);

                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                    Photo photo = new Photo();
                    photo.setName(fileName);
                    photo.setDescription(photoDTO.getDescription());
                    photo.setFileName(final_photo_name);
                    photo.setOriginalFileName(file.getOriginalFilename());
                    photo.setAlbum(album);
                    photoService.save(photo);

                    BufferedImage thumbImg = AppUtils.getThumbnai(file, THUMBNAIL_WIDTH);

                    File thumbnail_location = new File(AppUtils.get_photo_upload_path(final_photo_name, THUMBNAIL_FOLDER_NAME, album_id));
                    ImageIO.write(thumbImg, file.getContentType().split("/")[1] , thumbnail_location);


                } catch (Exception e) {
                    log.debug(AlbumError.ADD_ALBUM_ERROR+" "+e.getMessage());
                    fileNamesWithErrors.add(file.getOriginalFilename());
                }
            } else {
                fileNamesWithErrors.add(file.getOriginalFilename());
            }
            
        });


        HashMap<String, List<String>> result = new HashMap<>();
        result.put("SUCCESS", fileNamesWithSuccess);
        result.put("ERRORS", fileNamesWithErrors);

        List<HashMap<String, List<String>>> response = new ArrayList<>();
        response.add(result);
        return ResponseEntity.ok(response);
    }


    @GetMapping(value = "/{album_id}/photos/{photo_id}/download-photo")
    @SecurityRequirement(name = "praveen-api")
    public ResponseEntity<?> downloadPhoto(@PathVariable("album_id") long album_id,
    @PathVariable("photo_id") long photo_id, Authentication authentication){

        return downloadFile(album_id, photo_id, PHOTOS_FOLDER_NAME,  authentication);

        // return null;
    }

    public ResponseEntity<?> downloadFile( long album_id, 
     long photo_id,String folder_name , Authentication authentication){

        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();

        Optional<Album> optionalAlbum = albumService.findById(album_id);

        Album album;

        if(optionalAlbum.isPresent()){
            album = optionalAlbum.get();
            if(album.getAccount().getId() != account.getId()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<Photo> optionalPhoto = photoService.findById(photo_id);
        
        if(optionalPhoto.isPresent()){
            Photo photo = optionalPhoto.get();
            Resource resource = null;

            try {
                resource = AppUtils.getFilesAsResource(album_id, folder_name, photo.getFileName());    
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }

            if(resource == null){
                // return ResponseEntity.ok(resource);
                return new ResponseEntity<>("file not found", HttpStatus.NOT_FOUND);
            }
            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + photo.getOriginalFileName() + "\"";

            return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
            .body(resource);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // return null;
        
    }

    @GetMapping(value = "/{album_id}/photos/{photo_id}/download-thumbnail")
    @SecurityRequirement(name = "praveen-api")
    public ResponseEntity<?> downloadThumbnail(@PathVariable("album_id") long album_id, 
    @PathVariable("photo_id") long photo_id, Authentication authentication){

        return downloadFile(album_id, photo_id, THUMBNAIL_FOLDER_NAME, authentication);
        
    }
    

    @PutMapping(value = "/{album_id}/photos/{photo_id}/update-photo",  consumes = {"multipart/form-data"})
    @Operation(summary = "Update photo", description = "Update photo to album")
    @SecurityRequirement(name = "praveen-api")
    public ResponseEntity<?> update_photo(@Valid PhotoDTO photoDTO ,
     @PathVariable long album_id, @PathVariable long photo_id, Authentication authentication) {
    
        String email = authentication.getName();



        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();

        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album ;

        Optional<Photo> optionalPhoto = photoService.findById(photo_id);
        Photo photo;

        if(optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
            if(album.getAccount().getId() != account.getId()) {
                return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(null);
            }
        } 
        else {
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST).body(null);
        }

        if(optionalPhoto.isPresent()) {
            photo = optionalPhoto.get();
            if(photo.getAlbum().getId() != album_id) {
                return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(null);
            }
        } 
        else {
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST).body(null);
        }

        photo.setName(photoDTO.getName());
        photo.setDescription(photoDTO.getDescription());

        
        photoService.save(photo);

        PhotoDTO photoDTO1 = new PhotoDTO(photo.getId(), photo.getName(), photo.getDescription(), photo.getFileName(), null);
        

        return ResponseEntity.ok(photoDTO1);
        
        // return ResponseEnctity.ok().build();
    }


    @PutMapping(value = "/{album_id}/update-album", produces = "application/json")
    @Operation(summary = "Update album", description = "Update album")
    @SecurityRequirement(name = "praveen-api")
    public ResponseEntity<?> update_album(@Valid AlbumDTO albumDTO, @PathVariable long album_id, Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();

        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album;

        if(optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
            if(album.getAccount().getId() != account.getId()) {
                return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(null);
            }
        } 
        else {
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST).body(null);
        }

        album = optionalAlbum.get();

        album.setName(albumDTO.getName());
        album.setDescription(albumDTO.getDescription());
        albumService.save(album);


        AccountViewDTO accountViewDTO = new AccountViewDTO(album.getId(), album.getName(), album.getDescription());  
            
        return ResponseEntity.ok(accountViewDTO);

    }


    @DeleteMapping(value = "/{album_id}/delete-album")
    @Operation(summary = "Delete album", description = "Delete album")
    @SecurityRequirement(name = "praveen-api")
    public ResponseEntity<?> delete_album(@PathVariable long album_id, Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();

        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album;

        if(optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
            if(album.getAccount().getId() != account.getId()) {
                return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(null);
            }
        } 
        else {
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST).body(null);
        }

        album = optionalAlbum.get();
        albumService.deleteById(album.getId());

        return ResponseEntity.ok(AlbumSuccess.ALBUM_DELETED);
    }


    @DeleteMapping(value = "/{album_id}/photos/{photo_id}/delete-photo")
    @Operation(summary = "Delete photo", description = "Delete photo")
    @SecurityRequirement(name = "praveen-api")
    public ResponseEntity<?> delete_photo(@PathVariable long album_id, @PathVariable long photo_id, Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();

        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album;

        if(optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
            if(album.getAccount().getId() != account.getId()) {
                return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(null);
            }
        } 
        else {
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<Photo> optionalPhoto = photoService.findById(photo_id);
        Photo photo;

        if(optionalPhoto.isPresent()) {
            photo = optionalPhoto.get();
            if(photo.getAlbum().getId() != album_id) {
                return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(null);
            }
        } 
        else {
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST).body(null);
        }

        photoService.deleteById(photo.getId());

        return ResponseEntity.ok(AlbumSuccess.PHOTO_DELETED);

    }

    
}
