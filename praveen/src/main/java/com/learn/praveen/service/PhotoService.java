package com.learn.praveen.service;

import java.util.List;
// import java.awt.List;
// import java.lang.StackWalker.Option;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.praveen.model.Photo;
import com.learn.praveen.repository.PhotoRepository;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public Photo save(Photo photo) {
        return photoRepository.save(photo);

    }

    public Optional<Photo> findById(Long id) {
        return photoRepository.findById(id);
    }

    public List<Photo> findByAlbumId(Long album_id) {
        return photoRepository.findByAlbum_id(album_id);
    }

    public Optional<Photo> findByAlbumIdAndId(Long album_id, Long id) {
        return photoRepository.findByAlbum_idAndId(album_id, id);
    }
    
    public void deleteById(Long id) {
        photoRepository.deleteById(id);
    }
}
