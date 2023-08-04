package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.models.Address;
import com.docslilcoders.tacoslosprimos.models.Order;
import com.docslilcoders.tacoslosprimos.models.ShoppingCart;
import com.docslilcoders.tacoslosprimos.models.User;
import com.docslilcoders.tacoslosprimos.repositories.AddressRepository;
import com.docslilcoders.tacoslosprimos.repositories.OrderRepository;
import com.docslilcoders.tacoslosprimos.repositories.UserRepository;
import com.docslilcoders.tacoslosprimos.services.AuthBuddy;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserRepository userDao;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressDao;
    private final OrderRepository orderDao;


    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, AddressRepository addressDao, OrderRepository orderDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.addressDao = addressDao;
        this.orderDao = orderDao;
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Register");
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
            model.addAttribute("pageTitle", "Register");
            return "users/register";
        } else {
            String hash = passwordEncoder.encode(user.getPassword());
            user.setPassword(hash);
            //give the user some points for signing up
            user.setAccumulated_points(50);
            userDao.save(user);
            Address usersAddress = new Address(user.getPrimary_address(), user);
            addressDao.save(usersAddress);
            return "redirect:/login";
        }
    }
    @GetMapping("/forgot-password")
    public String getForgotPasswordPage(Model model) {
        model.addAttribute("pageTitle", "Forgot Password");
        return "users/forgot_password";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getId() == 0) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);

        List<Order> usersOrders = orderDao.findOrdersByUserId(user.getId());
        model.addAttribute("orders", usersOrders);
        model.addAttribute("pageTitle", "Profile");
        return "users/profile";
    }

    @GetMapping("/edit-profile")
    public String showEdit(Model model,
                           @RequestParam(name = "usernameTaken", required = false) boolean usernameTaken,
                           @RequestParam(name = "emailTaken", required = false) boolean emailTaken,
                           @RequestParam(name = "incorrectPassword", required = false) boolean incorrectPassword,
                           @RequestParam(name = "profileUpdated", required = false) boolean profileUpdated,
                           @RequestParam(name = "passwordUpdated", required = false) boolean passwordUpdated){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getId() == 0) {
            return "redirect:/login";
        }


        Optional<User> optionalUser = userDao.findById(user.getId());
        if(optionalUser.isEmpty()) {
            //TODO error page
            return "redirect:/login";
        }
        model.addAttribute("usernameTaken", usernameTaken);
        model.addAttribute("emailTaken", emailTaken);
        model.addAttribute("incorrectPassword", incorrectPassword);
        model.addAttribute("profileUpdated", profileUpdated);
        model.addAttribute("passwordUpdated", passwordUpdated);
        model.addAttribute("currentUser", optionalUser.get());
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Edit Profile");
        return "/users/edit_profile";
    }
    @PostMapping("/edit-profile")
    public String doEdit(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getId() == 0) {
            return "redirect:/login";
        }

        boolean usernameTaken = false;
        boolean emailTaken = false;

        if(!currentUser.getUsername().equals(user.getUsername()) && (userDao.findByUsername(user.getUsername()) != null)) {
            System.out.println("username taken ");
            usernameTaken = true;
        }

        if(!currentUser.getEmail().equals(user.getEmail()) && userDao.findByEmail(user.getEmail()) != null) {
            System.out.println("email taken ");
            emailTaken = true;
        }

        if(usernameTaken || emailTaken) {
            redirectAttributes.addAttribute("usernameTaken", usernameTaken);
            redirectAttributes.addAttribute("emailTaken", emailTaken);
            return "redirect:/edit-profile";
        } else {

            //update the new address in the address table too
            if(!currentUser.getPrimary_address().trim().equals(user.getPrimary_address().trim())) {
                Address findAddress = addressDao.findByAddress(currentUser.getPrimary_address().trim());
                if (findAddress != null) {
                    findAddress.setAddress(user.getPrimary_address().trim());
                    addressDao.save(findAddress);
                }
            }

        //this sets it for the current session
        currentUser.setFirst_name(user.getFirst_name());
        currentUser.setLast_name(user.getLast_name());
        currentUser.setUsername(user.getUsername());
        currentUser.setPhone(user.getPhone());
        currentUser.setEmail(user.getEmail());
        currentUser.setPrimary_address(user.getPrimary_address());
        currentUser.setPhoto_url(user.getPhoto_url());

        //this fills in the missing fields from the user object from form
        user.setId(currentUser.getId());
//        user.setPhoto_url(currentUser.getPhoto_url());
        user.setAccumulated_points(currentUser.getAccumulated_points());
        user.setRedeemed_points(currentUser.getRedeemed_points());
        user.setPassword(currentUser.getPassword());

        userDao.save(user);
            redirectAttributes.addAttribute("profileUpdated", true);
        return "redirect:/edit-profile";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword, @RequestParam String password, RedirectAttributes redirectAttributes){
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userDao.findById(loggedInUser.getId());
        if(optionalUser.isEmpty()) {
            //TODO error page
            return "redirect:/login";
        }

        String currentPassword = optionalUser.get().getPassword();

        boolean passwordCorrect = passwordEncoder.matches(password, currentPassword);
        if(passwordCorrect) {
            String newPasswordHash = passwordEncoder.encode(newPassword);
            optionalUser.get().setPassword(newPasswordHash);
            userDao.save(optionalUser.get());
            redirectAttributes.addAttribute("passwordUpdated", true);
        } else {
            redirectAttributes.addAttribute("incorrectPassword", true);
            return "redirect:/edit-profile";
        }
        return "redirect:/edit-profile";
    }

    @PostMapping("/addAddress")
    public String addAddress(@RequestParam String newAddress) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userDao.findById(loggedInUser.getId());
        if(optionalUser.isEmpty()) {
            //TODO error page
            return "redirect:/login";
        }
        Address address = new Address(newAddress, optionalUser.get());
        addressDao.save(address);


        return "redirect:/edit-profile";
    }


    @GetMapping("/updatePrimaryAddress")
    public String updateAddress(@RequestParam("newAddress") String newAddress) {
        System.out.println("\n\n\n\n" + newAddress + "\n\n\n\n");
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getId() == 0) {
            return "redirect:/login";
        }
        Optional<User> optionalUser = userDao.findById(currentUser.getId());
        if(optionalUser.isEmpty()) {
            //TODO error page
            return "redirect:/login";
        }

        optionalUser.get().setPrimary_address(newAddress);
        currentUser.setPrimary_address(newAddress);
        userDao.save(optionalUser.get());

        return "redirect:/edit-profile";
    }

    @GetMapping("/deleteAddress")
    public String deleteAddress(@RequestParam("addressId") String addressId) {
        System.out.println("\n\n\n\n" + addressId + "\n\n\n\n");
        Optional<Address> optionalAddress = addressDao.findById(Long.valueOf(addressId));
        if(optionalAddress.isEmpty()) {
            //TODO error page
            return "redirect:/";
        }

        addressDao.delete(optionalAddress.get());

        return "redirect:/edit-profile";
    }




}