package com.example.forum_api.Controller;

import com.example.forum_api.Entity.Answer;
import com.example.forum_api.Entity.Question;
import com.example.forum_api.Entity.User;
import com.example.forum_api.Service.AnswerService;
import com.example.forum_api.Service.QuestionService;
import com.example.forum_api.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    // Cevap ekleme işlemi
    @PostMapping("/create")
    public ModelAndView createAnswer(@RequestParam Long questionId,
                                     @RequestParam String answerText,
                                     HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        // Kullanıcıyı oturumdan al (null kontrolü yapıyoruz)
        User user = (User) session.getAttribute("user");

        if (user == null) {
            modelAndView.setViewName("login");
            modelAndView.addObject("message", "Öncelikle giriş yapmalısınız.");
            return modelAndView;
        }

        // Soruyu almak
        Optional<Question> questionOptional = questionService.getQuestionById(questionId);

        if (!questionOptional.isPresent()) {
            modelAndView.setViewName("question_details");
            modelAndView.addObject("message", "Soru bulunamadı.");
            return modelAndView;
        }

        // Soruyu Optional içinden çıkartıyoruz
        Question question = questionOptional.get();

        // Cevap nesnesini oluşturup kullanıcı ve soruyu ilişkilendirelim
        Answer answer = new Answer();
        answer.setAnswerText(answerText);
        answer.setQuestion(question);
        answer.setUser(user);

        try {
            // Cevap ekleme servisine gönderim
            answerService.createAnswer(answer);

            // Kullanıcıların answerCount'larını artır
            userService.incrementAnswerCount(user.getId());

            // Başarılıysa, cevabın eklendiği sorunun detaylarına yönlendir
            modelAndView.setViewName("redirect:/questions/" + questionId);
        } catch (Exception e) {
            modelAndView.setViewName("question_details");
            modelAndView.addObject("message", "Cevap eklenirken bir hata oluştu.");
            e.printStackTrace();
        }

        return modelAndView;
    }
}
