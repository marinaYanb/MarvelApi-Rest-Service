package com.marvel.comicsproject.web;

import com.marvel.comicsproject.domain.Comic;
import com.marvel.comicsproject.dto.ComicDto;
import com.marvel.comicsproject.dto.ComicResponse;
import com.marvel.comicsproject.dto.ComicsResponse;
import com.marvel.comicsproject.exceptions.CharacterNotFoundException;
import com.marvel.comicsproject.exceptions.ComicNotFoundException;
import com.marvel.comicsproject.handler.ComicImageHandler;
import com.marvel.comicsproject.handler.ImageHandler;
import com.marvel.comicsproject.service.ComicService;
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

@Api("Контроллер комиксов")
@RestController
@RequiredArgsConstructor
public class ComicController {

    private final ComicService comicService;
    private final ModelMapper modelMapper;

    @ApiOperation("Получение, фильтрация и сортировка списка комиксов")
    @GetMapping("/comics")
    @ResponseBody
    public ResponseEntity<Page<ComicDto>> findAllComics(
        @RequestParam(value = "filter", required = false) String filter,
        Pageable pageable) {
        Page<Comic> comics = comicService.getAllComics(filter, pageable);
        return new ResponseEntity<>(comics.map(comic -> modelMapper.map(comic, ComicDto.class)), HttpStatus.OK);
    }

    @ApiOperation("Поиск комиксов по id")
    @GetMapping("/comics/{comicId}")
    public ResponseEntity<ComicResponse> findComicById(@PathVariable Long comicId) {
        ComicResponse comicResponse = new ComicResponse();
        try {
            Comic comic = comicService.findComicById(comicId);
            ComicDto comicDto = modelMapper.map(comic, ComicDto.class);
            comicResponse.setComicDto(comicDto);
        } catch (ComicNotFoundException e) {
            comicResponse.setError(e.getMessage());
            return new ResponseEntity<>(comicResponse, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(comicResponse, HttpStatus.OK);
    }

    @ApiOperation("Поиск комиксов по id персонажа")
    @GetMapping("character/{characterId}/comics")
    public ResponseEntity<ComicsResponse> findCharactersByComicId(@PathVariable Long characterId) {
        ComicsResponse comicsResponse = new ComicsResponse();
        try {
            List<Comic> comicList = comicService.findComicsByCharacterId(characterId);
            List<ComicDto> comicDtoList = comicList
                .stream()
                .map(comic -> modelMapper.map(comic, ComicDto.class)).collect(Collectors.toList());
            comicsResponse.setComicsDto(comicDtoList);
        } catch (CharacterNotFoundException ex) {
            comicsResponse.setError(ex.getMessage());
            return new ResponseEntity<>(comicsResponse, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(comicsResponse, HttpStatus.OK);
    }

    @ApiOperation("Добавления нового комикса")
    @PostMapping(value = "/comics")
    public ResponseEntity<ComicResponse> createComic(@RequestBody Comic comic) {
        Comic newComic = comicService.saveComic(comic);
        ComicDto comicDto = modelMapper.map(newComic, ComicDto.class);
        ComicResponse comicResponse = new ComicResponse();
        comicResponse.setComicDto(comicDto);
        return new ResponseEntity<>(comicResponse, HttpStatus.OK);
    }

    @ApiOperation("Обновление данных комиксов")
    @PutMapping("/comics/{comicId}")
    public ResponseEntity<ComicResponse> updateComic(@PathVariable Long comicId, @RequestBody Comic comic) {
        Comic updatedComic = new Comic();
        ComicResponse comicResponse = new ComicResponse();
        try {
            updatedComic = comicService.findComicById(comicId);
        } catch (ComicNotFoundException ex) {
            comicResponse.setError(ex.getMessage());
            return new ResponseEntity<>(comicResponse, HttpStatus.NOT_FOUND);
        }
        updatedComic.setId(comicId);
        updatedComic.setDescription(comic.getDescription() == null ? updatedComic.getDescription() : comic.getDescription());
        updatedComic.setModified(comic.getModified() == null ? updatedComic.getModified() : comic.getModified());
        updatedComic.setTitle(comic.getTitle() == null ? updatedComic.getTitle() : comic.getTitle());
        updatedComic = comicService.saveComic(updatedComic);
        comicResponse.setComicDto(modelMapper.map(updatedComic, ComicDto.class));
        return new ResponseEntity<>(comicResponse, HttpStatus.OK);
    }

    @ApiOperation("Добавление изображения комикса по id")
    @PostMapping(value = "/comics/{comicId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ComicResponse> postUpload(@PathVariable("comicId") Long id,
                                                        @RequestPart("file") @ApiParam(value = "File", required = true) MultipartFile file) {
        ImageHandleService<ImageHandler, Comic> imageHandleService = new ImageHandleService<>();
        ImageHandler<Comic> imageHandler = new ComicImageHandler(comicService);
        ComicResponse comicResponse = new ComicResponse();
        Comic comic;
        try{
            comic = comicService.findComicById(id);
        }
        catch (ComicNotFoundException ex){
            comicResponse.setError(ex.getMessage());
            return new ResponseEntity<>(comicResponse, HttpStatus.NOT_FOUND);
        }
        ComicDto comicDto = modelMapper.map(imageHandleService.save(imageHandler, comic, file), ComicDto.class);
        comicResponse.setComicDto(comicDto);
        return new ResponseEntity<>(comicResponse, HttpStatus.OK);
    }
}
