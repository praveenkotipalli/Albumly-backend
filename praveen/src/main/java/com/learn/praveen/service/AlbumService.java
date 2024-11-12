package com.learn.praveen.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.praveen.model.Album;
import com.learn.praveen.repository.AlbumRepository;

@Service
public class AlbumService {
    
    @Autowired
    private AlbumRepository albumRepository;

    public Album save (Album album) {
        return albumRepository.save(album);
    }

    public void deleteById (long id) {
        albumRepository.deleteById(id);
    }

    public List<Album> findAll() {
        return albumRepository.findAll();
    }

    public List<Album> findByAccountId(long accountId) {
        return albumRepository.findByAccountId(accountId);
    }

    public Optional<Album> findById(long id) {
        return albumRepository.findById(id);
    }

    

}
