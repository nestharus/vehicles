package org.nes.vehicle.exception;

public class FetchInvalidEntitiesException extends Exception {
	public FetchInvalidEntitiesException() {
		super("Attempted to fetch an entity that did not exist");
	}
}