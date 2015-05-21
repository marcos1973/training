package com.philos.frameit.jobs;

public class UnknownOrderStatusException extends Exception {

	/**
	 * Constructor for UnknownOrderStatusException.
	 * 
	 * @param message the error message
	 * @param cause the exception to wrap
	 */
	public UnknownOrderStatusException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor for UnknownOrderStatusException.
	 * 
	 * @param message the error message
	 */
	public UnknownOrderStatusException(final String message) {
		super(message);
	}
}
