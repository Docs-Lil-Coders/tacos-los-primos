package com.docslilcoders.tacoslosprimos.repositories;


import com.docslilcoders.tacoslosprimos.models.MenuItem;
import com.docslilcoders.tacoslosprimos.models.NutritionInformation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NutritionInformationRepository extends JpaRepository<NutritionInformation, Long> {
    NutritionInformation findByMenuItemId(Long menuItemId);
}