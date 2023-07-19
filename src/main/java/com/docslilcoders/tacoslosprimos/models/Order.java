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
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private double totalPrice;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private orderStatus orderStatus;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private orderType orderType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderedItem> orderedItems;


    public enum orderStatus {
        PLACED ,CONFIRMED, BEING_PREPARED ,PICKUP_READY, OUT_FOR_DELIVERY, DELIVERED, COMPLETE ,CANCELED
    }

    public enum orderType {
      PICKUP, DELIVERY
    }


}

