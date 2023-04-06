package com.oberla.ecommerce.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oberla.ecommerce.common.ApiResponse;
import com.oberla.ecommerce.dto.product.ProductDto;
import com.oberla.ecommerce.exceptions.AuthenticationFailException;
import com.oberla.ecommerce.model.Product;
import com.oberla.ecommerce.model.User;
import com.oberla.ecommerce.model.WishList;
import com.oberla.ecommerce.repository.ProductRepository;
import com.oberla.ecommerce.service.AuthenticationService;
import com.oberla.ecommerce.service.WishListService;

@RestController
@RequestMapping("/wishlist")
public class WishListController {

	@Autowired
	WishListService wishListService;

	@Autowired
	AuthenticationService authenticationService;

	@Autowired
	private ProductRepository productRepository;

	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addWishList(@RequestBody ProductDto productDto,
			@RequestParam("token") String token) throws AuthenticationFailException {
		authenticationService.authenticate(token);
		User user = authenticationService.getUser(token);
		Product product = productRepository.getById(productDto.getId());

		WishList wishList = new WishList(user, product);
		wishListService.createWishList(wishList);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Added to wishlist"), HttpStatus.CREATED);
	}

	@GetMapping("/{token}")
	public ResponseEntity<List<ProductDto>> getWishList(@PathVariable("token") String token)
			throws AuthenticationFailException {
		authenticationService.authenticate(token);
		User user = authenticationService.getUser(token);
		List<WishList> wishLists = wishListService.readWishList(user);

		List<ProductDto> products = new ArrayList<>();
		for (WishList wishList : wishLists) {
			products.add(new ProductDto(wishList.getProduct()));
		}

		return new ResponseEntity<>(products, HttpStatus.OK);

	}

}
