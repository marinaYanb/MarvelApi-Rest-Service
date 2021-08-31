package com.marvel.comicsproject.service;

import com.marvel.comicsproject.domain.Comic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ComicService {

    Page<Comic> getAllComics(String filter, Pageable pageable);

    Comic findComicById(Long id);

    List<Comic> findComicsByCharacterId(Long comicId);

    Comic saveComic(Comic comic);
}