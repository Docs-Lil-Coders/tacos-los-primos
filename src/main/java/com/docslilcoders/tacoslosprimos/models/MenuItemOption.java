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
@Table(name = "menu_items_options")
public class MenuItemOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String option_name;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private optionCat optionCategory;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;


    public enum optionCat {
        MEAT, TOPPING, OTHER
    }


}

