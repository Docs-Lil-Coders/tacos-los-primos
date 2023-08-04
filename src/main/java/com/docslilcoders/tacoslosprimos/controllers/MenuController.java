package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.models.MenuItem;
import com.docslilcoders.tacoslosprimos.models.NutritionInformation;
import com.docslilcoders.tacoslosprimos.repositories.MenuItemRepository;
import com.docslilcoders.tacoslosprimos.repositories.NutritionInformationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class MenuController {

    private final MenuItemRepository menuItemDao;
    private final NutritionInformationRepository nutritionInformationDao;

    public MenuController(MenuItemRepository menuItemDao, NutritionInformationRepository nutritionInformationDao) {
        this.menuItemDao = menuItemDao;
        this.nutritionInformationDao = nutritionInformationDao;
    }


    @GetMapping("/menu")
    public String getMenuPage(Model model) {
        model.addAttribute("menuItems", menuItemDao.findAll());
        model.addAttribute("pageTitle", "Menu");

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
