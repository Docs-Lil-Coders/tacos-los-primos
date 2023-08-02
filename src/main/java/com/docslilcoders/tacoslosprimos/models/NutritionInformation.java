package com.docslilcoders.tacoslosprimos.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "nutrition_information")
public class NutritionInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = true)
    private Double calories;

    @Column(nullable = true)
    private Double totalFat;

    @Column(nullable = true)
    private Double totalCarbs;

    @Column(nullable = true)
    private Double protein;


    @OneToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

}