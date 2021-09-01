package com.marvel.comicsproject.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

import static com.marvel.comicsproject.constants.StringConst.CHARACTER_URI;

@Data
@Entity
@NoArgsConstructor
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    private String name;
    private String description;
    @JsonIgnore
    private String thumbnail;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modified;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
        name = "comic_character",
        joinColumns = @JoinColumn(name = "character_id"),
        inverseJoinColumns = @JoinColumn(name = "comic_id"))
    @JsonIgnore
    private Set<Comic> comics;

    public String getURLId() {
        return CHARACTER_URI + id;
    }

}
