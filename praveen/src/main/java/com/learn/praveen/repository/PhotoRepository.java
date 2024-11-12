package com.learn.praveen.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learn.praveen.model.Photo;

public interface PhotoRepository  extends JpaRepository<Photo, Long> {
    
    // public void save(Photo photo);
    List<Photo> findByAlbum_id(Long album_id);

    Optional<Photo> findByAlbum_idAndId(Long album_id, Long id);
}
