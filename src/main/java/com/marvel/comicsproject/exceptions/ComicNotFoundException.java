package com.marvel.comicsproject.exceptions;

public class ComicNotFoundException extends RuntimeException {

    public ComicNotFoundException(String message) {
        super(message);
    }
}
