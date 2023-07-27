package com.docslilcoders.tacoslosprimos.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItem {
    private MenuItem menuItem;

//    private Long menuItemId;
    private String meatOptionList;
    private int quantity;

    public double getItemTotal() {
        return menuItem.getPrice() * quantity;
    }

}
