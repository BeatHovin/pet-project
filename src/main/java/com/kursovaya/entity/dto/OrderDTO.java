package com.kursovaya.entity.dto;

import com.kursovaya.entity.Product;

import java.io.Serializable;

public class OrderDTO implements Serializable {
    public Product[] products;
    public String email;

}
