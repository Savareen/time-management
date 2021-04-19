package com.igorzaitcev.management.exceptions;

public class MissingArgumentException extends RuntimeException {

	private static final long serialVersionUID = -5238656184964982200L;

	public MissingArgumentException(String message) {
		super(message);
	}

}
