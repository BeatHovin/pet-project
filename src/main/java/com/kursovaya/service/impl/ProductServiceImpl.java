package com.kursovaya.service.impl;

import com.kursovaya.entity.Product;
import com.kursovaya.entity.product.*;
import com.kursovaya.repository.ProductRepository;
import com.kursovaya.repository.product.*;
import com.kursovaya.service.ProductService;

import com.kursovaya.repository.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private SteelGradeRepository steelGradeRepository;
    private SideThicknessRepository sideThicknessRepository;
    private ProfileSizeRepository profileSizeRepository;
    private ProductLengthRepository productLengthRepository;
    private StandardRepository standardRepository;
    private ProductTypeRepository productTypeRepository;


    @Autowired
    public ProductServiceImpl(StandardRepository standardRepository, ProductRepository productRepository, SteelGradeRepository steelGradeRepository, SideThicknessRepository sideThicknessRepository, ProfileSizeRepository profileSizeRepository, ProductLengthRepository productLengthRepository, ProductTypeRepository productTypeRepository) {
        this.productRepository = productRepository;
        this.steelGradeRepository = steelGradeRepository;
        this.sideThicknessRepository = sideThicknessRepository;
        this.profileSizeRepository = profileSizeRepository;
        this.productLengthRepository = productLengthRepository;
        this.productTypeRepository = productTypeRepository;
        this.standardRepository = standardRepository;
    }


    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findByProductType(String productType) {
        return productRepository.findProductsByProductType(productType);
    }

    @Override
    public List<ProductType> getProductTypes() {
        return productTypeRepository.findAll();
    }


    public Optional<ProductType> getProductTypeById(Long id){
        Optional<ProductType> result = productTypeRepository.findById(id);
        result.get().getSteelGrades();
        result.get().getLengths();
        result.get().getProfileSizes();
        result.get().getStandards();
        result.get().getSideThicknesses();
        return result;
    }

    @Override
    public Product getProductBySpecifications(String productType, String steelGrade, int profileSize, String length, double sideThickness) {
        return productRepository.findProductsBySpecifications(productType, steelGrade, profileSize, length, sideThickness);
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.getProductById(id);
    }

    @Override
    public void updateProduct(Long productId, int quantity, double price) {
        productRepository.changeProductQuantity(productId, quantity, price);
    }

    @Override
    public void saveNewProduct(String productType, String steelGrade, int profileSize, String length, double sideThickness, int quantity, double price) {
        ProductType _productType = productTypeRepository.findByProductType(productType);
        SteelGrade _steelGrade = steelGradeRepository.findSteelGradeBySteelGrade(steelGrade);
        ProfileSize _profileSize = profileSizeRepository.findProfileSizeBySize(profileSize);
        ProductLength _productLength = productLengthRepository.findProductLengthByProductLength(length);
        SideThickness _sideThickness = sideThicknessRepository.findSideThicknessBySideThickness(sideThickness);
        Standard _standard = new ArrayList<Standard>(_productType.getStandards()).get(0);

        Product product = new Product();
        product.setProductType(_productType);
        product.setSteelGrade(_steelGrade);
        product.setProfileSize(_profileSize);
        product.setProductLength(_productLength);
        product.setSideThickness(_sideThickness);
        product.setStandard(_standard);
        product.setPrice(price);
        product.setQuantity(quantity);
        productRepository.save(product);
    }

}
