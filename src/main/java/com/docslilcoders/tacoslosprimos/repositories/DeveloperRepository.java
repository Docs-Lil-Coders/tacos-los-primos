package com.docslilcoders.tacoslosprimos.repositories;

import com.docslilcoders.tacoslosprimos.models.Address;
import com.docslilcoders.tacoslosprimos.models.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {

}