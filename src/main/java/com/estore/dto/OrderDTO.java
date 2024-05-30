package com.estore.dto;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.estore.utility.OrderStatus;
import com.estore.utility.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

	private String orderId;
	
	@Builder.Default
	private Enum<OrderStatus> orderStatus = OrderStatus.PENDING;
	
	@Builder.Default
	private Enum<PaymentStatus> paymentStatus = PaymentStatus.NOT_PAID;
	
	private Double orderAmount;
	private String billingName;
	private String billingContact;

	private AddressDTO billingAddress;

	@CreationTimestamp
	private Date orderedDate;
	
	private Date deliveredDate;
	
    @Builder.Default
	private List<OrderItemDTO> items = new ArrayList<>();
}
