package com.docslilcoders.tacoslosprimos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordHelper {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordHelper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Your other service methods...

    public boolean isPasswordCorrect(String providedPassword, String storedPasswordHash) {
        // Manually encode the provided password using the same PasswordEncoder used by Spring Security
        String encodedProvidedPassword = passwordEncoder.encode(providedPassword);

        // Compare the encoded provided password with the stored password hash
        return passwordEncoder.matches(providedPassword, storedPasswordHash);
    }
}