package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.models.Address;
import com.docslilcoders.tacoslosprimos.models.User;
import com.docslilcoders.tacoslosprimos.repositories.AddressRepository;
import com.docslilcoders.tacoslosprimos.repositories.UserRepository;
import com.docslilcoders.tacoslosprimos.services.AuthBuddy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserController {

    private final UserRepository userDao;
    private final PasswordEncoder passwordEncoder;

    private final AddressRepository addressDao;


    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, AddressRepository addressDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.addressDao = addressDao;
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
            Address usersAddress = new Address(user.getPrimary_address(), user);
            addressDao.save(usersAddress);
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
        if (user.getId() == 0) {
            return "redirect:/login";
        }

        Optional<User> optionalUser = userDao.findById(user.getId());
        if(optionalUser.isEmpty()) {
            System.out.println("menu item not found");
            //TODO error page
            return "redirect:/login";
        }
        model.addAttribute("currentUser", optionalUser.get());
        model.addAttribute("user", user);
        return "/users/edit_profile"; //need to go back to change this
    }
    @PostMapping("/edit-profile")
    public String doEdit(@ModelAttribute User user, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getId() == 0) {
            return "redirect:/login";
        }

//        boolean usernameTaken = false;
//        boolean emailTaken = false;
//
//        if(!currentUser.getUsername().equals(user.getUsername()) && (userDao.findByUsername(user.getUsername()) != null)) {
//            System.out.println("username taken ");
//            usernameTaken = true;
//        }
//
//        if(!currentUser.getEmail().equals(user.getEmail()) && userDao.findByEmail(user.getEmail()) != null) {
//            System.out.println("email taken ");
//            emailTaken = true;
//        }
//
//        if(usernameTaken || emailTaken) {
//            model.addAttribute("usernameTaken", usernameTaken);
//            model.addAttribute("emailTaken", emailTaken);
//            return "users/edit_profile";
//        } else {
            //this sets it for the current session
            currentUser.setFirst_name(user.getFirst_name());
            currentUser.setLast_name(user.getLast_name());
            currentUser.setUsername(user.getUsername());
            currentUser.setPhone(user.getPhone());
            currentUser.setEmail(user.getEmail());
            currentUser.setPrimary_address(user.getPrimary_address());

            //this fills in the missing fields from the user object from form
            user.setId(currentUser.getId());
            user.setPhoto_url(currentUser.getPhoto_url());
            user.setAccumulated_points(currentUser.getAccumulated_points());
            user.setRedeemed_points(currentUser.getRedeemed_points());
            user.setPassword(currentUser.getPassword());

            userDao.save(user);
            return "redirect:/edit-profile";
//        }
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword, @RequestParam String password){
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userDao.findById(loggedInUser.getId());
        if(optionalUser.isEmpty()) {
            System.out.println("menu item not found");
            //TODO error page
            return "redirect:/login";
        }

        String currentPassword = optionalUser.get().getPassword();

        boolean passwordCorrect = passwordEncoder.matches(password, currentPassword);
        if(passwordCorrect) {
            String newPasswordHash = passwordEncoder.encode(newPassword);
            optionalUser.get().setPassword(newPasswordHash);
            userDao.save(optionalUser.get());
        } else {
            System.out.println("\n\n\nwrong password\n\n\n");
        }
        return "redirect:/edit-profile";
    }

    @PostMapping("/addAddress")
    public String addAddress(@RequestParam String newAddress) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userDao.findById(loggedInUser.getId());
        if(optionalUser.isEmpty()) {
            System.out.println("menu item not found");
            //TODO error page
            return "redirect:/login";
        }

        System.out.println("\n\n\n" + newAddress + "\n\n\n");
        Address address = new Address(newAddress, optionalUser.get());
        addressDao.save(address);


        return "redirect:/edit-profile";
    }







}

