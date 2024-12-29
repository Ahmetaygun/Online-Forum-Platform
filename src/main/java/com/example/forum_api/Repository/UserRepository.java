package com.example.forum_api.Repository;

import com.example.forum_api.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);  // Kullanıcı adı ile kullanıcı bulma

    Optional<User> findById(Long id); // ID ile kullanıcı arama
}
