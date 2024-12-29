package com.example.forum_api.Repository;

import com.example.forum_api.Entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // Soruya ait tüm cevapları alacak metot
    List<Answer> findByQuestionId(Long questionId);
}
