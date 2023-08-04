package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.services.EmailService;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class HomeController {

    @Value("${spring.sendgrid.api-key}")
    private String mailKey;


    @GetMapping()
    public String welcome() {
        return "index";
    }

    @PostMapping("/contactUs")
    public String contactForm(@RequestParam("contactUsName") String contactUsName,
                              @RequestParam("contactUsSubject") String contactUsSubject,
                              @RequestParam("contactUsEmail") String contactUsEmail,
                              @RequestParam("contactUsText") String contactUsText) throws IOException {

    EmailService.sendContactUsEmail(contactUsName, contactUsSubject, contactUsEmail, contactUsText, mailKey);

        return "index";
    }


}