package com.docslilcoders.tacoslosprimos.repositories;


import com.docslilcoders.tacoslosprimos.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewsRepository extends JpaRepository<Review, Long> {

}