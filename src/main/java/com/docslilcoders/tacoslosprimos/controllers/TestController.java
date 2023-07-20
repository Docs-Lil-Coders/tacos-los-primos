package com.docslilcoders.tacoslosprimos.controllers;

import com.docslilcoders.tacoslosprimos.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    private final UserRepository userDao;

    private final AddressRepository addressDao;
    private final MenuItemOptionRepository menuitemOptionDao;
    private final MenuItemRepository menuItemDao;
    private final OrderedItemRepository orderedItemDao;
    private final OrderRepository orderDao;
    public TestController(UserRepository userDao,
                          AddressRepository addressDao,
                          MenuItemOptionRepository menuitemOptionDao,
                          MenuItemRepository menuItemDao,
                          OrderedItemRepository orderedItemDao,
                          OrderRepository orderDao) {
        this.userDao = userDao;
        this.addressDao = addressDao;
        this.menuitemOptionDao = menuitemOptionDao;
        this.menuItemDao = menuItemDao;
        this.orderedItemDao = orderedItemDao;
        this.orderDao = orderDao;
    }


//PLEASE DO NOT DELETE ANYONE ELSE'S TEST METHODS. JUST ADD A NEW ONE WITH A BS URL TO TEST YOUR TEST FILES :)


    //please don't delete this one, it's just an example for copy/paste and then edit the pasted one to fit
    @GetMapping("/yourTestUrl")
    public String exampleMethodName() {
        return "yourTestFile";
    }

    @GetMapping("/testMenuItems")
    public String getMenuItems(Model model) {
        model.addAttribute("menuItems", menuItemDao.findAll());
        return "test/menuItemTest";
    }

    @GetMapping("/testMenuItemsModal")
    public String getMenuItemModal(Model model) {
        model.addAttribute("menuItems", menuItemDao.findAll());
        return "test/optionModalTest";
    }
}