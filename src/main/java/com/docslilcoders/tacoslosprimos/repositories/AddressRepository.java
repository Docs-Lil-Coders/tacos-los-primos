package com.docslilcoders.tacoslosprimos.repositories;



import com.docslilcoders.tacoslosprimos.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Long> {

}