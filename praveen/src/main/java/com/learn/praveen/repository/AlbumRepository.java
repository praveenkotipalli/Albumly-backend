package com.learn.praveen.repository;

import java.util.*; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.praveen.model.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByAccountId(long accountId);

    Optional<Album> findById(long id);
}
