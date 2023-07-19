package com.docslilcoders.tacoslosprimos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MenuController {

    @GetMapping("/menu")
    public String getAboutPage() {
        return "menu/menu";
    }

    @GetMapping("/view-more/{id}")
    public String getViewMorePage(@PathVariable String id) {
        return "menu/view_more";
    }
}
