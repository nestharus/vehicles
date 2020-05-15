package org.nes.vehicle.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nes.vehicle.domain.Vehicle;
import org.nes.vehicle.exception.UpdateInvalidEntitiesException;
import org.nes.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class VehicleUpdateTest {
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
	public void updateSingleVehicleTestSuccess() throws Exception {
		var vehicle = createVehicle(2000, "a", "a");
		final var createdVehicles = vehicleService.createVehicles(Collections.singletonList(vehicle));
		final var createdVehicle = createdVehicles.get(0);
		final var id = createdVehicle.getId();

		vehicle = createVehicle(2000, "aa", "a");
		vehicle.setId(id);

		final var updatedVehicles = vehicleService.updateVehicles(Collections.singletonList(vehicle));
		final var updatedVehicle = updatedVehicles.get(0);

		assertEquals(1, updatedVehicles.size());

		assertEquals(vehicle.getYear(), updatedVehicle.getYear());
		assertEquals(vehicle.getMake(), updatedVehicle.getMake());
		assertEquals(vehicle.getModel(), updatedVehicle.getModel());
	}

	@Test
	public void updateMultipleVehicleTestSuccess() throws Exception {
		var vehicle1 = createVehicle(2000, "a", "a");
		var vehicle2 = createVehicle(2001, "b", "b");
		var vehicle3 = createVehicle(2002, "c", "c");
		final var createdVehicles = vehicleService.createVehicles(Arrays.asList(vehicle1, vehicle2, vehicle3));

		vehicle1 = createVehicle(2000, "aa", "a");
		vehicle2 = createVehicle(2001, "bb", "b");
		vehicle3 = createVehicle(2002, "cc", "c");

		// vehicles 1-3 aren't final
		for (var vehicle : createdVehicles) {
			switch (vehicle.getYear()) {
				case 2000:
					vehicle1.setId(vehicle.getId());
					break;

				case 2001:
					vehicle2.setId(vehicle.getId());
					break;

				case 2002:
					vehicle3.setId(vehicle.getId());
					break;
			}
		}

		final var updatedVehicles = vehicleService.updateVehicles(Arrays.asList(vehicle1, vehicle2, vehicle3));

		for (var vehicle : updatedVehicles) {
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
		}
	}

	@Test
	public void updateSingleVehicleTestFail() throws Exception {
		final var vehicle = createVehicle(2000, "a", "a");
		vehicle.setId(5000);

		assertThrows(UpdateInvalidEntitiesException.class, () -> vehicleService.updateVehicles(Collections.singletonList(vehicle)));
	}

	@Test
	public void createMultipleVehicleTestCompleteFail() throws Exception {
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2001, "b", "b");
		final var vehicle3 = createVehicle(2002, "c", "c");

		vehicle1.setId(5000);
		vehicle2.setId(5001);
		vehicle3.setId(5002);

		assertThrows(UpdateInvalidEntitiesException.class, () -> vehicleService.updateVehicles(Arrays.asList(vehicle1, vehicle2, vehicle3)));
	}

	@Test
	public void createMultipleVehicleTestPartialFail() throws Exception {
		var vehicle1 = createVehicle(2000, "a", "a");
		var vehicle2 = createVehicle(2001, "b", "b");
		var vehicle3 = createVehicle(2002, "c", "c");
		final var createdVehicles = vehicleService.createVehicles(Arrays.asList(vehicle1, vehicle2));

		vehicle1 = createVehicle(2000, "aa", "a");
		vehicle2 = createVehicle(2001, "bb", "b");
		vehicle3 = createVehicle(2002, "cc", "c");

		for (var vehicle : createdVehicles) {
			switch (vehicle.getYear()) {
				case 2000:
					vehicle1.setId(vehicle.getId());
					break;

				case 2001:
					vehicle2.setId(vehicle.getId());
					break;

				case 2002:
					vehicle3.setId(vehicle.getId());
					break;
			}
		}

		vehicle3.setId(5000);

		createdVehicles.add(vehicle3);

		assertThrows(UpdateInvalidEntitiesException.class, () -> vehicleService.updateVehicles(createdVehicles));
	}
}