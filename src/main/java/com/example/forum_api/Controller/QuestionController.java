package com.example.forum_api.Controller;

import com.example.forum_api.Entity.Question;
import com.example.forum_api.Entity.User;
import com.example.forum_api.Factory.AbstractFactory;
import com.example.forum_api.Factory.DatabaseFactory;
import com.example.forum_api.Factory.ProgrammingFactory;
import com.example.forum_api.Service.QuestionService;
import com.example.forum_api.Service.TagService;
import com.example.forum_api.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    // Tüm soruları görüntüle
    @GetMapping
    public ModelAndView showAllQuestions() {
        ModelAndView modelAndView = new ModelAndView("questions");
        modelAndView.addObject("questions", questionService.getAllQuestions());
        return modelAndView;
    }

    // Soru detaylarını görüntüle
    @GetMapping("/{id}")
    public ModelAndView showQuestionDetails(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("question_details");
        Optional<Question> questionOpt = questionService.getQuestionById(id);

        if (questionOpt.isPresent()) {
            Question question = questionOpt.get();
            modelAndView.addObject("question", question);
            modelAndView.addObject("answers", question.getAnswers());
        } else {
            modelAndView.setViewName("error");
            modelAndView.addObject("message", "Soru bulunamadı.");
        }

        return modelAndView;
    }

    // Yeni soru oluşturma sayfası
    @GetMapping("/create")
    public ModelAndView showCreateQuestionPage() {
        return new ModelAndView("create_question");
    }

    // Soru oluştur
    @PostMapping("/create")
    public ModelAndView createQuestion(@RequestParam String title,
                                       @RequestParam String description,
                                       HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            modelAndView.setViewName("login");
            modelAndView.addObject("message", "Lütfen önce giriş yapın.");
            return modelAndView;
        }

        try {
            // QuestionService ile soru oluşturuluyor
            questionService.createQuestion(title, description, user, Set.of("java", "spring"));
            userService.incrementQuestionCount(user.getId());
            modelAndView.setViewName("redirect:/questions");
        } catch (Exception e) {
            modelAndView.setViewName("create_question");
            modelAndView.addObject("message", "Bir hata oluştu: " + e.getMessage());
        }

        return modelAndView;
    }

    // Etiket ekleme
    @PostMapping("/{id}/addTag")
    public String addTag(@PathVariable("id") Long questionId,
                         @RequestParam String tagType,
                         @RequestParam String tagName) {
        Optional<Question> questionOpt = questionService.getQuestionById(questionId);

        if (questionOpt.isEmpty()) {
            return "redirect:/questions?error=Soru bulunamadı.";
        }

        Question question = questionOpt.get();
        String[] tagNames = tagName.split(",");

        // Tag türüne göre factory'yi seçiyoruz
        AbstractFactory factory = getFactory(tagType);

        // Her bir etiket adı için işlem yap
        for (String singleTagName : tagNames) {
            try {
                tagService.addTagToQuestion(question, factory, singleTagName.trim());
            } catch (Exception e) {
                return "redirect:/questions/" + questionId + "?error=" + e.getMessage();
            }
        }

        questionService.saveQuestion(question);  // Soru güncelleniyor
        return "redirect:/questions/" + questionId;
    }

    // TagFactory'yi seçme fonksiyonu
    private AbstractFactory getFactory(String tagType) {
        switch (tagType.toUpperCase()) {
            case "PROGRAMMING":
                return new ProgrammingFactory();
            case "DATABASE":
                return new DatabaseFactory();
            default:
                throw new IllegalArgumentException("Geçersiz tag türü: " + tagType);
        }
    }
}
