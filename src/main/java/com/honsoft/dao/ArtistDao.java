package com.honsoft.dao;

import java.util.List;

import com.honsoft.entity.Artist;

public interface ArtistDao {
    Artist findById(Long id);

    void save(Artist artist);

    void update(Artist artist);

    List<Artist> getArtists();
}