package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UsersController {

    private final UserRepository userDao;

    public UsersController(UserRepository userDao) {
        this.userDao = userDao;
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "users/register";
    }
    @GetMapping("/forgot-password")
    public String getForgotPasswordPage() {
        return "users/forgot_password";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @GetMapping("/profile/{id}")
    public String getProfilePage(@PathVariable String id) {
        return "users/profile";
    }


}
