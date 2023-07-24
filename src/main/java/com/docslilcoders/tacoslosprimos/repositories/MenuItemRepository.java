package com.docslilcoders.tacoslosprimos.repositories;


import com.docslilcoders.tacoslosprimos.models.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

}