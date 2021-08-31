package com.marvel.comicsproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class ComicsResponse {

    List<ComicDto> comicsDto;
    String Error;
}
