package com.oberla.ecommerce.dto.user;

public class SignUpResponseDto {
	private String message;
	private String status;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public SignUpResponseDto(String status, String message) {
		this.message = message;
		this.status = status;
	}

}
