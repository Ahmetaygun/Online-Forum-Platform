package com.example.forum_api.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Answer> answers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "question_tags",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // Constructor
    public Question() {
    }

    public void addTag(Tag tag) {
        if (tag != null && !this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    public boolean removeTag(Tag tag) {
        if (tag != null && tags.contains(tag)) {
            return this.tags.remove(tag);
        }
        return false;
    }

    public void addTags(Set<Tag> newTags) {
        if (newTags != null && !newTags.isEmpty()) {
            this.tags.addAll(newTags);
        }
    }

    public Set<String> getTagNames() {
        Set<String> tagNames = new HashSet<>();
        for (Tag tag : tags) {
            tagNames.add(tag.getName());
        }
        return tagNames;
    }

    public Set<Tag> getTags() {
        return new HashSet<>(tags); // Ensure a defensive copy is returned
    }

    // Builder pattern for creating Question instances
    public static class Builder {
        private String title;
        private String content;
        private User user;
        private Set<Tag> tags = new HashSet<>();

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder tags(Set<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public Question build() {
            return new Question(this);
        }
    }

    private Question(Builder builder) {
        this.title = builder.title;
        this.content = builder.content;
        this.user = builder.user;
        this.tags = builder.tags;
    }
}
