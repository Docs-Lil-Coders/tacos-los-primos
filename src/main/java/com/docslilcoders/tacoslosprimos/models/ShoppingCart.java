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

    private static double deliveryCharge = 10.0;
    private static double taxes = .0625;

    private boolean deliveryOrder = false;

    private String promoCodeApplied;

    private int rewardsPointsApplied;




    public double getCartTotal() {
        double total = 0.0;
        for(int i = 0; i < items.size(); i++) {
            total += items.get(i).getItemTotal();
        }
        return total;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "items=" + items +
                ", deliveryOrder=" + deliveryOrder +
                ", promoCodeApplied='" + promoCodeApplied + '\'' +
                ", rewardsPointsApplied=" + rewardsPointsApplied +
                ", cartSubTotal=" + getCartTotal() +
                '}';
    }
}
