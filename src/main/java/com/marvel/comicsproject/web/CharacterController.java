package com.marvel.comicsproject.web;

import com.marvel.comicsproject.domain.Character;
import com.marvel.comicsproject.dto.CharacterDto;
import com.marvel.comicsproject.dto.CharacterResponse;
import com.marvel.comicsproject.dto.CharactersResponse;
import com.marvel.comicsproject.exceptions.CharacterNotFoundException;
import com.marvel.comicsproject.exceptions.ComicNotFoundException;
import com.marvel.comicsproject.handler.CharacterImageHandler;
import com.marvel.comicsproject.handler.ImageHandler;
import com.marvel.comicsproject.service.CharacterService;
import com.marvel.comicsproject.service.ImageHandleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@Api("Контроллер персонажей")
@RestController
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;
    private final ModelMapper modelMapper;

    @ApiOperation("Получение, фильтрация и сортировка списка персонажей")
    @GetMapping("/characters")
    public ResponseEntity<Page<CharacterDto>> findAllCharacters(@RequestParam(value = "filter", required = false) String filter,
        Pageable pageable) {
        Page<Character> characterPage = characterService.getAllCharacters(filter, pageable);
        Page<CharacterDto> characterDtoPage = characterPage.map(character -> modelMapper.map(character, CharacterDto.class));
        return new ResponseEntity<>(characterDtoPage, HttpStatus.OK);
    }

    @ApiOperation("Поиск персонажа по id")
    @GetMapping("/characters/{characterId}")
    public ResponseEntity<CharacterResponse> findCharacterById(@PathVariable Long characterId) {
        CharacterResponse characterResponse = new CharacterResponse();
        try {
            Character character = characterService.findCharacterById(characterId);
            CharacterDto characterDto = modelMapper.map(character, CharacterDto.class);
            characterResponse.setCharacterDto(characterDto);
        } catch (CharacterNotFoundException e) {
            characterResponse.setError(e.getMessage());
            return new ResponseEntity<>(characterResponse, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(characterResponse, HttpStatus.OK);
    }

    @ApiOperation("Поиск персонажей по id комикса")
    @GetMapping("comic/{comicId}/characters")
    public ResponseEntity<CharactersResponse> findCharactersByComic(@PathVariable Long comicId) {
        CharactersResponse charactersResponse = new CharactersResponse();
        try {
            List<Character> characterList = characterService.findCharactersByComicId(comicId);
            List<CharacterDto> characterDtoList = characterList
                .stream()
                .map(character -> modelMapper.map(character, CharacterDto.class)).collect(Collectors.toList());
            charactersResponse.setCharacterDto(characterDtoList);
        } catch (ComicNotFoundException ex) {
            charactersResponse.setError(ex.getMessage());
            return new ResponseEntity<>(charactersResponse, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(charactersResponse, HttpStatus.OK);
    }

    @ApiOperation("Добавление нового персонажа")
    @PostMapping("/characters")
    public ResponseEntity<CharacterResponse> createCharacter(@RequestBody Character character) {
        Character newCharacter = characterService.saveCharacter(character);
        CharacterDto characterDto = modelMapper.map(newCharacter, CharacterDto.class);
        CharacterResponse characterResponse = new CharacterResponse();
        characterResponse.setCharacterDto(characterDto);
        return new ResponseEntity<>(characterResponse, HttpStatus.OK);
    }

    @ApiOperation("Обновление данных персонажа")
    @PutMapping("/characters/{characterId}")
    public ResponseEntity<CharacterResponse> updateCharacter(@PathVariable Long characterId, @RequestBody Character character) {
        Character updatedCharacter = new Character();
        CharacterResponse characterResponse = new CharacterResponse();
        try {
            updatedCharacter = characterService.findCharacterById(characterId);
        } catch (CharacterNotFoundException ex) {
            characterResponse.setError(ex.getMessage());
            return new ResponseEntity<>(characterResponse, HttpStatus.NOT_FOUND);
        }
        updatedCharacter.setDescription(character.getDescription() == null ? updatedCharacter.getDescription() : character.getDescription());
        updatedCharacter.setModified(character.getModified() == null ? updatedCharacter.getModified() : character.getModified());
        updatedCharacter.setName(character.getName() == null ? updatedCharacter.getName() : character.getName());
        characterService.saveCharacter(updatedCharacter);
        characterResponse.setCharacterDto(modelMapper.map(updatedCharacter, CharacterDto.class));
        return new ResponseEntity<>(characterResponse, HttpStatus.OK);
    }

    @ApiOperation("Добавление изображения персонажа по id")
    @PostMapping(value = "/characters/{characterId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CharacterResponse> postUpload(@PathVariable("characterId") Long id,
        @RequestPart("file") @ApiParam(value = "File", required = true) MultipartFile file) {
        ImageHandleService<ImageHandler, Character> imageHandleService = new ImageHandleService<>();
        ImageHandler<Character> imageHandler = new CharacterImageHandler(characterService);
        CharacterResponse characterResponse = new CharacterResponse();
        Character character;
        try{
            character = characterService.findCharacterById(id);
        }
        catch (CharacterNotFoundException ex){
            characterResponse.setError(ex.getMessage());
            return new ResponseEntity<>(characterResponse, HttpStatus.NOT_FOUND);
        }
        CharacterDto characterDto = modelMapper.map(imageHandleService.save(imageHandler, character, file), CharacterDto.class);
        characterResponse.setCharacterDto(characterDto);
        return new ResponseEntity<>(characterResponse, HttpStatus.OK);
    }
}
