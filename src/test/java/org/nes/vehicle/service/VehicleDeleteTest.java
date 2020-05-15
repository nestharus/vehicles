package org.nes.vehicle.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nes.vehicle.domain.Vehicle;
import org.nes.vehicle.exception.DeleteInvalidEntitiesException;
import org.nes.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class VehicleDeleteTest {
	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private VehicleRepository vehicleRepository;

	@AfterEach
	public void clean() {
		vehicleRepository.deleteAll();
	}

	private Vehicle createVehicle(final int year, final String make, final String model) {
		final var vehicle = new Vehicle();

		vehicle.setYear(year);
		vehicle.setMake(make);
		vehicle.setModel(model);

		return vehicle;
	}

	@Test
	public void destroyVehicleTestSuccess() throws Exception {
		final var vehicle = createVehicle(2000, "a", "a");
		final var createdVehicles = vehicleService.createVehicles(Collections.singletonList(vehicle));
		final var createdVehicle = createdVehicles.get(0);

		vehicleService.deleteVehicleById(createdVehicle.getId());

		assertFalse(vehicleService.allVehiclesExist(createdVehicles));
	}

	@Test
	public void destroyVehicleTestFail() throws Exception {
		assertThrows(DeleteInvalidEntitiesException.class, () -> vehicleService.deleteVehicleById(5000));
	}
}