package org.nes.vehicle.exception;

public class UpdateInvalidEntitiesException extends Exception {
	public UpdateInvalidEntitiesException() {
		super("Attempted to update entities when some or all of them did not exist");
	}
}