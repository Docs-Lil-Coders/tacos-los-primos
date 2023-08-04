package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.models.CateringEmail;
import com.docslilcoders.tacoslosprimos.models.Developer;
import com.docslilcoders.tacoslosprimos.models.Review;
import com.docslilcoders.tacoslosprimos.models.User;
import com.docslilcoders.tacoslosprimos.repositories.DeveloperRepository;
import com.docslilcoders.tacoslosprimos.repositories.ReviewsRepository;
import com.docslilcoders.tacoslosprimos.services.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Optional;

@Controller
public class InfoController {

    private final DeveloperRepository developerDao;
    private final ReviewsRepository reviewDao;
    @Value("${spring.sendgrid.api-key}")
    private String mailKey;

    public InfoController(DeveloperRepository developerDao, ReviewsRepository reviewDao){
        this.developerDao = developerDao;
        this.reviewDao = reviewDao;
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
    public String getCateringPage(Model model) {
        model.addAttribute("cateringEmail", new CateringEmail());
        return "info/catering";
    }

    @PostMapping("/catering")
    public String sendCateringEmail(@ModelAttribute CateringEmail cateringEmail) throws IOException {
        EmailService.sendCateringEmail(cateringEmail, mailKey);
        return "redirect:/catering";
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
    public String getReviewsPage(Model model) {
        model.addAttribute("reviews", reviewDao.findAll());
        return "info/reviews";
    }

    @PostMapping("/reviews")
    public String createReview( @RequestParam(name = "ratingInput") int ratingInput, @RequestParam(name = "reviewText") String reviewText){
        Review newReview = new Review(reviewText, ratingInput);
        reviewDao.save(newReview);
        return "redirect:/reviews";
    }
}