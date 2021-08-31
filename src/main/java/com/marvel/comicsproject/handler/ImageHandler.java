package com.marvel.comicsproject.handler;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface ImageHandler<O> {

    O saveImageInObject(O object, Path path);

    Path saveFile(O object, MultipartFile file);
}
