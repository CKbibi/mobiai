package com.cex0.mobiai.controller;

import com.cex0.mobiai.model.entity.User;
import com.cex0.mobiai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class Test {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/save")
    public void save() {
        User user = new User();
        user.setUsername("cex0");
        user.setPassword("123456");
        user.setEmail("9818@qq.com");
        user.setNickname("B6b0N9");
        userRepository.save(user);
    }
}
