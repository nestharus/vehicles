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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class VehicleListAllTest {
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
	public void listVehicleSingleTestSuccess() throws Exception {
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2001, "b", "b");
		final var vehicle3 = createVehicle(2002, "c", "c");
		vehicleService.createVehicles(Arrays.asList(vehicle1, vehicle2, vehicle3));
		final var createdVehicles = vehicleService.listAllVehicles();

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
	public void listVehicleMultipleTestSuccess() throws Exception {
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2001, "b", "b");
		final var vehicle3 = createVehicle(2002, "c", "c");
		final var vehicle4 = createVehicle(2003, "d", "d");
		final var vehicle5 = createVehicle(2004, "e", "e");
		final var vehicle6 = createVehicle(2005, "f", "f");
		vehicleService.createVehicles(Arrays.asList(vehicle1, vehicle2, vehicle3));
		vehicleService.createVehicles(Arrays.asList(vehicle4, vehicle5, vehicle6));
		final var createdVehicles = vehicleService.listAllVehicles();

		assertEquals(6, createdVehicles.size());

		assertNotEquals(createdVehicles.get(0).getId(), createdVehicles.get(1).getId());
		assertNotEquals(createdVehicles.get(0).getId(), createdVehicles.get(2).getId());
		assertNotEquals(createdVehicles.get(0).getId(), createdVehicles.get(3).getId());
		assertNotEquals(createdVehicles.get(0).getId(), createdVehicles.get(4).getId());
		assertNotEquals(createdVehicles.get(0).getId(), createdVehicles.get(5).getId());
		assertNotEquals(createdVehicles.get(1).getId(), createdVehicles.get(2).getId());
		assertNotEquals(createdVehicles.get(1).getId(), createdVehicles.get(3).getId());
		assertNotEquals(createdVehicles.get(1).getId(), createdVehicles.get(4).getId());
		assertNotEquals(createdVehicles.get(1).getId(), createdVehicles.get(5).getId());
		assertNotEquals(createdVehicles.get(2).getId(), createdVehicles.get(3).getId());
		assertNotEquals(createdVehicles.get(2).getId(), createdVehicles.get(4).getId());
		assertNotEquals(createdVehicles.get(2).getId(), createdVehicles.get(5).getId());
		assertNotEquals(createdVehicles.get(3).getId(), createdVehicles.get(4).getId());
		assertNotEquals(createdVehicles.get(3).getId(), createdVehicles.get(5).getId());
		assertNotEquals(createdVehicles.get(4).getId(), createdVehicles.get(5).getId());

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
				case 2003:
					assertEquals(vehicle4.getMake(), vehicle.getMake());
					assertEquals(vehicle4.getModel(), vehicle.getModel());

					break;
				case 2004:
					assertEquals(vehicle5.getMake(), vehicle.getMake());
					assertEquals(vehicle5.getModel(), vehicle.getModel());

					break;
				case 2005:
					assertEquals(vehicle6.getMake(), vehicle.getMake());
					assertEquals(vehicle6.getModel(), vehicle.getModel());

					break;
			}
		});
	}
}