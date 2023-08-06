package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.models.AddressUpdated;
import com.docslilcoders.tacoslosprimos.models.MenuItem;
import com.docslilcoders.tacoslosprimos.models.NutritionInformation;
import com.docslilcoders.tacoslosprimos.models.User;
import com.docslilcoders.tacoslosprimos.repositories.MenuItemRepository;
import com.docslilcoders.tacoslosprimos.repositories.NutritionInformationRepository;
import com.docslilcoders.tacoslosprimos.repositories.OrderRepository;
import com.docslilcoders.tacoslosprimos.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class MenuController {

    private final MenuItemRepository menuItemDao;
    private final NutritionInformationRepository nutritionInformationDao;
    private final OrderRepository orderDao;

    private final UserRepository userDao;

    public MenuController(MenuItemRepository menuItemDao,
                          NutritionInformationRepository nutritionInformationDao,
                          OrderRepository orderDao,
                          UserRepository userDao) {
        this.menuItemDao = menuItemDao;
        this.nutritionInformationDao = nutritionInformationDao;
        this.orderDao = orderDao;
        this.userDao = userDao;
    }


    @GetMapping("/menu")
    public String getMenuPage(Model model) {
        model.addAttribute("menuItems", menuItemDao.findAll());
        model.addAttribute("pageTitle", "Menu");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                Optional<User> optionalUser = userDao.findById(user.getId());
                if (optionalUser.isEmpty()) {
                    model.addAttribute("previousOrders", false);
                } else {
                    model.addAttribute("previousOrders", true);
                    List<MenuItem> previousItems = orderDao.findUniqueMenuItemsByUserId(optionalUser.get().getId());
                    model.addAttribute("previousItems", previousItems);
                }
            } else {
                model.addAttribute("previousOrders", false);

            }
        } else {
            model.addAttribute("previousOrders", false);

        }

        return "menu/menu";
    }

    @GetMapping("/menu/{id}")
    public String getViewMorePage(@PathVariable Long id, Model model) {
        Optional<MenuItem> optionalMenuItem = menuItemDao.findById(id);

        if (optionalMenuItem.isEmpty()) {
            System.out.println("Item with id " + id + " not found!");
            return "/error";
        }

        NutritionInformation nutritionInformation = nutritionInformationDao.findByMenuItemId(id);

        model.addAttribute("nutritionInformation", nutritionInformation);
        model.addAttribute("item", optionalMenuItem.get());
        model.addAttribute("pageTitle", optionalMenuItem.get().getItem_name());
        return "/menu/view_more";
    }
}
