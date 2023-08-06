package com.docslilcoders.tacoslosprimos.repositories;



import com.docslilcoders.tacoslosprimos.models.Address;
import com.docslilcoders.tacoslosprimos.models.AddressUpdated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AddressUpdatedRepository extends JpaRepository<AddressUpdated, Long> {
    AddressUpdated findByUserIdAndIsPrimaryTrue(Long userId);

    List<AddressUpdated> findByUserIdAndIsPrimaryFalse(Long userId);

    List<AddressUpdated> findByUserId(Long userId);

    @Query("SELECT a.id, a.building, a.city, a.isPrimary, a.state, a.street, a.zipCode FROM AddressUpdated a WHERE a.user.id = :userId")
    List<Object[]> findAddressesByUserId(@Param("userId") Long userId);
}