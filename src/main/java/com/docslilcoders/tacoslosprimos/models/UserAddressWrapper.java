package com.docslilcoders.tacoslosprimos.models;

public class UserAddressWrapper {
    private User user;
    private AddressUpdated addressUpdated;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AddressUpdated getAddressUpdated() {
        return addressUpdated;
    }

    public void setAddressUpdated(AddressUpdated addressUpdated) {
        this.addressUpdated = addressUpdated;
    }

    public UserAddressWrapper(User user, AddressUpdated addressUpdated) {
        this.user = user;
        this.addressUpdated = addressUpdated;
    }
}
