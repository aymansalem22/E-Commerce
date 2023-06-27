package com.oberla.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oberla.ecommerce.model.AuthenticationToken;
import com.oberla.ecommerce.model.User;

@Repository
public interface TokenRepository extends JpaRepository<AuthenticationToken, Integer> {
	AuthenticationToken findTokenByUser(User user);

	AuthenticationToken findTokenByToken(String token);

}
