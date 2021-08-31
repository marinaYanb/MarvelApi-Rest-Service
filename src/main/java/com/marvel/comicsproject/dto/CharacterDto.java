package com.marvel.comicsproject.dto;

import com.marvel.comicsproject.domain.Comic;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CharacterDto {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime modified;
    private String resourceURI;
    private Set<Comic> comics;
    private String thumbnail;
}
