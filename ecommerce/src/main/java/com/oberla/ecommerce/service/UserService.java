package com.oberla.ecommerce.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oberla.ecommerce.config.MessageStrings;
import com.oberla.ecommerce.dto.user.SignInDto;
import com.oberla.ecommerce.dto.user.SignInResponseDto;
import com.oberla.ecommerce.dto.user.SignUpResponseDto;
import com.oberla.ecommerce.dto.user.SignupDto;
import com.oberla.ecommerce.exceptions.AuthenticationFailException;
import com.oberla.ecommerce.exceptions.CustomException;
import com.oberla.ecommerce.model.AuthenticationToken;
import com.oberla.ecommerce.model.User;
import com.oberla.ecommerce.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AuthenticationService authenticationService;

	Logger logger = LoggerFactory.getLogger(UserService.class);

	public SignUpResponseDto signUp(SignupDto signupDto) throws CustomException {
		if (Objects.nonNull(userRepository.findByEmail(signupDto.getEmail()))) {
			throw new CustomException("User already exists");
		}

		String encryptedPassword = signupDto.getPassword();
		try {
			encryptedPassword = hashPassword(signupDto.getPassword());

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.error("hasing password failed {}", e.getMessage());
		}

		User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(),
				encryptedPassword);
		try {
			userRepository.save(user);
			final AuthenticationToken authenticationToken = new AuthenticationToken(user);
			authenticationService.saveConfirmationToken(authenticationToken);
			return new SignUpResponseDto("success", "user created successfully");
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}

	String hashPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();
		String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
		return myHash;
	}

	public SignInResponseDto signIn(SignInDto signInDto) throws AuthenticationFailException, CustomException {
		User user = userRepository.findByEmail(signInDto.getEmail());
		if (!Objects.nonNull(user)) {
			throw new AuthenticationFailException("user not present");
		}
		try {

			if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
				throw new AuthenticationFailException(MessageStrings.WRONG_PASSWORD);
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.error("hasing password failed {}", e.getMessage());
			throw new CustomException(e.getMessage());
		}

		AuthenticationToken token = authenticationService.getToken(user);

		if (!Objects.nonNull(token)) {
			throw new CustomException(MessageStrings.AUTH_TOKEN_NOT_PRESENT);
		}

		return new SignInResponseDto("success", token.getToken());
	}
}
