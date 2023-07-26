package com.kursovaya.controller;

import com.kursovaya.entity.Order;
import com.kursovaya.entity.Product;
import com.kursovaya.entity.product.ProductType;
import com.kursovaya.exception.ExceptionHandling;
import com.kursovaya.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = { "/product"})
public class ProductController extends ExceptionHandling {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/productListByProductType/{productType}")
    public ResponseEntity<List<Product>> getProductsByProductType(@PathVariable("productType") String productType) {
        List<Product> products = productService.findByProductType(productType);
        return new ResponseEntity<>(products, OK);
    }

    @GetMapping("/productListByProductTypeId/{id}")
    public ResponseEntity<Optional<ProductType>> getProductTypeById(@PathVariable("id") Long id) {
        Optional<ProductType> products = productService.getProductTypeById(id);
        return new ResponseEntity<>(products, OK);
    }

    @GetMapping("/productTypeList")
    public ResponseEntity<List<ProductType>> getAllProductTypes() {
        List<ProductType> productTypes = productService.getProductTypes();
        return new ResponseEntity<>(productTypes, OK);
    }

    @GetMapping("/productList")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getProducts();
        return new ResponseEntity<>(products, OK);
    }

    @PostMapping("/productBySpecifications")
    public ResponseEntity<Product> getProductBySpecifications(@RequestParam("productType") String productType,
                                                              @RequestParam ("steelGrade") String steelGrade,
                                                              @RequestParam ("sideThickness") double sideThickness,
                                                              @RequestParam ("length") String length,
                                                              @RequestParam ("profileSize") int profileSize,
                                                              @RequestParam ("quantity") int quantity,
                                                              @RequestParam ("userEmail") String userEmail) {
        Product productToCard = productService.getProductBySpecifications(productType, steelGrade, profileSize, length, sideThickness);
        if (productToCard.getQuantity() >= quantity){
            productToCard.setQuantity(quantity);
            return new ResponseEntity<>(productToCard, OK);
        }

        return null;
    }

    @PostMapping("/saveOrUpdateProduct")
    public ResponseEntity<Product> changeProductQuantity(@RequestParam ("productType") String productType,
                                                         @RequestParam ("steelGrade") String steelGrade,
                                                         @RequestParam ("sideThickness") double sideThickness,
                                                         @RequestParam ("length") String length,
                                                         @RequestParam ("profileSize") int profileSize,
                                                         @RequestParam ("quantity") int quantity,
                                                         @RequestParam ("price") double price,
                                                         @RequestParam ("userEmail") String user) {
        Product productToUpdate = productService.getProductBySpecifications(productType, steelGrade, profileSize, length, sideThickness);
        if (productToUpdate == null){
            productService.saveNewProduct(productType, steelGrade, profileSize, length, sideThickness, quantity, price);
        }
        else {
            productService.updateProduct(productToUpdate.getId(), quantity, price);
        }
        return new ResponseEntity<>(OK);
    }

}
