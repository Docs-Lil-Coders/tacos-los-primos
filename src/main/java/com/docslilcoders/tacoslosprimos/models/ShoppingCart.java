package com.docslilcoders.tacoslosprimos.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;
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


    public double getCompleteTotal() {
        double total = 0.0;
        double subTotal = getCartTotal();
        total += subTotal;
        if (deliveryOrder) {
            total += deliveryCharge;
        }
        double tax = total * taxes;
        total += tax;
        total -= getPromoCodeValue();
        total -= getPointsRedeemedValue();
        return Math.round(total * 100.0) / 100.0;
    }

    public boolean getDeliveryOrder() {
        return deliveryOrder;
    }

    public double getPromoCodeValue() {
        double value = 0.0;
        if (promoCodeApplied.equals("N/A")) {
            return value;

        } else {
            value = switch (promoCodeApplied.charAt(0)) {
                case 'X' -> 5.0;
                case 'Y' -> 10.0;
                case 'Z' -> 15.0;
                case 'A' -> 20.0;
                default -> 0.0;
            };
            return value;
        }
    }

    public double getPointsRedeemedValue() {
        return rewardsPointsApplied * .10;
    }


    public double getCartTotal() {
        double total = 0.0;
        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getItemTotal();
        }
        return Math.round(total * 100.0) / 100.0;
    }

    public String getCartTotalString() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(getCartTotal());
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
