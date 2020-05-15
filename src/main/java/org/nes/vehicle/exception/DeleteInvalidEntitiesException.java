package org.nes.vehicle.exception;

public class DeleteInvalidEntitiesException extends Exception {
	public DeleteInvalidEntitiesException() {
		super("Attempted to delete entities when some or all of them did not exist");
	}
}