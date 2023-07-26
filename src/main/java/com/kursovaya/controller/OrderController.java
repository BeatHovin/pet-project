package com.kursovaya.controller;

import com.kursovaya.entity.Order;
import com.kursovaya.entity.Product;
import com.kursovaya.entity.dto.OrderDTO;
import com.kursovaya.exception.ExceptionHandling;
import com.kursovaya.service.OrderService;
import com.kursovaya.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = { "/order"})
public class OrderController extends ExceptionHandling {

    private OrderService orderService;
    private UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/orderListByStatus/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable("status") String status){
        List<Order> orders = orderService.getOrdersByStatus(status);
        return new ResponseEntity<>(orders, OK);
    }

    @GetMapping("/getOrderListByEmail/{email}")
    public ResponseEntity<List<Order>> getOrdersByEmail(@PathVariable("email") String email){
        List<Order> orders = orderService.getOrdersByEmail(email);

        return new ResponseEntity<>(orders, OK);
    }

    @GetMapping("/acceptOrder/{orderId}")
    public ResponseEntity<Order> acceptOrder(@PathVariable("orderId") Long orderId){
        orderService.acceptOrder(orderId);
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/denyOrder/{orderId}")
    public ResponseEntity<Order> denyOrder(@PathVariable("orderId") Long orderId){
        orderService.denyOrder(orderId);
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/processOrder/{orderId}")
    public ResponseEntity<Order> processOrder(@PathVariable("orderId") Long orderId){
        orderService.processOrder(orderId);
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/getOrderList")
    public ResponseEntity<List<Order>> getOrders(){
        List<Order> orders = orderService.getOrders();
        return new ResponseEntity<>(orders, OK);
    }





    @PostMapping(value = "/postOrder")
    public ResponseEntity<Product[]> adsfasd(@RequestBody OrderDTO orderDTO) {
        orderService.createOrder(orderDTO.products, userService.findUserByEmail(orderDTO.email));
        return new ResponseEntity<>(orderDTO.products, OK);

    }
}
