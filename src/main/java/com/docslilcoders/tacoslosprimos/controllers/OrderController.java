package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.models.*;
import com.docslilcoders.tacoslosprimos.repositories.*;
import com.docslilcoders.tacoslosprimos.services.CartService;
import com.docslilcoders.tacoslosprimos.services.EmailService;
import com.docslilcoders.tacoslosprimos.utility.DateUtils;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
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
    private final OrderedItemRepository orderedItemDao;
    private final AddressUpdatedRepository addressUpdatedDao;

    List<String> allStates = Arrays.asList(
            "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware",
            "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky",
            "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri",
            "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
            "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island",
            "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington",
            "West Virginia", "Wisconsin", "Wyoming"
    );

    @Value("${spring.sendgrid.api-key}")
    private String mailKey;

    public OrderController(MenuItemRepository menuItemDao,
                           CartService cartService,
                           AddressUpdatedRepository addressUpdatedDao,
                           UserRepository userDao,
                           PromoCodeRepository promoCodeDao,
                           OrderRepository orderDao,
                           OrderedItemRepository orderedItemDao) {
        this.menuItemDao = menuItemDao;
        this.cartService = cartService;
        this.userDao = userDao;
        this.promoCodeDao = promoCodeDao;
        this.orderDao = orderDao;
        this.orderedItemDao = orderedItemDao;
        this.addressUpdatedDao = addressUpdatedDao;
    }


    @GetMapping("/checkout")
    public String getAboutPage(HttpSession session, Model model) {
        ShoppingCart cart = cartService.getCart(session);
        model.addAttribute("cart", cart);
        model.addAttribute("allStates", allStates);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                Optional<User> optionalUser = userDao.findById(user.getId());
                if (optionalUser.isEmpty()) {
                    model.addAttribute("pointsAvailable", 0);
                    model.addAttribute("address", new AddressUpdated());
                    model.addAttribute("user", new User());
                } else {
                    model.addAttribute("pointsAvailable", optionalUser.get().getAccumulated_points());
                    model.addAttribute("user", optionalUser.get());
                    model.addAttribute("address", addressUpdatedDao.findByUserIdAndIsPrimaryTrue(optionalUser.get().getId()));
                    model.addAttribute("loggedIn", true);
                    model.addAttribute("allAddresses", addressUpdatedDao.findByUserId(optionalUser.get().getId()));
                    Gson gson = new Gson();
                    model.addAttribute("addressesJSON", gson.toJson(addressUpdatedDao.findAddressesByUserId(optionalUser.get().getId())));

                }
            } else {
                model.addAttribute("pointsAvailable", 0);
                model.addAttribute("address", new AddressUpdated());
                model.addAttribute("user", new User());
            }
        } else {
            model.addAttribute("pointsAvailable", 0);
            model.addAttribute("address", new AddressUpdated());
            model.addAttribute("user", new User());
        }

        List<String> promoCodes = promoCodeDao.findPromoCodeNamesByRedeemedEqualsZero();
        model.addAttribute("promoCodes", promoCodes);
        model.addAttribute("pageTitle", "Checkout");

        return "orders/checkout";
    }

    @GetMapping("/order-status")
    public String getOrderStatusPage(@RequestParam(required = false) boolean orderNotFound, Model model) {
        model.addAttribute("orderNotFound", orderNotFound);
        model.addAttribute("pageTitle", "Order Status");

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
           return "redirect:/order-status?orderNotFound=true";
        }
        model.addAttribute("order", order.get());
        model.addAttribute("pageTitle", "Order Status");

        return "orders/order_status";
    }

    @GetMapping("/view-bag")
    public String getViewBagPage(HttpSession session, Model model) {
        ShoppingCart cart = cartService.getCart(session);
        model.addAttribute("cart", cart);
        model.addAttribute("pageTitle", "View Bag");
        return "orders/view_bag";
    }

    @GetMapping("/orderSummary")
    public String getOrderSummaryPage(@RequestParam("orderId") long orderId,  Model model) {
        // Fetch the order details using the `id`
        Optional<Order> order = orderDao.findById(orderId);
        if (order.isEmpty()) {
            System.out.println("Order not found");
            // TODO: Handle the case when the order is not found, e.g., redirect to an error page
            throw new NoSuchElementException("order item not found");
        }

        // Add the order to the model
        model.addAttribute("order", order.get());
        model.addAttribute("pageTitle", "Order Summary");
        return "orders/orderSummary";
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
                           Model model,
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
        model.addAttribute("orderId", orderId);
        model.addAttribute("guest", guest);
        model.addAttribute("pageTitle", "Order Confirmation");
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
    public String placeOrder( @RequestParam("address") String address,@RequestParam("email") boolean email, @RequestParam("sendTo") String sendTo, HttpSession session) throws IOException {
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
        Optional<User> optionalUser = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                optionalUser = userDao.findById(user.getId());
                if (optionalUser.isEmpty()) {
                   newOrder = new Order(address, status, type, price);
                } else {
                    guest = false;
                    newOrder = new Order(address, status, type, price, optionalUser.get());
                    //update points
                    int currentPoints = optionalUser.get().getAccumulated_points();
                    int currentRedeemedPoints = optionalUser.get().getRedeemed_points();
                    int pointsUsed = cart.getRewardsPointsApplied();
                    int pointsToAdd = 5;
                    optionalUser.get().setAccumulated_points(currentPoints - pointsUsed + pointsToAdd);
                    optionalUser.get().setRedeemed_points(currentRedeemedPoints + pointsUsed);
                    user.setAccumulated_points(optionalUser.get().getAccumulated_points());
                    user.setRedeemed_points(optionalUser.get().getRedeemed_points());
                    userDao.save(optionalUser.get());
                }
            } else {
                newOrder = new Order(address, status, type, price);
            }
        } else {
            newOrder = new Order(address, status, type, price);
        }
        //set the time of the order
        long currentTimestampMillis = System.currentTimeMillis();
        String formattedDateTime = DateUtils.formatDateTime(currentTimestampMillis);
        newOrder.setPlacedAt(formattedDateTime);

        Order savedOrder = orderDao.save(newOrder);
        long orderId = savedOrder.getId();
        for(int i = 0; i < cart.getItems().size(); i++){
            OrderedItem orderedItem = new OrderedItem(cart.getItems().get(i).getQuantity(), cart.getItems().get(i).getMeatOptionList(), cart.getItems().get(i).getMenuItem(), newOrder);
            orderedItemDao.save(orderedItem);
        }
        if(email){
            if(!guest){
                EmailService.sendConfirmationEmail(orderId, optionalUser.get(), sendTo, mailKey);
            } else {
                EmailService.sendConfirmationEmail(orderId, null, sendTo, mailKey);
            }
        }

        cartService.resetCart(session);
        return "?orderId=178913" + orderId + "&guest=" + guest;
    }



}
