package com.marvel.comicsproject.service;

import com.marvel.comicsproject.domain.Comic;
import com.marvel.comicsproject.exceptions.CharacterNotFoundException;
import com.marvel.comicsproject.exceptions.ComicNotFoundException;
import com.marvel.comicsproject.repo.ComicRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class ComicServiceImpl implements ComicService {

    private final ComicRepository comicRepository;

    @Override
    public Page<Comic> getAllComics(String filter, Pageable pageable) {
        if (filter != null) {
            return comicRepository.findAll(filter, pageable);
        }
        return comicRepository.findAll(pageable);
    }

    @Override
    public Comic findComicById(Long comicId) {
        return comicRepository.findById(comicId)
            .orElseThrow(() -> new ComicNotFoundException("Комикс с id " + comicId + " не найден"));
    }

    @Override
    public List<Comic> findComicsByCharacterId(Long characterId) {
        List<Comic> comicList = comicRepository.findComicsByCharacterId(characterId);
        if (comicList.isEmpty()) {
            throw new CharacterNotFoundException("Персонаж с id " + characterId + " не найден");
        } else {
            return comicList;
        }
    }

    @Override
    public Comic saveComic(Comic comic) {
        return comicRepository.save(comic);
    }
}
