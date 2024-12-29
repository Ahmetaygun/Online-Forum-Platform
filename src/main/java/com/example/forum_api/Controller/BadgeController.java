package com.example.forum_api.Controller;

import com.example.forum_api.Entity.User;
import com.example.forum_api.Repository.UserRepository;
import com.example.forum_api.Service.BadgeService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {

    private final BadgeService badgeService;
    private final UserRepository userRepository;

    public BadgeController(BadgeService badgeService, UserRepository userRepository) {
        this.badgeService = badgeService;
        this.userRepository = userRepository;
    }

    @PostMapping("/check/{userId}")
    public String checkAndAssignBadge(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId); // Kullanıcıyı al
        if (user.isPresent()) {
            // Rozet atamayı çağır
            badgeService.assignBadgeIfEligible(user.get());
            return "Rozetler kontrol edildi ve uygun olanlar eklendi.";
        }
        return "Kullanıcı bulunamadı.";  // Kullanıcı bulunmazsa
    }
}
