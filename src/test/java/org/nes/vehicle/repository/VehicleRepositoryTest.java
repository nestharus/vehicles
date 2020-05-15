package org.nes.vehicle.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nes.vehicle.domain.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class VehicleRepositoryTest {
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
	public void testOneIn() {
		final var vehicle = createVehicle(2000, "a", "a");
		final var savedVehicle = vehicleRepository.save(vehicle);

		assertEquals(1, vehicleRepository.countByIdIn(Collections.singletonList(savedVehicle.getId())));
	}

	@Test
	public void testOneNotIn() {
		assertEquals(0, vehicleRepository.countByIdIn(Collections.singletonList(5000)));
	}

	@Test
	public void testOneAll() {
		final var vehicle = createVehicle(2000, "a", "a");
		final var savedVehicle = vehicleRepository.save(vehicle);

		assertEquals(1, vehicleRepository.countByIdIn(Arrays.asList(savedVehicle.getId(), 5000)));
	}

	@Test
	public void testMultipleIn() {
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2001, "b", "b");
		final var vehicle3 = createVehicle(2002, "c", "c");
		final var savedVehicles = (List<Vehicle>) vehicleRepository.saveAll(Arrays.asList(vehicle1, vehicle2, vehicle3));
		final var ids = savedVehicles.stream()
			.map(Vehicle::getId)
			.collect(Collectors.toList());

		assertEquals(3, vehicleRepository.countByIdIn(ids));
	}

	@Test
	public void testMultipleNotIn() {
		assertEquals(0, vehicleRepository.countByIdIn(Arrays.asList(5000, 5001, 5002)));
	}

	@Test
	public void testMultipleAll() {
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2001, "b", "b");
		final var vehicle3 = createVehicle(2002, "c", "c");
		final var savedVehicles = (List<Vehicle>) vehicleRepository.saveAll(Arrays.asList(vehicle1, vehicle2, vehicle3));
		final var ids = savedVehicles.stream()
			.map(Vehicle::getId)
			.collect(Collectors.toList());

		ids.addAll(Arrays.asList(5000, 5001, 5002));

		assertEquals(3, vehicleRepository.countByIdIn(ids));
	}
}