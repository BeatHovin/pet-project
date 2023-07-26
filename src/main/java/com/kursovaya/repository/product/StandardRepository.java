package com.kursovaya.repository.product;

import com.kursovaya.entity.product.Standard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StandardRepository extends JpaRepository<Standard, Long> {
//    @Query("SELECT s FROM Standard s WHERE s. = ?1")
//    Standard findStandardByProductType(String productType);
}
