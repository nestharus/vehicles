package org.nes.vehicle.configuration;

import lombok.extern.slf4j.Slf4j;
import org.nes.vehicle.exception.CreateExistingEntitiesException;
import org.nes.vehicle.exception.DeleteInvalidEntitiesException;
import org.nes.vehicle.exception.FetchInvalidEntitiesException;
import org.nes.vehicle.exception.UpdateInvalidEntitiesException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

// because I don't like to spam try/catch blocks in rest controllers

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
	// entity creation exception
	@ExceptionHandler(CreateExistingEntitiesException.class)
	public ResponseEntity<String> handleError(final CreateExistingEntitiesException exception, final WebRequest webRequest) {
		return response(exception, HttpStatus.CONFLICT);
	}

	// entity deletion exception
	@ExceptionHandler(DeleteInvalidEntitiesException.class)
	public ResponseEntity<String> handleError(final DeleteInvalidEntitiesException exception, final WebRequest webRequest) {
		return response(exception, HttpStatus.NOT_FOUND);
	}

	// entity fetch exception
	@ExceptionHandler(FetchInvalidEntitiesException.class)
	public ResponseEntity<String> handleError(final FetchInvalidEntitiesException exception, final WebRequest webRequest) {
		return response(exception, HttpStatus.NOT_FOUND);
	}

	// entity update exception
	@ExceptionHandler(UpdateInvalidEntitiesException.class)
	public ResponseEntity<String> handleError(final UpdateInvalidEntitiesException exception, final WebRequest webRequest) {
		return response(exception, HttpStatus.NOT_FOUND);
	}

	// entity constraint violation exception
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> handleError(final ConstraintViolationException exception, final WebRequest webRequest) {
		return response(exception, HttpStatus.BAD_REQUEST);
	}

	// wrapper to simplify exception methods
	private ResponseEntity<String> response(final Exception exception, final HttpStatus status) {
		log.error(exception.getMessage());

		return new ResponseEntity<>(exception.getMessage(), status);
	}
}