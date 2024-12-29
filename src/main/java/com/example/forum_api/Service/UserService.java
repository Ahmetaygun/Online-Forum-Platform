package com.example.forum_api.Service;

import com.example.forum_api.Entity.User;
import com.example.forum_api.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BadgeService badgeService;

    // Singleton örneği için static referans
    private static UserService instance;

    // Private constructor ile doğrudan nesne oluşturulmasını engelle
    private UserService(UserRepository userRepository, BadgeService badgeService) {
        this.userRepository = userRepository;
        this.badgeService = badgeService;
    }

    // Global erişim noktası (Thread-Safe Singleton)
    public static synchronized UserService getInstance(UserRepository userRepository, BadgeService badgeService) {
        if (instance == null) {
            instance = new UserService(userRepository, badgeService);
        }
        return instance;
    }

    // Kullanıcıyı kaydetme
    public void register(User user) {
        userRepository.save(user);
    }

    // Kullanıcı girişi (login) işlemi
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }

    // Soru sayısını artırma
    public void incrementQuestionCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        user.setQuestionCount(user.getQuestionCount() + 1); // Soru sayısını artır
        userRepository.save(user); // Kullanıcıyı kaydet
        badgeService.assignBadgeIfEligible(user);  // Rozet atama kontrolü
    }

    // Yanıt sayısını artırma
    public void incrementAnswerCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        user.setAnswerCount(user.getAnswerCount() + 1); // Yanıt sayısını artır
        userRepository.save(user); // Kullanıcıyı kaydet
        badgeService.assignBadgeIfEligible(user);  // Rozet atama kontrolü
    }
}
