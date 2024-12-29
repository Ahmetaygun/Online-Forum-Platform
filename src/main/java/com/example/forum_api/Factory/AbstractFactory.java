package com.example.forum_api.Factory;

import com.example.forum_api.Entity.Tag;
import com.example.forum_api.Entity.Category;

public interface AbstractFactory {
    Tag createTag(String name);
    Category createCategory(String name);
}
