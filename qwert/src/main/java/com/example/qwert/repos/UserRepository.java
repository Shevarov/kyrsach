package com.example.qwert.repos;

import com.example.qwert.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);
    User findUserById(Integer id);
    void deleteById(Integer integer);
    User findByActivationCode(String code);
    User findByEmail(String email);
}

