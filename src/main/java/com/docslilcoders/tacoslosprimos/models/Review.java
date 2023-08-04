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
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = true)
    private int stars;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String review;

    public Review(String review, int stars){
        this.review = review;
        this.stars = stars;
    }
}
