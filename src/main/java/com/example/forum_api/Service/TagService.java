package com.example.forum_api.Service;

import com.example.forum_api.Entity.Question;
import com.example.forum_api.Entity.Tag;
import com.example.forum_api.Factory.AbstractFactory;
import com.example.forum_api.Repository.TagRepository;
import com.example.forum_api.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Transactional
    public void addTagToQuestion(Question question, AbstractFactory factory, String tagName) {
        Tag newTag = factory.createTag(tagName);

        Tag savedTag = tagRepository.findByName(newTag.getName())
                .orElseGet(() -> {
                    tagRepository.save(newTag);
                    return newTag;
                });

        question.getTags().add(savedTag);

        questionRepository.save(question);
    }


}
