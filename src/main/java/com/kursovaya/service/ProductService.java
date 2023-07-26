package com.kursovaya.service;

import com.kursovaya.entity.Product;
import com.kursovaya.entity.product.ProductType;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getProducts();
    List<Product> findByProductType(String productType);
    List<ProductType> getProductTypes();
    Optional<ProductType> getProductTypeById(Long id);
    Product getProductBySpecifications(String productType, String steelGrade, int profileSize, String length, double sideThickness);
    Product findProductById(Long id);
    void updateProduct(Long productId, int quantity, double price);

    void saveNewProduct(String productType, String steelGrade, int profileSize, String length, double sideThickness, int quantity, double price);

//    Product findProductByType(String productType);
//
//    Product addNewProduct(String firstName, String lastName, String email, String role, boolean isNonLocked, boolean isActive) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;
//
//    Product updateProduct(String currentEmail, String newFirstName, String newLastName, String newEmail, String role, boolean isNonLocked, boolean isActive) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;

//    void deleteProduct(String email) throws IOException;
}
