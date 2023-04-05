package com.oberla.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oberla.ecommerce.dto.user.SignInDto;
import com.oberla.ecommerce.dto.user.SignInResponseDto;
import com.oberla.ecommerce.dto.user.SignUpResponseDto;
import com.oberla.ecommerce.dto.user.SignupDto;
import com.oberla.ecommerce.exceptions.AuthenticationFailException;
import com.oberla.ecommerce.exceptions.CustomException;
import com.oberla.ecommerce.service.UserService;

@RequestMapping("user")
@RestController
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/signup")
	public SignUpResponseDto Signup(@RequestBody SignupDto signupDto) throws CustomException {
		return userService.signUp(signupDto);

	}

	@PostMapping("/signIn")
	public SignInResponseDto Signup(@RequestBody SignInDto signInDto)
			throws CustomException, AuthenticationFailException {
		return userService.signIn(signInDto);
	}

}
