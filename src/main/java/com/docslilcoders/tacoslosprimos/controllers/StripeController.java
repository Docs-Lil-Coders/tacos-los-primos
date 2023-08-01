package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.models.StripePaymentResponse;
import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class StripeController {

    @Value("${stripe.secret.key}")
    private String stripeKey;

    @GetMapping("/stripe-test")
    public String checkoutTest (Model model){
        return "test/apiTest";
    }

    @GetMapping(value = "/stripe-token" , produces = "application/json")
    @ResponseBody
    public String createPaymentIntent()//PASS IN ORDER ID AS PARAMETER... cart on the sessio
     {
         //FETCH ORDER FROM DATABASE
        Gson gson = new Gson();
        Stripe.apiKey = stripeKey;

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .setCurrency("USD")
                .setAmount(3333L) //USE ORDER TOTAL amount of order
                .build();

        try {
            // Create a PaymentIntent with the order amount and currency
            PaymentIntent intent = PaymentIntent.create(params);

            // Send PaymentIntent details to client
            return gson.toJson(new StripePaymentResponse(intent.getClientSecret()));
        } catch(StripeException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
