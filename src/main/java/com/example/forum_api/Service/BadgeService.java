    package com.example.forum_api.Service;

    import com.example.forum_api.Entity.Badge;
    import com.example.forum_api.Entity.User;
    import com.example.forum_api.Factory.BadgeFactory;
    import com.example.forum_api.Repository.BadgeRepository;
    import com.example.forum_api.Repository.UserRepository;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.stereotype.Service;

    import java.util.Optional;
    @Service
    public class BadgeService {

        private final BadgeRepository badgeRepository;
        private final UserRepository userRepository;
        private static final Logger logger = LoggerFactory.getLogger(BadgeService.class);

        public BadgeService(BadgeRepository badgeRepository, UserRepository userRepository) {
            this.badgeRepository = badgeRepository;
            this.userRepository = userRepository;
        }

        // Rozet atama
        public void assignBadgeIfEligible(User user) {
            try {
                // Rozet kontrolü ve atama
                checkAndAssignBadge(user, "Bronz", "Başlangıç seviyesinde katkı sağlayan kullanıcı.", "Bronz", 5, 10);
                checkAndAssignBadge(user, "Gümüş", "Orta seviyede katkı sağlayan kullanıcı.", "Gümüş", 10, 100);
                checkAndAssignBadge(user, "Altın", "Yüksek seviyede katkı sağlayan kullanıcı.", "Altın", 50, 500);
            } catch (Exception e) {
                logger.error("Rozet atama hatası: ", e);
                throw new RuntimeException("Rozet atama sırasında bir hata oluştu.", e);
            }
        }

        // Rozetleri dinamik kontrol et ve ata
        private void checkAndAssignBadge(User user, String badgeName, String description, String level, int minQuestions, int minAnswers) {
            // Kontrol için kullanıcı sorularını ve cevaplarını karşılaştır
            if (user.getQuestionCount() >= minQuestions && user.getAnswerCount() >= minAnswers) {
                // İlk olarak rozet var mı diye kontrol et
                Optional<Badge> badge = badgeRepository.findByName(badgeName);

                Badge badgeToAssign = badge.orElseGet(() -> {
                    // Yeni rozet oluşturulacaksa
                    Badge newBadge = BadgeFactory.createBadge(badgeName, description, level);
                    badgeRepository.save(newBadge);  // Yeni rozet veritabanına kaydedildi
                    return newBadge;
                });

                // Kullanıcıda yoksa bu rozet eklenecek
                if (!user.getBadges().contains(badgeToAssign)) {
                    user.getBadges().add(badgeToAssign);
                    userRepository.save(user);  // Güncellenmiş kullanıcı kaydediliyor
                    logger.info("Rozet başarıyla atandı: " + badgeName);
                } else {
                    logger.info("Kullanıcı zaten bu rozeti almış: " + badgeName);
                }
            }
        }
    }
