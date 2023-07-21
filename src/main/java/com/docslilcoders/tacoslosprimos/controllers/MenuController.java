package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.repositories.MenuItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/view-more/{id}")
    public String getViewMorePage(@PathVariable String id) {
        return "menu/view_more";
    }
}
