package com.exam.ZikriMuzakkyExam.controller;

import com.exam.ZikriMuzakkyExam.entity.User;
import com.exam.ZikriMuzakkyExam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secure/auth")
public class AdminController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/admin/add")
    public String addUser(@RequestBody  User user){
        String pwd = user.getPassword();
        String  encrptedPwd = passwordEncoder.encode(pwd);
        user.setPassword(encrptedPwd);
        userRepository.save(user);
        return "User Added Success !";
    }
}
