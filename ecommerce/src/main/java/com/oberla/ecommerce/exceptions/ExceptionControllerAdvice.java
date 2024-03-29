package com.oberla.ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

	@ExceptionHandler(value = CustomException.class)
	public final ResponseEntity<String> handleUpdateFailException(CustomException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ProductNotExistException.class)
	public final ResponseEntity<String> handleUpdateeFailException(ProductNotExistException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = CartItemNotExistException.class)
	public final ResponseEntity<String> handleUpdateeFailException(CartItemNotExistException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = OrderNotFoundException.class)
	public final ResponseEntity<String> handleUpdateeFailException(OrderNotFoundException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = AuthenticationFailException.class)
	public final ResponseEntity<String> handleUpdateeFailException(AuthenticationFailException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

}
