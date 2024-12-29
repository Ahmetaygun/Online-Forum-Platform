package com.example.forum_api.Service;

import com.example.forum_api.Entity.Question;
import com.example.forum_api.Entity.Tag;
import com.example.forum_api.Entity.TagType;
import com.example.forum_api.Entity.User;
import com.example.forum_api.Repository.QuestionRepository;
import com.example.forum_api.Repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TagRepository tagRepository;  // TagRepository'yi buraya ekliyoruz.

    // Tüm soruları al
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // Belirli bir ID'ye sahip soruyu al
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public void createQuestion(String title, String description, User user, Set<String> tagNames) {
        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagNames) {
            Optional<Tag> optionalTag = tagRepository.findByName(tagName);  // Optional döner
            if (optionalTag.isPresent()) {
                tags.add(optionalTag.get());  // Eğer tag bulunduysa, tag'i al
            } else {
                // Eğer tag bulunmazsa, yeni bir tag oluştur ve kaydet
                Tag newTag = new Tag(tagName, TagType.GENERAL);  // Default TagType kullanıldı
                tagRepository.save(newTag);
                tags.add(newTag);
            }
        }

        // Soruyu oluştur ve kaydet
        Question question = new Question.Builder()
                .title(title)
                .content(description)
                .user(user)
                .tags(tags)
                .build();

        questionRepository.save(question);
    }



    // Soruyu kaydet ya da güncelle
    public void saveQuestion(Question question) {
        questionRepository.save(question);  // Soruyu kaydet
    }
}
