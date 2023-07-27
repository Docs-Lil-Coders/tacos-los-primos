package com.docslilcoders.tacoslosprimos.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShoppingCart {
    private List<CartItem> items = new ArrayList<>();

    public double getCartTotal() {
        double total = 0.0;
        for(int i = 0; i < items.size(); i++) {
            total += items.get(i).getItemTotal();
        }
        return total;
    }
}
