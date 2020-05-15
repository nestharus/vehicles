package org.nes.vehicle.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nes.vehicle.dto.VehicleDto;
import org.nes.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class ValidationVehiclesTest {
	@Autowired
	private VehicleRepository vehicleRepository;

	@LocalServerPort
	private int port;

	@AfterEach
	public void clean() {
		vehicleRepository.deleteAll();
	}

	private VehicleDto createVehicle(final int year, final String make, final String model) {
		final var vehicle = new VehicleDto();

		vehicle.year = year;
		vehicle.make = make;
		vehicle.model = model;

		return vehicle;
	}

	@Test
	public void yearMinimum() {
		final var restTemplate = new RestTemplate();
		final var vehicle = createVehicle(1950, "a", "a");
		final var createResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Collections.singletonList(vehicle)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
	}

	@Test
	public void yearMaximum() {
		final var restTemplate = new RestTemplate();
		final var vehicle = createVehicle(2050, "a", "a");
		final var createResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Collections.singletonList(vehicle)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
	}

	@Test
	public void yearTooLow() {
		final var restTemplate = new RestTemplate();
		final var vehicle = createVehicle(1949, "a", "a");
		final var createResponse = assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Collections.singletonList(vehicle)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		));

		assertEquals("400 : [createVehicles.arg0[0].year: must be greater than or equal to 1950]", createResponse.getMessage());
	}

	@Test
	public void yearTooHigh() {
		final var restTemplate = new RestTemplate();
		final var vehicle = createVehicle(2051, "a", "a");
		final var createResponse = assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Collections.singletonList(vehicle)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		));

		assertEquals("400 : [createVehicles.arg0[0].year: must be less than or equal to 2050]", createResponse.getMessage());
	}

	@Test
	public void makeEmpty() {
		final var restTemplate = new RestTemplate();
		final var vehicle = createVehicle(2000, "", "a");
		final var createResponse = assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Collections.singletonList(vehicle)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		));

		assertEquals("400 : [createVehicles.arg0[0].make: must not be empty]", createResponse.getMessage());
	}

	@Test
	public void makeNull() {
		final var restTemplate = new RestTemplate();
		final var vehicle = createVehicle(2000, null, "a");
		final var createResponse = assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Collections.singletonList(vehicle)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		));

		final var errors = createResponse.getMessage()
			.replaceAll("400 : \\[", "")
			.replaceAll("]$", "")
			.split(",");

		assertEquals(2, errors.length);
		assertTrue("400 : [createVehicles.arg0[0].make: must not be empty, createVehicles.arg0[0].make: must not be null]".contains(errors[0].trim()));
		assertTrue("400 : [createVehicles.arg0[0].make: must not be empty, createVehicles.arg0[0].make: must not be null]".contains(errors[1].trim()));
	}

	@Test
	public void modelEmpty() {
		final var restTemplate = new RestTemplate();
		final var vehicle = createVehicle(2000, "a", "");
		final var createResponse = assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Collections.singletonList(vehicle)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		));

		assertEquals("400 : [createVehicles.arg0[0].model: must not be empty]", createResponse.getMessage());
	}

	@Test
	public void modelNull() {
		final var restTemplate = new RestTemplate();
		final var vehicle = createVehicle(2000, "a", null);
		final var createResponse = assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Collections.singletonList(vehicle)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		));

		final var errors = createResponse.getMessage()
			.replaceAll("400 : \\[", "")
			.replaceAll("]$", "")
			.split(",");

		assertEquals(2, errors.length);
		assertTrue("400 : [createVehicles.arg0[0].model: must not be empty, createVehicles.arg0[0].model: must not be null]".contains(errors[0].trim()));
		assertTrue("400 : [createVehicles.arg0[0].model: must not be empty, createVehicles.arg0[0].model: must not be null]".contains(errors[1].trim()));
	}
}