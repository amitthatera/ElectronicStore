package com.estore.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.estore.entities.Order;
import com.estore.entities.User;
import com.estore.utility.OrderStatus;
import com.estore.utility.PaymentStatus;

public interface OrderRepository extends JpaRepository<Order, String> {

	Page<Order> findByOrderStatus(Enum<OrderStatus> orderStatus, Pageable pageable);

	Page<Order> findByPaymentStatus(Enum<PaymentStatus> orderStatus, Pageable pageable);

	List<Order> findByUser(User user);

	Page<Order> findByOrderedDate(Date orderDate, Pageable pageable);

	Page<Order> findByDeliveredDate(Date deliverDate, Pageable pageable);
}
