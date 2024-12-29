package com.example.forum_api.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Tag name must not be null")
    @Size(min = 3, max = 255, message = "Tag name must be between 3 and 255 characters")
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TagType type;

    @ManyToMany(mappedBy = "tags")
    private Set<Question> questions = new HashSet<>();

    public Tag() {}

    public Tag(String name, TagType type) {
        this.name = name;
        this.type = type;
    }


    public Tag(String name) {
        this.name = name;
        this.type = TagType.GENERAL;
    }
}
