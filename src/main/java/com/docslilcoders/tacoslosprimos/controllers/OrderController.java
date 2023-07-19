package com.docslilcoders.tacoslosprimos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

    @GetMapping("/checkout")
    public String getAboutPage() {
        return "orders/checkout";
    }

    @GetMapping("/order-status")
    public String getOrderStatusPage() {
        return "orders/order_status";
    }

    @GetMapping("/view-bag")
    public String getViewBagPage() {
        return "orders/view_bag";
    }
}
