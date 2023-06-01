package com.exam.ZikriMuzakkyExam.repository;

import com.exam.ZikriMuzakkyExam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
}
