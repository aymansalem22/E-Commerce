package com.oberla.ecommerce.controllers;

import java.util.List;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oberla.ecommerce.common.ApiResponse;
import com.oberla.ecommerce.dto.checkout.CheckoutItemDto;
import com.oberla.ecommerce.dto.checkout.StripeResponse;
import com.oberla.ecommerce.exceptions.AuthenticationFailException;
import com.oberla.ecommerce.exceptions.OrderNotFoundException;
import com.oberla.ecommerce.model.Order;
import com.oberla.ecommerce.model.User;
import com.oberla.ecommerce.service.AuthenticationService;
import com.oberla.ecommerce.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	AuthenticationService authenticationService;

	@PostMapping("/create-checkout-session")
	public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemDto> checkoutItemDtoList)
			throws StripeException {

		Session session = orderService.createSession(checkoutItemDtoList);
		StripeResponse stripeResponse = new StripeResponse(session.getId());

		return new ResponseEntity<StripeResponse>(stripeResponse, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<ApiResponse> placeOrder(@RequestParam("token") String token,
			@RequestParam("sessionId") String sessionId) throws AuthenticationFailException {
		authenticationService.authenticate(token);
		User user = authenticationService.getUser(token);
		orderService.placeOrder(user, sessionId);
		return new ResponseEntity<>(new ApiResponse(true, "Order has been placed"), HttpStatus.CREATED);
	}

	@GetMapping("/")
	public ResponseEntity<List<Order>> getAllOrders(@RequestParam("token") String token)
			throws AuthenticationFailException {

		authenticationService.authenticate(token);

		User user = authenticationService.getUser(token);

		List<Order> orderDtoList = orderService.listOrders(user);

		return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
	}

	// get orderitems for an order
	@GetMapping("/{id}")
	public ResponseEntity<Object> getOrderById(@PathVariable("id") Integer id, @RequestParam("token") String token)
			throws AuthenticationFailException, OrderNotFoundException {
		authenticationService.authenticate(token);
		User user = authenticationService.getUser(token);
		Order order = orderService.getOrder(id, user);
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

}
