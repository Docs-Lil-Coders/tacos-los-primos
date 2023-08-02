package com.docslilcoders.tacoslosprimos.services;

import com.docslilcoders.tacoslosprimos.models.ShoppingCart;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private static final String CART_SESSION_ATTRIBUTE = "shoppingCart";

    public ShoppingCart getCart(HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute(CART_SESSION_ATTRIBUTE, cart);
        }
        return cart;
    }

    public void resetCart(HttpSession session) {
        session.setAttribute(CART_SESSION_ATTRIBUTE, new ShoppingCart());
    }

}