package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.models.*;
import com.docslilcoders.tacoslosprimos.repositories.AddressRepository;
import com.docslilcoders.tacoslosprimos.repositories.AddressUpdatedRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserRepository userDao;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressDao;
    private final OrderRepository orderDao;
    private final AddressUpdatedRepository addressUpdatedDao;

    List<String> allStates = Arrays.asList(
            "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware",
            "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky",
            "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri",
            "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
            "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island",
            "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington",
            "West Virginia", "Wisconsin", "Wyoming"
    );


    public UserController(UserRepository userDao,
                          PasswordEncoder passwordEncoder,
                          AddressRepository addressDao,
                          AddressUpdatedRepository addressUpdatedDao,
                          OrderRepository orderDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.addressDao = addressDao;
        this.orderDao = orderDao;
        this.addressUpdatedDao = addressUpdatedDao;
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
//        model.addAttribute("user", new User());
//        model.addAttribute("addressUpdated", new AddressUpdated());
        UserAddressWrapper wrapper = new UserAddressWrapper(new User(), new AddressUpdated());
        model.addAttribute("userAddressWrapper",  wrapper);
        model.addAttribute("pageTitle", "Register");

        model.addAttribute("allStates", allStates);
        return "users/register";
    }
    @PostMapping("/register")
    public String saveUser(@ModelAttribute UserAddressWrapper userAddressWrapper,
                           Model model){

        System.out.println(userAddressWrapper.getUser());
        System.out.println(userAddressWrapper.getAddressUpdated());
        User user = userAddressWrapper.getUser();
        AddressUpdated addressUpdated = userAddressWrapper.getAddressUpdated();
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
            model.addAttribute("allStates", allStates);
            return "users/register";
        } else {
            user.setPrimary_address("");
            String hash = passwordEncoder.encode(user.getPassword());
            user.setPassword(hash);
            //give the user some points for signing up
            user.setAccumulated_points(50);
            userDao.save(user);
            addressUpdated.setUser(user);
            addressUpdated.setIsPrimary(true);
            addressUpdatedDao.save(addressUpdated);
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
        model.addAttribute("primaryAddress", addressUpdatedDao.findByUserIdAndIsPrimaryTrue(user.getId()));
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

        AddressUpdated addressUpdated = addressUpdatedDao.findByUserIdAndIsPrimaryTrue(user.getId());
        System.out.println(addressUpdated);

        model.addAttribute("usernameTaken", usernameTaken);
        model.addAttribute("emailTaken", emailTaken);
        model.addAttribute("incorrectPassword", incorrectPassword);
        model.addAttribute("profileUpdated", profileUpdated);
        model.addAttribute("passwordUpdated", passwordUpdated);
        model.addAttribute("currentUser", optionalUser.get());
        model.addAttribute("allStates", allStates);
        model.addAttribute("pageTitle", "Edit Profile");
        model.addAttribute("savedAddresses", addressUpdatedDao.findByUserIdAndIsPrimaryFalse(user.getId()));
        UserAddressWrapper wrapper = new UserAddressWrapper(user, addressUpdated);
        model.addAttribute("userAddressWrapper", wrapper);
        model.addAttribute("addressUpdated", new AddressUpdated());

        return "/users/edit_profile";
    }
    @PostMapping("/edit-profile")
    public String doEdit(@ModelAttribute UserAddressWrapper userAddressWrapper, RedirectAttributes redirectAttributes) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getId() == 0) {
            return "redirect:/login";
        }

        User user = userAddressWrapper.getUser();
        AddressUpdated address = userAddressWrapper.getAddressUpdated();
        System.out.println("\n\n\n\n" + address);
        System.out.println("\n\n\n\n" + user);



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


        //this sets it for the current session
            user.setPrimary_address("");
        currentUser.setFirst_name(user.getFirst_name());
        currentUser.setLast_name(user.getLast_name());
        currentUser.setUsername(user.getUsername());
        currentUser.setPhone(user.getPhone());
        currentUser.setEmail(user.getEmail());
        currentUser.setPrimary_address("");
        currentUser.setPhoto_url(user.getPhoto_url());

        //this fills in the missing fields from the user object from form
        user.setId(currentUser.getId());
        user.setAccumulated_points(currentUser.getAccumulated_points());
        user.setRedeemed_points(currentUser.getRedeemed_points());
        user.setPassword(currentUser.getPassword());

        //updates address
            address.setUser(user);
            address.setIsPrimary(true);
            addressUpdatedDao.save(address);

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

//    @PostMapping("/addAddress")
//    public String addAddress(@RequestParam String newAddress) {
//        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Optional<User> optionalUser = userDao.findById(loggedInUser.getId());
//        if(optionalUser.isEmpty()) {
//            //TODO error page
//            return "redirect:/login";
//        }
//        Address address = new Address(newAddress, optionalUser.get());
//        addressDao.save(address);
//
//
//        return "redirect:/edit-profile";
//    }

    @PostMapping("/saveNewAddress")
    public String saveNewAddress(@ModelAttribute AddressUpdated addressUpdated) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getId() == 0) {
            return "redirect:/login";
        }
                Optional<User> optionalUser = userDao.findById(currentUser.getId());
        if(optionalUser.isEmpty()) {
            //TODO error page
            return "redirect:/login";
        }
        addressUpdated.setUser(optionalUser.get());
        addressUpdated.setIsPrimary(false);
        addressUpdatedDao.save(addressUpdated);
        return "redirect:/edit-profile";
    }


    @GetMapping("/updatePrimaryAddress")
    public String updateAddress(@RequestParam("newAddress") String newAddress) {
        System.out.println("\n\n\n\n" + newAddress + "\n\n\n\n");
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getId() == 0) {
            return "redirect:/login";
        }
        AddressUpdated currentAddress = addressUpdatedDao.findByUserIdAndIsPrimaryTrue(currentUser.getId());
        Optional<AddressUpdated> newPrimary = addressUpdatedDao.findById(Long.valueOf(newAddress));
        if (newPrimary.isEmpty()) {
            return "redirect:/";
        }
        currentAddress.setIsPrimary(false);
        newPrimary.get().setIsPrimary(true);
        addressUpdatedDao.save(currentAddress);
        addressUpdatedDao.save(newPrimary.get());
        return "redirect:/edit-profile";
    }

    @GetMapping("/deleteAddress")
    public String deleteAddress(@RequestParam("addressId") String addressId) {
        System.out.println("\n\n\n\n" + addressId + "\n\n\n\n");
        Optional<AddressUpdated> optionalAddress = addressUpdatedDao.findById(Long.valueOf(addressId));
        if(optionalAddress.isEmpty()) {
            //TODO error page
            return "redirect:/";
        }

        addressUpdatedDao.delete(optionalAddress.get());

        return "redirect:/edit-profile";
    }


    @GetMapping("/testOrders")
    public String getMenuItems(){
        List<MenuItem> testing = orderDao.findUniqueMenuItemsByUserId(12L);
        System.out.println(testing);
        return "menu/menu";
    }


}