package com.example.forum_api.Controller;

import com.example.forum_api.Entity.User;
import com.example.forum_api.Repository.UserRepository;
import com.example.forum_api.Service.BadgeService;
import com.example.forum_api.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // Singleton ile UserService örneğini al
    @Autowired
    public UserController(UserRepository userRepository, BadgeService badgeService) {
        this.userService = UserService.getInstance(userRepository, badgeService);
    }

    @GetMapping("/register")
    public ModelAndView showRegisterPage() {
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@RequestParam String username, @RequestParam String password) {
        ModelAndView modelAndView = new ModelAndView("register");
        if (username == null || username.isEmpty()) {
            modelAndView.addObject("message", "Kullanıcı adı boş olamaz!");
            return modelAndView;
        }

        if (password == null || password.isEmpty()) {
            modelAndView.addObject("message", "Şifre boş olamaz!");
            return modelAndView;
        }

        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            userService.register(user);
            modelAndView.setViewName("index");
            modelAndView.addObject("message", "Kayıt başarılı! Giriş yapabilirsiniz.");
            return modelAndView;
        } catch (Exception e) {
            modelAndView.addObject("message", "Kayıt sırasında bir hata oluştu: " + e.getMessage());
            return modelAndView;
        }
    }

    @GetMapping("/login")
    public ModelAndView showLoginPage() {
        return new ModelAndView("login");
    }

    @PostMapping("/login")
    public ModelAndView loginUser(@RequestParam String username, @RequestParam String password, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            modelAndView.setViewName("login");
            modelAndView.addObject("message", "Kullanıcı adı ve şifre boş olamaz!");
            return modelAndView;
        }

        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("user", user); // Kullanıcıyı oturumda sakla
            modelAndView.setViewName("redirect:/questions"); // Başarılı giriş sonrası yönlendirme
            return modelAndView;
        } else {
            modelAndView.setViewName("login");
            modelAndView.addObject("message", "Geçersiz kullanıcı adı veya şifre!");
            return modelAndView;
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Oturumdan kullanıcıyı çıkart
        session.invalidate();
        return "redirect:/users/login"; // Kullanıcıyı giriş sayfasına yönlendir
    }
}
