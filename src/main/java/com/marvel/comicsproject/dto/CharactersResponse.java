package com.marvel.comicsproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class CharactersResponse {

    List<CharacterDto> characterDto;
    String Error;
}
