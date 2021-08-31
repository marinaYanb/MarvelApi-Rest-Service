package com.marvel.comicsproject.dto;

import com.marvel.comicsproject.domain.Character;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ComicDto {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime modified;
    private String resourceURI;
    private Set<Character> characters;
    private String thumbnail;
}
