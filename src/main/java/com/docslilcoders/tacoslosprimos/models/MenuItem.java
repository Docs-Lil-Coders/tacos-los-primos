package com.docslilcoders.tacoslosprimos.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String item_name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private mainCat mainCategory;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private subCat subCategory;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private meatReq meatReq;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private toppingsReq toppingsReq;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "menuItem")
    private List<MenuItemOption> menuItemOptions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "menuItem")
    private List<OrderedItem> orderedItems;


    public enum mainCat {
        APPETIZERS, ENTREES, SIDES, SALSAS, BEVERAGES
    }

    public enum subCat {
        CHIPS, SODAS, COFFEE, AGUAFRESCAS, OTHER
    }

    public enum meatReq {
        YES, NO
    }

    public enum toppingsReq {
        YES, NO
    }

}
