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
@Table(name = "addresses_updated")
public class AddressUpdated {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = true)
    private String street;

    @Column(nullable = true)
    private String building;

    @Column(nullable = true)
    private String city;

    @Column(nullable = true)
    private String state;

    @Column(nullable = true)
    private String zipCode;

    @Column(nullable = true)
    private Boolean isPrimary;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public AddressUpdated(String street, String building, String city, String state, String zipCode, Boolean isPrimary, User user) {
        this.street = street;
        this.building = building;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.isPrimary = isPrimary;
        this.user = user;
    }

    @Override
    public String toString() {
        return "AddressUpdated{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", building='" + building + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", isPrimary=" + isPrimary +
                ", user=" + user +
                '}';
    }

    public String oneLiner(){
        String addressString = "";
        addressString += street;
        if(building != null && !building.trim().equals("")){
            addressString += ", " + building;
        }
        addressString += ", " + city + ", " + state + " " + zipCode;
        return addressString;
    }
}