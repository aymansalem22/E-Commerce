package com.oberla.ecommerce.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oberla.ecommerce.config.MessageStrings;
import com.oberla.ecommerce.exceptions.AuthenticationFailException;
import com.oberla.ecommerce.model.AuthenticationToken;
import com.oberla.ecommerce.model.User;
import com.oberla.ecommerce.repository.TokenRepository;

@Service
public class AuthenticationService {

	@Autowired
	TokenRepository repository;

	public void saveConfirmationToken(AuthenticationToken authenticationToken) {
		repository.save(authenticationToken);
	}

	public AuthenticationToken getToken(User user) {
		return repository.findTokenByUser(user);
	}

	public User getUser(String token) {
		AuthenticationToken authenticationToken = repository.findTokenByToken(token);
		if (Objects.nonNull(authenticationToken)) {
			if (Objects.nonNull(authenticationToken.getUser())) {
				return authenticationToken.getUser();
			}
		}
		return null;
	}

	public void authenticate(String token) throws AuthenticationFailException {
		if (!Objects.nonNull(token)) {
			throw new AuthenticationFailException(MessageStrings.AUTH_TOKEN_NOT_PRESENT);
		}
		if (!Objects.nonNull(getUser(token))) {
			throw new AuthenticationFailException(MessageStrings.AUTH_TOEKN_NOT_VALID);
		}
	}

}
