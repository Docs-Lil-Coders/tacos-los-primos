package com.docslilcoders.tacoslosprimos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {

    @GetMapping("/about-us")
    public String getAboutPage() {
        return "info/about_us";
    }

    @GetMapping("/careers")
    public String getCareersPage() {
        return "info/careers";
    }

    @GetMapping("/catering")
    public String getCateringPage() {
        return "info/catering";
    }
}