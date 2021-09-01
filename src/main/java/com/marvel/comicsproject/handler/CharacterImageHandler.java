package com.marvel.comicsproject.handler;

import com.marvel.comicsproject.domain.Character;
import com.marvel.comicsproject.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.marvel.comicsproject.constants.StringConst.UPLOADED_CHARACTER_FOLDER;

@RequiredArgsConstructor
public class CharacterImageHandler implements ImageHandler<Character>{

    private final CharacterService characterService;
    @Override
    public Character saveImageInObject(Character character, Path path) {
        character.setThumbnail(path.toString());
        return characterService.saveCharacter(character);
    }

    @Override
    public Path saveFile(Character character, MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        String fileName = UUID.randomUUID() + file.getOriginalFilename();
        Path path = null;
        try {
            path = Paths.get("src/main/resources" +UPLOADED_CHARACTER_FOLDER + character.getName() + "-" + fileName);
            File dest = new File(new File(path.toString()).getAbsolutePath());
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
