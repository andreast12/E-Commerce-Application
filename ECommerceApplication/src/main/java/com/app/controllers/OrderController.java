package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.config.AppConstants;
import com.app.payloads.CreditCardRequest;
import com.app.payloads.OrderDTO;
import com.app.payloads.OrderResponse;
import com.app.services.OrderService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class OrderController {

	@Autowired
	public OrderService orderService;

	@PostMapping("/public/users/{email}/carts/{cartId}/payments/{paymentMethod}/order")
	public ResponseEntity<OrderDTO> orderProducts(@PathVariable String email, @PathVariable Long cartId,
			@PathVariable String paymentMethod, @Valid @RequestBody(required = false) CreditCardRequest creditCardRequest) {
		OrderDTO order = orderService.placeOrder(email, cartId, paymentMethod, creditCardRequest);

		return new ResponseEntity<OrderDTO>(order, HttpStatus.CREATED);
	}

	@GetMapping("/admin/orders")
	public ResponseEntity<OrderResponse> getAllOrders(
			@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false) String sortBy,
			@RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

		OrderResponse orderResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);

		return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.FOUND);
	}

	@GetMapping("public/users/{email}/orders")
	public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable String email) {
		List<OrderDTO> orders = orderService.getOrdersByUser(email);

		return new ResponseEntity<List<OrderDTO>>(orders, HttpStatus.FOUND);
	}

	@GetMapping("public/users/{email}/orders/{orderId}")
	public ResponseEntity<OrderDTO> getOrderByUser(@PathVariable String email, @PathVariable Long orderId) {
		OrderDTO order = orderService.getOrder(email, orderId);

		return new ResponseEntity<OrderDTO>(order, HttpStatus.FOUND);
	}

	@GetMapping("public/users/{email}/orders/filter")
	public ResponseEntity<List<OrderDTO>> getOrdersByUserWithFilters(@PathVariable String email,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate,
			@RequestParam(required = false) String paymentMethod,
			@RequestParam(required = false) String orderStatus,
			@RequestParam(required = false) Boolean hasDiscount) {

		List<OrderDTO> orders = orderService.getOrdersByUserWithFilters(email, startDate, endDate,
				paymentMethod, orderStatus, hasDiscount);

		return new ResponseEntity<List<OrderDTO>>(orders, HttpStatus.FOUND);
	}

	@PutMapping("admin/users/{email}/orders/{orderId}/orderStatus/{orderStatus}")
	public ResponseEntity<OrderDTO> updateOrderByUser(@PathVariable String email, @PathVariable Long orderId, @PathVariable String orderStatus) {
		OrderDTO order = orderService.updateOrder(email, orderId, orderStatus);

		return new ResponseEntity<OrderDTO>(order, HttpStatus.OK);
	}

}
