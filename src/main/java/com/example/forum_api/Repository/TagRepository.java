package com.example.forum_api.Repository;

import com.example.forum_api.Entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);  // Etiketi isme göre Optional olarak bul
}
