package com.marvel.comicsproject.service;

import com.marvel.comicsproject.domain.Character;
import com.marvel.comicsproject.exceptions.CharacterNotFoundException;
import com.marvel.comicsproject.exceptions.ComicNotFoundException;
import com.marvel.comicsproject.repo.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;

    @Override
    public Page<Character> getAllCharacters(String filter, Pageable pageable) {
        if (filter != null) {
            return characterRepository.findAll(filter, pageable);
        }
        return characterRepository.findAll(pageable);
    }

    @Override
    public Character findCharacterById(Long characterId) {
        return characterRepository.findById(characterId)
            .orElseThrow(() -> new CharacterNotFoundException("Персонаж с id " + characterId + " не найден"));
    }

    @Override
    public List<Character> findCharactersByComicId(Long comicId) {
        List<Character> characterList = characterRepository.findCharactersByComicId(comicId);
        if (characterList.isEmpty()) {
            throw new ComicNotFoundException("Комикс с id " + comicId + " не найден");
        } else {
            return characterList;
        }
    }

    @Override
    public Character saveCharacter(Character character) {
        return characterRepository.save(character);
    }
}
