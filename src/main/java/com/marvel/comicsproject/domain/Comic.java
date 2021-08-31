package com.marvel.comicsproject.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

import static com.marvel.comicsproject.constants.StringConst.COMIC_URI;

@Data
@Entity
@NoArgsConstructor
public class Comic {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    private Long id;
    private String title;
    private String description;
    @JsonIgnore
    private String thumbnail;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modified;
    @ManyToMany(mappedBy = "comics", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Character> characters;

    public String getURLId() {
        return COMIC_URI + id;
    }
}
