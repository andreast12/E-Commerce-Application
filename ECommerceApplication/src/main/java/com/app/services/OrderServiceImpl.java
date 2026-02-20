package com.app.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.entites.Cart;
import com.app.entites.CartItem;
import com.app.entites.Discount;
import com.app.entites.Order;
import com.app.entites.OrderItem;
import com.app.entites.Payment;
import com.app.entites.Product;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CreditCardRequest;
import com.app.payloads.OrderDTO;
import com.app.payloads.OrderItemDTO;
import com.app.payloads.OrderResponse;
import com.app.repositories.CartItemRepo;
import com.app.repositories.CartRepo;
import com.app.repositories.DiscountRepo;
import com.app.repositories.OrderItemRepo;
import com.app.repositories.OrderRepo;
import com.app.repositories.PaymentRepo;
import com.app.repositories.UserRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	public UserRepo userRepo;

	@Autowired
	public CartRepo cartRepo;

	@Autowired
	public OrderRepo orderRepo;

	@Autowired
	private PaymentRepo paymentRepo;

	@Autowired
	public OrderItemRepo orderItemRepo;

	@Autowired
	public CartItemRepo cartItemRepo;

	@Autowired
	public UserService userService;

	@Autowired
	public CartService cartService;

	@Autowired
	private DiscountRepo discountRepo;

	@Autowired
	public ModelMapper modelMapper;

	@Override
	public OrderDTO placeOrder(String email, Long cartId, String paymentMethod, CreditCardRequest creditCardRequest) {

		Cart cart = cartRepo.findCartByEmailAndCartId(email, cartId);

		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "cartId", cartId);
		}

		List<CartItem> cartItems = cart.getCartItems();

		if (cartItems.size() == 0) {
			throw new APIException("Cart is empty");
		}

		if (paymentMethod.equalsIgnoreCase("Credit Card")) {
			if (creditCardRequest == null || creditCardRequest.getCardNumber() == null
					|| creditCardRequest.getCardCvc() == null) {
				throw new APIException("Card number and CVC are required for Credit Card payment");
			}
		}

		Order order = new Order();
		order.setEmail(email);
		order.setOrderDate(LocalDate.now());

		double totalAmount = 0;
		List<Discount> activeDiscounts = discountRepo.findActiveDiscounts(LocalDate.now());
		boolean hasStoreDiscount = !activeDiscounts.isEmpty();

		if (hasStoreDiscount) {
			for (CartItem cartItem : cartItems) {
				totalAmount += cartItem.getProduct().getPrice() * cartItem.getQuantity();
			}

			Discount bestDiscount = activeDiscounts.stream()
					.max((d1, d2) -> Double.compare(d1.getDiscountPercentage(), d2.getDiscountPercentage()))
					.get();
			double discountedAmount = totalAmount * (1 - bestDiscount.getDiscountPercentage() / 100.0);

			order.setDiscountName(bestDiscount.getDiscountName());
			order.setDiscountPercentage(bestDiscount.getDiscountPercentage());
			order.setDiscountedAmount(discountedAmount);
			order.setTotalAmount(discountedAmount);
		} else {
			order.setTotalAmount(cart.getTotalPrice());
		}

		order.setOrderStatus("Order Accepted !");

		Payment payment = new Payment();
		payment.setOrder(order);
		payment.setPaymentMethod(paymentMethod);

		if (paymentMethod.equalsIgnoreCase("Credit Card")) {
			payment.setCardNumber(creditCardRequest.getCardNumber());
			payment.setCardCvc(creditCardRequest.getCardCvc());
		}

		payment = paymentRepo.save(payment);
		order.setPayment(payment);

		Order savedOrder = orderRepo.save(order);

		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());

			if (hasStoreDiscount) {
				Discount bestDiscount = activeDiscounts.stream()
						.max((d1, d2) -> Double.compare(d1.getDiscountPercentage(), d2.getDiscountPercentage()))
						.get();
				orderItem.setDiscount(bestDiscount.getDiscountPercentage());
				orderItem.setOrderedProductPrice(cartItem.getProduct().getPrice());
			} else {
				orderItem.setDiscount(cartItem.getDiscount());
				orderItem.setOrderedProductPrice(cartItem.getProductPrice());
			}

			orderItem.setOrder(savedOrder);
			orderItems.add(orderItem);
		}

		orderItems = orderItemRepo.saveAll(orderItems);

		cart.getCartItems().forEach(item -> {
			int quantity = item.getQuantity();
			Product product = item.getProduct();
			cartService.deleteProductFromCart(cartId, item.getProduct().getProductId());
			product.setQuantity(product.getQuantity() - quantity);
		});

		OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
		orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

		return orderDTO;
	}

	@Override
	public List<OrderDTO> getOrdersByUser(String email) {
		List<Order> orders = orderRepo.findAllByEmail(email);

		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		if (orderDTOs.size() == 0) {
			throw new APIException("No orders placed yet by the user with email: " + email);
		}

		return orderDTOs;
	}

	@Override
	public OrderDTO getOrder(String email, Long orderId) {

		Order order = orderRepo.findOrderByEmailAndOrderId(email, orderId);

		if (order == null) {
			throw new ResourceNotFoundException("Order", "orderId", orderId);
		}

		return modelMapper.map(order, OrderDTO.class);
	}

	@Override
	public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

		Page<Order> pageOrders = orderRepo.findAll(pageDetails);

		List<Order> orders = pageOrders.getContent();

		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		if (orderDTOs.size() == 0) {
			throw new APIException("No orders placed yet by the users");
		}

		OrderResponse orderResponse = new OrderResponse();

		orderResponse.setContent(orderDTOs);
		orderResponse.setPageNumber(pageOrders.getNumber());
		orderResponse.setPageSize(pageOrders.getSize());
		orderResponse.setTotalElements(pageOrders.getTotalElements());
		orderResponse.setTotalPages(pageOrders.getTotalPages());
		orderResponse.setLastPage(pageOrders.isLast());

		return orderResponse;
	}

	@Override
	public List<OrderDTO> getOrdersByUserWithFilters(String email, String startDate, String endDate,
			String paymentMethod, String orderStatus, Boolean hasDiscount) {

		List<Order> orders = orderRepo.findAllByEmail(email);

		if (orders.isEmpty()) {
			throw new APIException("No orders placed yet by the user with email: " + email);
		}

		java.util.stream.Stream<Order> stream = orders.stream();

		if (startDate != null) {
			LocalDate start = LocalDate.parse(startDate);
			stream = stream.filter(order -> !order.getOrderDate().isBefore(start));
		}

		if (endDate != null) {
			LocalDate end = LocalDate.parse(endDate);
			stream = stream.filter(order -> !order.getOrderDate().isAfter(end));
		}

		if (paymentMethod != null) {
			stream = stream.filter(order -> order.getPayment() != null
					&& paymentMethod.equalsIgnoreCase(order.getPayment().getPaymentMethod()));
		}

		if (orderStatus != null) {
			stream = stream.filter(order -> orderStatus.equalsIgnoreCase(order.getOrderStatus()));
		}

		if (hasDiscount != null) {
			if (hasDiscount) {
				stream = stream.filter(order -> order.getDiscountName() != null);
			} else {
				stream = stream.filter(order -> order.getDiscountName() == null);
			}
		}

		List<OrderDTO> orderDTOs = stream.map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		if (orderDTOs.isEmpty()) {
			throw new APIException("No orders found matching the given filters for user with email: " + email);
		}

		return orderDTOs;
	}

	@Override
	public OrderDTO updateOrder(String email, Long orderId, String orderStatus) {

		Order order = orderRepo.findOrderByEmailAndOrderId(email, orderId);

		if (order == null) {
			throw new ResourceNotFoundException("Order", "orderId", orderId);
		}

		order.setOrderStatus(orderStatus);

		return modelMapper.map(order, OrderDTO.class);
	}

}
