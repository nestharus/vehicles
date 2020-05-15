package org.nes.vehicle.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nes.vehicle.domain.Vehicle;
import org.nes.vehicle.exception.CreateExistingEntitiesException;
import org.nes.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class VehicleCreateTest {
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
	public void createSingleVehicleTestSuccess() throws Exception {
		final var vehicle = createVehicle(2000, "a", "a");
		final var createdVehicles = vehicleService.createVehicles(Collections.singletonList(vehicle));
		final var createdVehicle = createdVehicles.get(0);

		assertEquals(1, createdVehicles.size());

		assertEquals(vehicle.getYear(), createdVehicle.getYear());
		assertEquals(vehicle.getMake(), createdVehicle.getMake());
		assertEquals(vehicle.getModel(), createdVehicle.getModel());
	}

	@Test
	public void createMultipleVehicleTestSuccess() throws Exception {
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2001, "b", "b");
		final var vehicle3 = createVehicle(2002, "c", "c");
		final var createdVehicles = vehicleService.createVehicles(Arrays.asList(vehicle1, vehicle2, vehicle3));

		assertEquals(3, createdVehicles.size());

		assertNotEquals(createdVehicles.get(0).getId(), createdVehicles.get(1).getId());
		assertNotEquals(createdVehicles.get(0).getId(), createdVehicles.get(2).getId());
		assertNotEquals(createdVehicles.get(1).getId(), createdVehicles.get(2).getId());

		createdVehicles.forEach(vehicle -> {
			switch (vehicle.getYear()) {
				case 2000:
					assertEquals(vehicle1.getMake(), vehicle.getMake());
					assertEquals(vehicle1.getModel(), vehicle.getModel());

					break;
				case 2001:
					assertEquals(vehicle2.getMake(), vehicle.getMake());
					assertEquals(vehicle2.getModel(), vehicle.getModel());

					break;
				case 2002:
					assertEquals(vehicle3.getMake(), vehicle.getMake());
					assertEquals(vehicle3.getModel(), vehicle.getModel());

					break;
			}
		});
	}

	@Test
	public void createSingleVehicleTestFail() throws Exception {
		final var vehicle = createVehicle(2000, "a", "a");
		final var createdVehicles = vehicleService.createVehicles(Collections.singletonList(vehicle));

		assertThrows(CreateExistingEntitiesException.class, () -> vehicleService.createVehicles(createdVehicles));
	}

	@Test
	public void createMultipleVehicleTestCompleteFail() throws Exception {
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2001, "b", "b");
		final var vehicle3 = createVehicle(2002, "c", "c");
		final var createdVehicles = vehicleService.createVehicles(Arrays.asList(vehicle1, vehicle2, vehicle3));

		assertThrows(CreateExistingEntitiesException.class, () -> vehicleService.createVehicles(createdVehicles));
	}

	@Test
	public void createMultipleVehicleTestPartialFail() throws Exception {
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2001, "b", "b");
		final var vehicle3 = createVehicle(2002, "c", "c");
		final var createdVehicles = vehicleService.createVehicles(Arrays.asList(vehicle1, vehicle2));
		createdVehicles.add(vehicle3);

		assertThrows(CreateExistingEntitiesException.class, () -> vehicleService.createVehicles(createdVehicles));
	}
}