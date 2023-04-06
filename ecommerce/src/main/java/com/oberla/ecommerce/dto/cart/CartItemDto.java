package com.oberla.ecommerce.dto.cart;

import javax.validation.constraints.NotNull;

import com.oberla.ecommerce.model.Cart;
import com.oberla.ecommerce.model.Product;

public class CartItemDto {

	private Integer id;
	private @NotNull Integer quantity;
	private @NotNull Product product;

	public CartItemDto(Cart cart) {

		this.setId(cart.getId());
		this.setQuantity(cart.getQuantity());
		this.setProduct(cart.getProduct());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
