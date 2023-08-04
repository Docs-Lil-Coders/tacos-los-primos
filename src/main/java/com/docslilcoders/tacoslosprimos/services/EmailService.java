package com.docslilcoders.tacoslosprimos.services;

import com.docslilcoders.tacoslosprimos.models.CateringEmail;
import com.docslilcoders.tacoslosprimos.models.User;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;
import java.net.URLEncoder;

public class EmailService {

    public static void sendEmail(String subject, String emailTo, String emailContent, String mailKey) throws IOException {
        Email from = new Email("tacoslosprimosrestaurante@gmail.com");
        Email to = new Email(emailTo);
        Content content = new Content("text/plain", emailContent);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(mailKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
//            Response response = sg.api(request);
//            System.out.println(response.getStatusCode());
//            System.out.println(response.getBody());
//            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }

    public static void sendContactUsEmail(String contactUsName, String contactUsSubject, String contactUsEmail, String contactUsText, String mailKey) throws IOException {
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
        sendEmail(contactUsSubject, "tacoslosprimosrestaurante@gmail.com", body, mailKey);

    }
    public static void sendCateringEmail(CateringEmail cateringEmail, String mailKey) throws IOException {
        String body = cateringEmail.toString();

    sendEmail("Catering Request", "tacoslosprimosrestaurante@gmail.com", body, mailKey);

    }

    public static void sendConfirmationEmail(long orderNumber, User user, String sendTo, String mailKey) throws IOException {
        System.out.println("\n\n\n\n" + sendTo);

        String intro;
        String warning = "";

        if(user == null){
          intro = "Dear Valued Customer,\n\n";
        } else {
          intro = "Dear " + user.getFirst_name() + " " + user.getLast_name() + ",\n\n";
          warning = "Didn't place this order? Please let us know immediately, and we'll investigate the matter to ensure your account's security.\n\n";
        }

        String body = intro + "We are absolutely delighted to share the wonderful news! Your order has been successfully placed and is now in the capable hands of our dedicated team.\n\n" +
                "Your Order Number Is: 178913" + orderNumber + "\n\n" +
                "Our talented cooks are already hard at work, preparing your delectable treats with love and care. Your satisfaction is our top priority, and we're committed to making this a delightful dining experience for you.\n\n" +
                "To stay in the loop and receive the latest updates on your order status, simply use your unique order number to track your delivery on our website!\n\n" +
                "If you have any questions or need assistance with your order, please don't hesitate to reach out to our friendly customer support team. We're here to ensure that you have a delightful culinary journey with us!\n\n" +
                warning +
                "Thank you for choosing Tacos Los Primos. We appreciate your business and can't wait to serve you the most mouthwatering dishes you've ever tasted!\n\n" +
                "Savor the flavors, \nThe Tacos Los Primos Team";

        sendEmail("Order Confirmation", sendTo, body, mailKey);
    }


}
