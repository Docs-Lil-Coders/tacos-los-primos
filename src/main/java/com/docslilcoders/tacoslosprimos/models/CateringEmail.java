package com.docslilcoders.tacoslosprimos.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CateringEmail {

    private String name;
    private String contactNumber;
    private String email;
    private String eventType;
    private String eventDate;
    private String numberOfGuests;
    private String location;
    private String preferencesRestrictions;
    private String menu;
    private String additional;

    @Override
    public String toString() {
        return "Catering Request: " +
                "\nName: " + name +
                "\n Contact Number: " + contactNumber +
                "\n Email: " + email +
                "\n Event Type: " + eventType +
                "\n Event Date: " + eventDate +
                "\n Number of Guests" + numberOfGuests +
                "\n Location" + location +
                "\n Dietary Pref/Restrictions: " + preferencesRestrictions +
                "\n Menu Pref: " + menu +
                "\n Additional Comments: " + additional;
    }
}
