package com.marvel.comicsproject.repo;

import com.marvel.comicsproject.domain.Character;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    @Query("select character from Character character join fetch character.comics comic where comic.id= :comicId")
    List<Character> findCharactersByComicId(@Param("comicId") Long comicId);

    @Query("select ch from Character ch where ch.name like %?1%" +
        "or ch.description like %?1%")
    Page<Character> findAll(@Param("filter") String filter, Pageable pageable);
}
