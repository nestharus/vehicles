package org.nes.vehicle;

import org.springframework.http.HttpStatus;

public class ErrorUtilities {
	public static String serverErrorMessage(final HttpStatus status, final Exception exception) {
		return status.value() + " : [" + exception.getMessage() + "]";
	}
}