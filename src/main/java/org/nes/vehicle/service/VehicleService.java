package org.nes.vehicle.service;

import org.nes.vehicle.domain.Vehicle;
import org.nes.vehicle.exception.CreateExistingEntitiesException;
import org.nes.vehicle.exception.DeleteInvalidEntitiesException;
import org.nes.vehicle.exception.FetchInvalidEntitiesException;
import org.nes.vehicle.exception.UpdateInvalidEntitiesException;

import javax.validation.Valid;
import java.util.List;

// more or less made the methods that the controller needed
public interface VehicleService {
	List<Vehicle> findVehicles(final int year, final String make, final String model);

	List<Vehicle> listAllVehicles();

	Vehicle getVehicleById(int id) throws FetchInvalidEntitiesException;

	List<Vehicle> createVehicles(@Valid List<Vehicle> vehicles) throws CreateExistingEntitiesException;

	List<Vehicle> updateVehicles(@Valid List<Vehicle> vehicles) throws UpdateInvalidEntitiesException;

	void deleteVehicleById(int id) throws DeleteInvalidEntitiesException;

	boolean allVehiclesExist(@Valid List<Vehicle> vehicles);

	boolean anyVehiclesExist(@Valid List<Vehicle> vehicles);
}