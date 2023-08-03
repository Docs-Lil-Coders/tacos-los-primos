package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.models.Developer;
import com.docslilcoders.tacoslosprimos.repositories.DeveloperRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class InfoController {

    private final DeveloperRepository developerDao;

    public InfoController(DeveloperRepository developerDao){
        this.developerDao = developerDao;
    }

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

    @GetMapping("/contact-info")
    public String getContactInfoPage() {
        return "info/contact_info";
    }

    @GetMapping("/developers")
    public String getDevelopersPage(Model model) {
        Optional<Developer> naime = developerDao.findById(1L);
        if (naime.isEmpty()){
            return "redirect:/login";
        }
        Optional<Developer> maddie = developerDao.findById(2L);
        if (maddie.isEmpty()){
            return "redirect:/login";
        }
        Optional<Developer> melissa = developerDao.findById(3L);
        if (melissa.isEmpty()){
            return "redirect:/login";
        }
        Optional<Developer> jakira = developerDao.findById(4L);
        if (jakira.isEmpty()){
            return "redirect:/login";
        }

        model.addAttribute("naime", naime.get());
        model.addAttribute("maddie", maddie.get());
        model.addAttribute("melissa", melissa.get());
        model.addAttribute("jakira", jakira.get());
        return "info/developers";
    }

    @GetMapping("/faqs")
    public String getFaqsPage() {
        return "info/faqs";
    }

    @GetMapping("/reviews")
    public String getReviewsPage() {
        return "info/reviews";
    }
}