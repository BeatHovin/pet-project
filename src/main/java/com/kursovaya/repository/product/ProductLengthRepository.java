package com.kursovaya.repository.product;

import com.kursovaya.entity.product.ProductLength;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLengthRepository extends JpaRepository<ProductLength, Long> {
    ProductLength findProductLengthByProductLength(String length);
}
