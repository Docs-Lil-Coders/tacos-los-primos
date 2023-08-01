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

    @Column(nullable = true)
    private Double saturatedFat;

    @Column(nullable = true)
    private Double dietaryFiber;

    @Column(nullable = true)
    private Double calcium;

    @Column(nullable = true)
    private Double transFat;

    @Column(nullable = true)
    private Double totalSugars;

    @Column(nullable = true)
    private Double addedSugars;

    @Column(nullable = true)
    private Double iron;

    @Column(nullable = true)
    private Double cholesterol;

    @Column(nullable = true)
    private Double vitaminD;

    @Column(nullable = true)
    private Double potassium;

    @Column(nullable = true)
    private Double sodium;


    @OneToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

}