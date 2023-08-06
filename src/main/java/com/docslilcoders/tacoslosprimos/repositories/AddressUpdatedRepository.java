package com.docslilcoders.tacoslosprimos.repositories;



import com.docslilcoders.tacoslosprimos.models.Address;
import com.docslilcoders.tacoslosprimos.models.AddressUpdated;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AddressUpdatedRepository extends JpaRepository<AddressUpdated, Long> {
    AddressUpdated findByUserIdAndIsPrimaryTrue(Long userId);

    List<AddressUpdated> findByUserIdAndIsPrimaryFalse(Long userId);
}