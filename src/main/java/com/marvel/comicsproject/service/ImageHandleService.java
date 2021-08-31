package com.marvel.comicsproject.service;

import com.marvel.comicsproject.handler.ImageHandler;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public class ImageHandleService<T extends ImageHandler, O> {

    public O save(T imageHandler, O someEntity, MultipartFile file) {

        Path path = imageHandler.saveFile(someEntity, file);

        return (O) imageHandler.saveImageInObject(someEntity, path);
    }
}