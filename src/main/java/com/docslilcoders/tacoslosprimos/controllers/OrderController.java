package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.models.*;
import com.docslilcoders.tacoslosprimos.repositories.MenuItemRepository;
import com.docslilcoders.tacoslosprimos.repositories.OrderRepository;
import com.docslilcoders.tacoslosprimos.repositories.PromoCodeRepository;
import com.docslilcoders.tacoslosprimos.repositories.UserRepository;
import com.docslilcoders.tacoslosprimos.services.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Controller
public class OrderController {

    private CartService cartService;
    private final MenuItemRepository menuItemDao;
    private final UserRepository userDao;
    private final PromoCodeRepository promoCodeDao;
    private final OrderRepository orderDao;

    public OrderController(MenuItemRepository menuItemDao, CartService cartService, UserRepository userDao, PromoCodeRepository promoCodeDao, OrderRepository orderDao) {

        this.menuItemDao = menuItemDao;
        this.cartService = cartService;
        this.userDao = userDao;
        this.promoCodeDao = promoCodeDao;
        this.orderDao = orderDao;
    }


    @GetMapping("/checkout")
    public String getAboutPage(HttpSession session, Model model) {
        ShoppingCart cart = cartService.getCart(session);
        model.addAttribute("cart", cart);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                Optional<User> optionalUser = userDao.findById(user.getId());
                if (optionalUser.isEmpty()) {
                    model.addAttribute("pointsAvailable", 0);
                    model.addAttribute("user", new User());
                } else {
                    model.addAttribute("pointsAvailable", optionalUser.get().getAccumulated_points());
                    model.addAttribute("user", optionalUser.get());
                }
            } else {
                model.addAttribute("pointsAvailable", 0);
                model.addAttribute("user", new User());
            }
        } else {
            model.addAttribute("pointsAvailable", 0);
            model.addAttribute("user", new User());
        }

        List<String> promoCodes = promoCodeDao.findPromoCodeNamesByRedeemedEqualsZero();
        model.addAttribute("promoCodes", promoCodes);

        return "orders/checkout";
    }

    @GetMapping("/order-status")
    public String getOrderStatusPage(@RequestParam(required = false) boolean orderNotFound, Model model) {
        model.addAttribute("orderNotFound", orderNotFound);
        return "orders/order_status";
    }

    @PostMapping("/order-status")
    public String getOrderStatusPage(@RequestParam("orderId") String orderId, Model model) {
        if(orderId.length() < 7) {
            return "redirect:/order-status?orderNotFound=true";
        }
        String trimmedNumber = orderId.substring(6);
        long result = Long.parseLong(trimmedNumber);
        Optional<Order> order = orderDao.findById(result);
        if (order.isEmpty()) {
            System.out.println("item not found");
            //TODO error page
           return "redirect:/order-status?orderNotFound=true";
        }
        model.addAttribute("order", order.get());
        return "orders/order_status";
    }

    @GetMapping("/view-bag")
    public String getViewBagPage(HttpSession session, Model model) {
        ShoppingCart cart = cartService.getCart(session);
        model.addAttribute("cart", cart);

        return "orders/view_bag";
    }

    @GetMapping("/addToBag")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addToCart(@RequestParam("menuItem") String menuItemId,
                            @RequestParam("quantity") String quantity,
                            @RequestParam("options") String options,
                            HttpSession session) {
        ShoppingCart cart = cartService.getCart(session);
        Long itemId = Long.valueOf(menuItemId);
        int quantityValue = Integer.parseInt(quantity);
        Optional<MenuItem> menuItem = menuItemDao.findById(itemId);
        if (menuItem.isEmpty()) {
            System.out.println("menu item not found");
            //TODO error page
            throw new NoSuchElementException("Menu item not found");
        }
        CartItem cartItem = new CartItem(menuItem.get(), options, quantityValue);
        cart.getItems().add(cartItem);
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

    @GetMapping("/thankYou")
    public String showConfirmation(@RequestParam("orderId") String orderId, @RequestParam("guest") boolean guest, Model model) {
        System.out.println("\n\n\n" + orderId);
        System.out.println("\n\n\n" + guest);
        model.addAttribute("orderId", orderId);
        model.addAttribute("guest", guest);
        return "orders/orderConfirmation";
    }

    @GetMapping("/updateCartFinal")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCart(@RequestParam("orderType") String orderType,
                     @RequestParam("promoCode") String promoCode,
                     @RequestParam("pointsRedeemed") String pointsRedeemed,
                     HttpSession session) {
        ShoppingCart cart = cartService.getCart(session);
        if (orderType.equals("delivery")) {
            cart.setDeliveryOrder(true);
        } else {
            cart.setDeliveryOrder(false);
        }
        cart.setPromoCodeApplied(promoCode);
        cart.setRewardsPointsApplied(Integer.parseInt(pointsRedeemed));
    }

    @GetMapping("/placeOrder")
    @ResponseBody
    public String placeOrder( @RequestParam("address") String address, HttpSession session) {
        ShoppingCart cart = cartService.getCart(session);
        boolean guest = true;
        Order.orderStatus status = Order.orderStatus.PLACED;
        Order.orderType type;
        if(cart.getDeliveryOrder()){
            type = Order.orderType.DELIVERY;
        } else {
            type = Order.orderType.PICKUP;
        }

        double price = cart.getCompleteTotal();
        Order newOrder;


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                Optional<User> optionalUser = userDao.findById(user.getId());
                if (optionalUser.isEmpty()) {
                   newOrder = new Order(address, status, type, price);
                } else {
                    guest = false;
                    newOrder = new Order(address, status, type, price, optionalUser.get());
                    //update points
                    int currentPoints = optionalUser.get().getAccumulated_points();
                    int pointsUsed = cart.getRewardsPointsApplied();
                    int pointsToAdd = 5;
                    optionalUser.get().setAccumulated_points(currentPoints - pointsUsed + pointsToAdd);
                    userDao.save(optionalUser.get());
                }
            } else {
                newOrder = new Order(address, status, type, price);
            }
        } else {
            newOrder = new Order(address, status, type, price);
        }

        long timestamp = System.currentTimeMillis();

        System.out.println("Current Timestamp: " + timestamp);
        Order savedOrder = orderDao.save(newOrder);
        long orderId = savedOrder.getId();
        cartService.resetCart(session);
        return "?orderId=178913" + orderId + "&guest=" + guest;
    }

}
