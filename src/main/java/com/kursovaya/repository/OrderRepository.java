package com.kursovaya.repository;
import com.kursovaya.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT p FROM Order p WHERE p.orderStatus = ?1")
    List<Order> findOrdersByOrderStatus(String status);

    @Query("SELECT p FROM Order p WHERE p.user.email = ?1")
    List<Order> findOrdersByUser(String email);

    @Query("UPDATE Order o set o.orderStatus = ?2 where o.id = ?1")
    Order updateOrderStatus(Long orderId, String status);

    @Modifying
    @Query("UPDATE Order o set o.orderStatus = 'in_processing' where o.id = ?1")
    void acceptOrder(Long orderId);

    @Modifying
    @Query("UPDATE Order o set o.orderStatus = 'denied' where o.id = ?1")
    void denyOrder(Long orderId);

    @Modifying
    @Query("UPDATE Order o set o.orderStatus = 'ready' where o.id = ?1")
    void processOrder(Long orderId);

}
