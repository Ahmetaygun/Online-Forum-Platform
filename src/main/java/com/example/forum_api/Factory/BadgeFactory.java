package com.example.forum_api.Factory;

import com.example.forum_api.Entity.Badge;

public class BadgeFactory {

    public static Badge createBadge(String name, String description, String level) {
        if (level == null || (!level.equals("Bronz") && !level.equals("Gümüş") && !level.equals("Altın"))) {
            throw new IllegalArgumentException("Invalid level");
        }
        Badge badge = new Badge();
        badge.setName(name);
        badge.setDescription(description);
        badge.setLevel(level);
        return badge;
    }}

