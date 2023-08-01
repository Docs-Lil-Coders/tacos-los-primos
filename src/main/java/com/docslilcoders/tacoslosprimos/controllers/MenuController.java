package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.models.MenuItem;
import com.docslilcoders.tacoslosprimos.repositories.MenuItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class MenuController {

    private final MenuItemRepository menuItemDao;

    public MenuController(MenuItemRepository menuItemDao) {
        this.menuItemDao = menuItemDao;
    }


    @GetMapping("/menu")
    public String getMenuPage(Model model) {
        model.addAttribute("menuItems", menuItemDao.findAll());
        return "menu/menu";
    }

    @GetMapping("/menu/{id}")
    public String getViewMorePage(@PathVariable Long id, Model model) {
        Optional<MenuItem> optionalMenuItem = menuItemDao.findById(id);

        if (optionalMenuItem.isEmpty()) {
            System.out.println("Item with id " + id + " not found!");
            return "/error";
        }

        model.addAttribute("item", optionalMenuItem.get());
        return "/menu/view_more";
    }
}
