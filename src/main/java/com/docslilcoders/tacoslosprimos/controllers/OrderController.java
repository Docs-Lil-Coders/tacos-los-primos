package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.models.CartItem;
import com.docslilcoders.tacoslosprimos.models.MenuItem;
import com.docslilcoders.tacoslosprimos.models.ShoppingCart;
import com.docslilcoders.tacoslosprimos.models.User;
import com.docslilcoders.tacoslosprimos.repositories.MenuItemRepository;
import com.docslilcoders.tacoslosprimos.services.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class OrderController {

    private CartService cartService;

    private final MenuItemRepository menuItemDao;

    public OrderController(MenuItemRepository menuItemDao, CartService cartService) {

        this.menuItemDao = menuItemDao;
        this.cartService = cartService;
    }


    @GetMapping("/checkout")
    public String getAboutPage() {
        return "orders/checkout";
    }

    @GetMapping("/order-status")
    public String getOrderStatusPage() {
        return "orders/order_status";
    }

    @GetMapping("/view-bag")
    public String getViewBagPage(HttpSession session, Model model) {
        ShoppingCart cart = cartService.getCart(session);
        model.addAttribute("cart", cart);

        return "orders/view_bag";
//        return "test/view_bag_test";
    }

    @GetMapping("/addToBag")
    public String addToCart(@RequestParam("menuItem") String menuItemId,
                            @RequestParam("quantity") String quantity,
                            @RequestParam("options") String options,
                            HttpSession session) {
        ShoppingCart cart = cartService.getCart(session);
        Long itemId = Long.valueOf(menuItemId);
        int quantityValue = Integer.parseInt(quantity);
        Optional<MenuItem> menuItem = menuItemDao.findById(itemId);
        if(menuItem.isEmpty()) {
            System.out.println("menu item not found");
            //TODO error page
            return "menu/menu";
        }
        CartItem cartItem = new CartItem(menuItem.get(), options, quantityValue);
        cart.getItems().add(cartItem);

        return "redirect:/view-bag"; // Redirect to the cart page after adding the product
    }

    @GetMapping("/editBag")
    public String editCart(@RequestParam("quantity") String quantity,
                           @RequestParam("index") String index,
                           HttpSession session) {
        ShoppingCart cart = cartService.getCart(session);
        int itemIndex = Integer.parseInt(index);
        int itemQuantity = Integer.parseInt(quantity);
        cart.getItems().get(itemIndex).setQuantity(itemQuantity);
        return "redirect:/view-bag";
    }

    @GetMapping("/removeItem")
    public String removeFromCart(@RequestParam("index") String index,
                           HttpSession session) {
        ShoppingCart cart = cartService.getCart(session);
        int itemIndex = Integer.parseInt(index);
        cart.getItems().remove(itemIndex);
        return "redirect:/view-bag";
    }

}
