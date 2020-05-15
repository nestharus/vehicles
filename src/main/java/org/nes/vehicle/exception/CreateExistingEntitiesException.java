package org.nes.vehicle.exception;

public class CreateExistingEntitiesException extends Exception {
	public CreateExistingEntitiesException() {
		super("Attempted to create entities when some or all of them already existed");
	}
}