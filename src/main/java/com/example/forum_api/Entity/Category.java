package com.example.forum_api.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category {
    private String name;
    private TagType type;

    public Category() {}

    public Category(String name, TagType type) {
        this.name = name;
        this.type = type;
    }
}
