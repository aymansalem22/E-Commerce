package com.oberla.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oberla.ecommerce.dto.cart.CartDto;
import com.oberla.ecommerce.model.Cart;
import com.oberla.ecommerce.model.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
	List<Cart> findAllByUserOrderByCreatedDateDesc(User user);

}
