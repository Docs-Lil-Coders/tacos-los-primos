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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String first_name;

    @Column(nullable = false)
    private String last_name;

    @Column(nullable = false)
    private long phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String photo_url;

    @Column(nullable = false)
    private int accumulated_points;

    @Column(nullable = false)
    private int redeemed_points;

    @Column(nullable = false)
    private String primary_address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Address> usersAddresses;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Order> userOrders;

//  public User(User copy) {
//      id = copy.id; // This line is SUPER important! Many things won't work if it's absent
//      first_name = copy.first_name;
//      last_name = copy.last_name;
//      phone = copy.phone;
//      email = copy.email;
//      password = copy.password;
//      photo_url = copy.photo_url;
//      accumulated_points = copy.accumulated_points;
//      redeemed_points = copy.redeemed_points;
//      primary_address = copy.primary_address;
//  }


}
