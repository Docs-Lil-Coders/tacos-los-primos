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

        if(contactUsEmail.isEmpty()) {
            contactUsEmail = "Not Entered";
        }
        if(contactUsName.isEmpty()) {
            contactUsName = "Not Entered";
        }
        if(contactUsSubject.isEmpty()) {
            contactUsSubject = "Not Entered";
        }
        if(contactUsText.isEmpty()) {
            contactUsText = "Not Entered";
        }

        contactUsSubject = "From Contact Us Page: " +  contactUsSubject;

        String body = "Senders Name: " + contactUsName + "\n\nSenders Reply-To Email: " + contactUsEmail + "\n\nSenders Message: " + contactUsText;
        EmailService.sendEmail(contactUsSubject, "tacoslosprimosrestaurante@gmail.com", body, mailKey);

        return "index";
    }


}