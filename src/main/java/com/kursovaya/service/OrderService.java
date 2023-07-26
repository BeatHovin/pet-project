package com.kursovaya.service;

import com.kursovaya.entity.Product;
import com.kursovaya.entity.Order;
import com.kursovaya.entity.User;

import java.util.List;

public interface OrderService {
    List<Order> getOrdersByStatus(String status);
    List<Order> getOrdersByEmail(String email);
    List<Order> getOrders();
    Order updateOrderStatus(Long orderId, String status);
    void acceptOrder(Long orderId);
    void denyOrder(Long orderId);
    void processOrder(Long orderId);

    boolean isCartExists(String email);
    void createOrder(Product[] cart, User user);
}
