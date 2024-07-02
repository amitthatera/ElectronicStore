package com.estore.services;

import java.util.List;

import com.estore.dto.OrderDTO;
import com.estore.dto.PageableResponse;
import com.estore.utility.OrderStatus;
import com.estore.utility.PaymentStatus;

public interface OrderService {

	OrderDTO createOrder(OrderDTO orderDto, String userId);

	OrderDTO updatePaymentStatus(String orderId, PaymentStatus paymentStatus);

	OrderDTO updateOrderStatus(String orderId, OrderStatus orderStatus);

	void removeOrder(String orderId);

	OrderDTO getOrderById(String orderId);

	List<OrderDTO> getOrderByUser(String userId);

	PageableResponse<OrderDTO> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

}
