package com.oberla.ecommerce.service;

import com.oberla.ecommerce.dto.cart.CartDto;
import com.oberla.ecommerce.dto.cart.CartItemDto;
import com.oberla.ecommerce.dto.checkout.CheckoutItemDto;
import com.oberla.ecommerce.exceptions.OrderNotFoundException;
import com.oberla.ecommerce.model.Order;
import com.oberla.ecommerce.model.OrderItem;
import com.oberla.ecommerce.model.User;
import com.oberla.ecommerce.repository.OrderItemsRepository;
import com.oberla.ecommerce.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

	@Value("${BASE_URL}")
	private String baseURL;

	@Value("${STRIPE_SECRET_KEY}")
	private String apiKey;

	@Autowired
	private CartService cartService;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderItemsRepository orderItemsRepository;

	SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItemDto checkoutItemDto) {
		return SessionCreateParams.LineItem.PriceData.builder().setCurrency("usd")
				.setUnitAmount(((long) checkoutItemDto.getPrice()) * 100)
				.setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
						.setName(checkoutItemDto.getProductName()).build())
				.build();
	}

	SessionCreateParams.LineItem createSessionLineItem(CheckoutItemDto checkoutItemDto) {
		return SessionCreateParams.LineItem.builder()

				.setPriceData(createPriceData(checkoutItemDto))

				.setQuantity(Long.parseLong(String.valueOf(checkoutItemDto.getQuantity()))).build();
	}

	public Session createSession(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {

		String successURL = baseURL + "payment/success";
		String failedURL = baseURL + "payment/failed";

		Stripe.apiKey = apiKey;

		List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<>();

		for (CheckoutItemDto checkoutItemDto : checkoutItemDtoList) {
			sessionItemsList.add(createSessionLineItem(checkoutItemDto));
		}

		SessionCreateParams params = SessionCreateParams.builder()
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.setMode(SessionCreateParams.Mode.PAYMENT).setCancelUrl(failedURL).addAllLineItem(sessionItemsList)
				.setSuccessUrl(successURL).build();
		return Session.create(params);
	}

	public void placeOrder(User user, String sessionId) {

		CartDto cartDto = cartService.listCartItems(user);

		List<CartItemDto> cartItemDtoList = cartDto.getCartItems();

		Order newOrder = new Order();
		newOrder.setCreatedDate(new Date());
		newOrder.setSessionId(sessionId);
		newOrder.setUser(user);
		newOrder.setTotalPrice(cartDto.getTotalCost());
		orderRepository.save(newOrder);

		for (CartItemDto cartItemDto : cartItemDtoList) {
			OrderItem orderItem = new OrderItem();
			orderItem.setCreatedDate(new Date());
			orderItem.setPrice(cartItemDto.getProduct().getPrice());
			orderItem.setProduct(cartItemDto.getProduct());
			orderItem.setQuantity(cartItemDto.getQuantity());
			orderItem.setOrder(newOrder);

			orderItemsRepository.save(orderItem);
		}

		cartService.deleteUserCartItems(user);

	}

	public List<Order> listOrders(User user) {

		return orderRepository.findAllByUserOrderByCreatedDateDesc(user);
	}

	public Order getOrder(Integer orderId, User user) throws OrderNotFoundException {

		Optional<Order> optionalOrder = orderRepository.findById(orderId);

		if (optionalOrder.isEmpty()) {
			throw new OrderNotFoundException("order id is not valid");
		}

		Order order = optionalOrder.get();

		if (order.getUser() != user) {
			throw new OrderNotFoundException("order does not belong to user");
		}

		return order;

	}

}
