package org.nes.vehicle.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nes.vehicle.domain.Vehicle;
import org.nes.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class VehicleAllExistTest {
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
	public void existSingleVehicleTestSuccess() throws Exception {
		final var vehicle = createVehicle(2000, "a", "a");
		final var createdVehicles = vehicleService.createVehicles(Collections.singletonList(vehicle));

		assertTrue(vehicleService.allVehiclesExist(createdVehicles));
	}

	@Test
	public void exitMultipleVehicleTestSuccess() throws Exception {
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2001, "b", "b");
		final var vehicle3 = createVehicle(2002, "c", "c");
		final var createdVehicles = vehicleService.createVehicles(Arrays.asList(vehicle1, vehicle2, vehicle3));

		assertTrue(vehicleService.allVehiclesExist(createdVehicles));
	}

	@Test
	public void exitSingleVehicleTestFail() throws Exception {
		final var vehicle = createVehicle(2000, "a", "a");

		vehicle.setId(5000);

		final var createdVehicles = Collections.singletonList(vehicle);

		assertFalse(vehicleService.allVehiclesExist(createdVehicles));
	}

	@Test
	public void exitMultipleVehicleTestCompleteFail() throws Exception {
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2001, "b", "b");
		final var vehicle3 = createVehicle(2002, "c", "c");

		vehicle1.setId(5000);
		vehicle2.setId(5001);
		vehicle3.setId(5002);

		final var createdVehicles = Arrays.asList(vehicle1, vehicle2, vehicle3);

		assertFalse(vehicleService.allVehiclesExist(createdVehicles));
	}

	@Test
	public void createMultipleVehicleTestPartialFail() throws Exception {
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2001, "b", "b");
		final var vehicle3 = createVehicle(2002, "c", "c");
		final var createdVehicles = vehicleService.createVehicles(Arrays.asList(vehicle1, vehicle2));

		vehicle3.setId(5000);

		createdVehicles.add(vehicle3);

		assertFalse(vehicleService.allVehiclesExist(createdVehicles));
	}
}