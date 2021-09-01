package com.marvel.comicsproject.service;

import com.marvel.comicsproject.domain.Character;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CharacterService {

    Page<Character> getAllCharacters(String filter, Pageable pageable);

    Character findCharacterById(Long id);

    List<Character> findCharactersByComicId(Long id);

    Character saveCharacter(Character character);
}
