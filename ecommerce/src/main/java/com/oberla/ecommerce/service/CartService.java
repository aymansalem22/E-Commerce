package com.oberla.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oberla.ecommerce.dto.cart.AddToCartDto;
import com.oberla.ecommerce.dto.cart.CartDto;
import com.oberla.ecommerce.dto.cart.CartItemDto;
import com.oberla.ecommerce.exceptions.CartItemNotExistException;
import com.oberla.ecommerce.model.Cart;
import com.oberla.ecommerce.model.Product;
import com.oberla.ecommerce.model.User;
import com.oberla.ecommerce.repository.CartRepository;

@Service
public class CartService {

	@Autowired
	CartRepository cartRepository;

	public void addToCart(AddToCartDto addToCartDto, Product product, User user) {
		Cart cart = new Cart(product, addToCartDto.getQuantity(), user);
		cartRepository.save(cart);

	}

	public CartDto listCartItems(User user) {

		List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
		List<CartItemDto> cartItems = new ArrayList<>();
		for (Cart cart : cartList) {
			CartItemDto cartItemDto = new CartItemDto(cart);
			cartItems.add(cartItemDto);
		}

		double totalCost = 0;
		for (CartItemDto cartItemDto : cartItems) {
			totalCost += cartItemDto.getProduct().getPrice() * cartItemDto.getQuantity();
		}

		return new CartDto(cartItems, totalCost);

	}

	public void deleteCartItem(int cartItemId, User user) throws CartItemNotExistException {
		Optional<Cart> optionalCart = cartRepository.findById(cartItemId);
		if (!optionalCart.isPresent()) {
			throw new CartItemNotExistException("cartItemId not valid");
		}

		Cart cart = optionalCart.get();
		if (cart.getUser() != user) {
			throw new CartItemNotExistException("cart item does not belong to user");
		}

		cartRepository.deleteById(cartItemId);

	}

}
