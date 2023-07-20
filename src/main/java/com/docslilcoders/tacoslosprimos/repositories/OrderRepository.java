package com.docslilcoders.tacoslosprimos.repositories;



import com.docslilcoders.tacoslosprimos.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {

}