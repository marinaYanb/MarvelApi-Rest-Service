package com.marvel.comicsproject.repo;

import com.marvel.comicsproject.domain.Comic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {
    @Query("select comic from Comic comic join fetch comic.characters character where character.id= :characterId")
    List<Comic> findComicsByCharacterId(@Param("characterId") Long characterId);

    @Query("select comic from Comic comic where comic.title like %?1%" +
        "or comic.description like %?1%")
    Page<Comic> findAll(@Param("filter") String filter, Pageable pageable);
}