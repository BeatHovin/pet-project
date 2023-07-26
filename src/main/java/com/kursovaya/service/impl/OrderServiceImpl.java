package com.kursovaya.service.impl;

import com.kursovaya.entity.Product;
import com.kursovaya.entity.order.OrderDetails;
import com.kursovaya.repository.OrderRepository;
import com.kursovaya.repository.ProductRepository;
import com.kursovaya.entity.Order;

import com.kursovaya.entity.User;
import com.kursovaya.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    ProductRepository productRepository;


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }


//    @Override
//    public Order addToCart(Product product, int quantity, User user, String status) {
//
//        if (isCartExists(user.getEmail())){
//            List<Order> cart = orderRepository.findOrdersByOrderStatusAndUser("in_cart", user.getEmail());
//            Order order = new Order();
//            order.setId(cart.get(0).getId());
//            order.setOrderStatus(status);
//            order.setQuantity(quantity);
//            order.setUser(user);
//            order.setProductsInOrder(cart.ge);
//            cart.add()
//        }
//
//        order.getProductsInOrder(product);
//        order.setQuantity(quantity);
//        order.setUser(user);
//        order.setOrderStatus(status);
//        orderRepository.save(order);
//        return order;
//    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findOrdersByOrderStatus(status);
    }

    @Override
    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findOrdersByUser(email);
    }

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrderStatus(Long orderId, String status) {
        return orderRepository.updateOrderStatus(orderId, status);
    }

    @Override
    public void acceptOrder(Long orderId) {
        orderRepository.acceptOrder(orderId);
    }

    @Override
    public void denyOrder(Long orderId) {
        orderRepository.denyOrder(orderId);
    }

    @Override
    public void processOrder(Long orderId) {
        orderRepository.processOrder(orderId);

    }

    @Override
    public boolean isCartExists(String email) {
        return false;
    }

//    @Override
//    public boolean isCartExists(String email) {
//        return !orderRepository.findUsersCart(email).isEmpty();
//    }

    @Override
    public void createOrder(Product[] cart, User user) {
        ArrayList<OrderDetails> orderDetailsArrayList = new ArrayList<>();
        Order order = new Order();
        for (Product product: cart){
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setProduct(productRepository.findProductsBySpecifications(product.getProductType().getProductType(),
                    product.getSteelGrade().getSteelGrade(), product.getProfileSize().getSize(), product.getProductLength().getProductLength(), product.getSideThickness().getSideThickness() ));
            orderDetails.setQuantity(product.getQuantity());
            orderDetailsArrayList.add(orderDetails);
            orderDetails.setOrder(order);
        }

        order.setOrderDetails(orderDetailsArrayList);
        order.setUser(user);
        order.setOrderStatus("in_accepting");

        orderRepository.save(order);

//        for (Product product: cart){
//            ProductInOrder productInOrder = new ProductInOrder();
//            productInOrder.setProduct(product);
//            productInOrder.setQuantity(product.getQuantity());
//            productsInOrders.add(productInOrder);
//        }
//
//        Order order = new Order();
//        order.setOrderStatus("in_processing");
//        order.setProductsInOrders(productsInOrders);
//        order.setUser(user);
//        orderRepository.save(order);
//
//        for (Product product: cart) {
//            Order order = new Order();
//            order.setOrderStatus("in_processing");
//            order.setQuantity(product.getQuantity());
//            order.setUser(user);
//            order.setProduct(product);
//            orderRepository.save(order);
//        }




        for (Product product : cart) {
            Product temp = productRepository.findProductsBySpecifications(product.getProductType().getProductType(), product.getSteelGrade().getSteelGrade(), product.getProfileSize().getSize(),
                                                            product.getProductLength().getProductLength(), product.getSideThickness().getSideThickness());
            temp.setQuantity(temp.getQuantity()-product.getQuantity());
            productRepository.save(temp);
        }
    }
}
