package com.marvel.comicsproject.handler;

import com.marvel.comicsproject.domain.Comic;
import com.marvel.comicsproject.service.ComicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.marvel.comicsproject.constants.StringConst.UPLOADED_COMIC_FOLDER;

@RequiredArgsConstructor
public class ComicImageHandler implements ImageHandler<Comic>{

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
            path = Paths.get("src/main/resources"+ UPLOADED_COMIC_FOLDER + comic.getTitle() + "-" + fileName);
            File dest = new File(new File(path.toString()).getAbsolutePath());
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
