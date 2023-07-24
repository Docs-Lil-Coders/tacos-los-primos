package com.docslilcoders.tacoslosprimos.repositories;


import com.docslilcoders.tacoslosprimos.models.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderedItemRepository extends JpaRepository<OrderedItem, Long> {

}