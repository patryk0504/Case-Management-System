package com.example.management.repository;

import com.example.management.model.Case;
import com.example.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndEmail(String username, String email);
//    User getUserById(Long id);
}
