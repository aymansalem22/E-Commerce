package com.oberla.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oberla.ecommerce.common.ApiResponse;
import com.oberla.ecommerce.dto.cart.AddToCartDto;
import com.oberla.ecommerce.dto.cart.CartDto;
import com.oberla.ecommerce.exceptions.AuthenticationFailException;
import com.oberla.ecommerce.exceptions.CartItemNotExistException;
import com.oberla.ecommerce.exceptions.ProductNotExistException;
import com.oberla.ecommerce.model.Product;
import com.oberla.ecommerce.model.User;
import com.oberla.ecommerce.service.AuthenticationService;
import com.oberla.ecommerce.service.CartService;
import com.oberla.ecommerce.service.CategoryService;
import com.oberla.ecommerce.service.ProductService;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	CartService cartService;

	@Autowired
	ProductService productService;

	@Autowired
	AuthenticationService authenticationService;

	@GetMapping("/")
	public ResponseEntity<CartDto> getCartItems(@RequestParam("token") String token)
			throws AuthenticationFailException {
		authenticationService.authenticate(token);
		User user = authenticationService.getUser(token);
		CartDto cartDto = cartService.listCartItems(user);
		return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addToCart(@RequestBody AddToCartDto addToCartDto,
			@RequestParam("token") String token) throws ProductNotExistException, AuthenticationFailException {

		authenticationService.authenticate(token);
		User user = authenticationService.getUser(token);

		Product product = productService.getProductById(addToCartDto.getProductId());
		cartService.addToCart(addToCartDto, product, user);

		return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);

	}

	@DeleteMapping("/delete/{cartItemId}")
	public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable("cartItemId") int cartItemId,
			@RequestParam("token") String token) throws AuthenticationFailException, CartItemNotExistException {
		authenticationService.authenticate(token);
		User user = authenticationService.getUser(token);
		cartService.deleteCartItem(cartItemId, user);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Item has been removed"), HttpStatus.OK);
	}

}
