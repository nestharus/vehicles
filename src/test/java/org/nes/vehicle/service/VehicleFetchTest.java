package org.nes.vehicle.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nes.vehicle.domain.Vehicle;
import org.nes.vehicle.exception.FetchInvalidEntitiesException;
import org.nes.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class VehicleFetchTest {
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
	public void fetchVehicleTestSuccess() throws Exception {
		final var vehicle = createVehicle(2000, "a", "a");
		final var createdVehicles = vehicleService.createVehicles(Collections.singletonList(vehicle));
		final var createdVehicle = vehicleService.getVehicleById(createdVehicles.get(0).getId());

		assertEquals(1, createdVehicles.size());

		assertEquals(vehicle.getYear(), createdVehicle.getYear());
		assertEquals(vehicle.getMake(), createdVehicle.getMake());
		assertEquals(vehicle.getModel(), createdVehicle.getModel());
	}

	@Test
	public void fetchVehicleTestFail() throws Exception {
		assertThrows(FetchInvalidEntitiesException.class, () -> vehicleService.getVehicleById(5000));
	}
}