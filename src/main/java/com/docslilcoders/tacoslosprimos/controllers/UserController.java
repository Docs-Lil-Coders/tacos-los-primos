package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.models.User;
import com.docslilcoders.tacoslosprimos.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserRepository userDao;
    private PasswordEncoder passwordEncoder;


    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "users/register";
    }
    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user, Model model){
        boolean usernameTaken = false;
        boolean emailTaken = false;

        if(userDao.findByUsername(user.getUsername()) != null) {
            System.out.println("username taken ");
            usernameTaken = true;
        }

        if(userDao.findByEmail(user.getEmail()) != null) {
            System.out.println("email taken ");
            emailTaken = true;
        }

        if(usernameTaken || emailTaken) {
            model.addAttribute("usernameTaken", usernameTaken);
            model.addAttribute("emailTaken", emailTaken);
            return "users/register";
        } else {
            user.setPhoto_url("");
            String hash = passwordEncoder.encode(user.getPassword());
            user.setPassword(hash);
            userDao.save(user);
            System.out.println(user);
            return "redirect:/login";
        }
    }
    @GetMapping("/forgot-password")
    public String getForgotPasswordPage() {
        return "users/forgot_password";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model) {


        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getId() == 0) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "users/profile";
    }
    @GetMapping("/edit-profile")
    public String showEdit(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "/users/edit_profile"; //need to go back to change this
    }
}
