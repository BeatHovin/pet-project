package com.kursovaya.repository;

import com.kursovaya.entity.product.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
    ProductType findByProductType(String productType);
}
