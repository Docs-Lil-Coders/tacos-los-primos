package com.docslilcoders.tacoslosprimos.repositories;

import com.docslilcoders.tacoslosprimos.models.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {
    @Query("SELECT p.code FROM PromoCode p WHERE p.redeemed = false")
    List<String> findPromoCodeNamesByRedeemedEqualsZero();
}
