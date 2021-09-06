package com.marvel.comicsproject.handler;

import com.marvel.comicsproject.domain.Comic;
import com.marvel.comicsproject.service.ComicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RequiredArgsConstructor
public class ComicImageHandler implements ImageHandler<Comic> {
    @Value("${com.marvel.comicsproject.uploadedComicFolder}")
    private String uploadedComicFolder;

    private final ComicService comicService;

    @Override
    public Comic saveImageInObject(Comic comic, Path path) {
        comic.setThumbnail(path.toString());
        return comicService.saveComic(comic);
    }

    @Override
    public Path saveFile(Comic comic, MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        String fileName = UUID.randomUUID() + file.getOriginalFilename();
        Path path = null;
        try {
            path = Paths.get("src/main/resources" + uploadedComicFolder + comic.getTitle() + "-" + fileName);
            File dest = new File(new File(path.toString()).getAbsolutePath());
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
