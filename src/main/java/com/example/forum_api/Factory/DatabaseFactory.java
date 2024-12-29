package com.example.forum_api.Factory;

import com.example.forum_api.Entity.Tag;
import com.example.forum_api.Entity.Category;
import com.example.forum_api.Entity.TagType;

public class DatabaseFactory implements AbstractFactory {

    @Override
    public Tag createTag(String name) {
        return new Tag(name, TagType.DATABASE);
    }

    @Override
    public Category createCategory(String name) {
        return new Category(name, TagType.DATABASE);
    }
}
