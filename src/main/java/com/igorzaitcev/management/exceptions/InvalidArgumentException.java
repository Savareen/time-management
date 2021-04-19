package com.igorzaitcev.management.exceptions;

public class InvalidArgumentException extends RuntimeException {

	private static final long serialVersionUID = -3629108764193410937L;

	public InvalidArgumentException(String message) {
		super(message);
	}

}
