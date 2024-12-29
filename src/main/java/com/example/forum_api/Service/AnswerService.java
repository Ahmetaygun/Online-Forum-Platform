package com.example.forum_api.Service;

import com.example.forum_api.Entity.Answer;
import com.example.forum_api.Repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    // Soruya ait cevapları almak için
    public List<Answer> getAnswersByQuestionId(Long questionId) {
        return answerRepository.findByQuestionId(questionId);
    }

    // Yeni bir cevap eklemek için
    public void createAnswer(Answer answer) {
        answerRepository.save(answer);
    }
}
