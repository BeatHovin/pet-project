package com.kursovaya.repository;

import com.kursovaya.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.productType.productType = ?1")
    List<Product> findProductsByProductType(String productType);

    @Query("SELECT p FROM Product p WHERE p.productType.productType = ?1 and  p.steelGrade.steelGrade = ?2 and p.profileSize.size = ?3 and p.productLength.productLength = ?4 and p.sideThickness.sideThickness = ?5")
    Product findProductsBySpecifications(String productType, String steelGrade, int profileSize, String length, double sideThickness);
    @Modifying
    @Query("UPDATE Product p set p.price = ?3, p.quantity = ?2 WHERE p.id = ?1")
    void changeProductQuantity(Long productId, int quantity, double price);

    Product getProductById(Long id);
}
