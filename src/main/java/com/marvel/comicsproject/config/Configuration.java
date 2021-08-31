package com.marvel.comicsproject.config;

import com.marvel.comicsproject.domain.Character;
import com.marvel.comicsproject.domain.Comic;
import com.marvel.comicsproject.dto.CharacterDto;
import com.marvel.comicsproject.dto.ComicDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Character, CharacterDto>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setName(source.getName());
                map().setDescription(source.getDescription());
                map().setModified(source.getModified());
                map().setComics(source.getComics());
                map().setResourceURI(source.getURLId());
                map().setThumbnail(source.getThumbnail());
            }
        });
        modelMapper.addMappings(new PropertyMap<Comic, ComicDto>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setTitle(source.getTitle());
                map().setDescription(source.getDescription());
                map().setModified(source.getModified());
                map().setCharacters(source.getCharacters());
                map().setResourceURI(source.getURLId());
                map().setThumbnail(source.getThumbnail());
            }
        });
        return modelMapper;
    }
}