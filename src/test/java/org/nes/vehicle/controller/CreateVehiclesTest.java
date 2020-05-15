package org.nes.vehicle.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nes.vehicle.ErrorUtilities;
import org.nes.vehicle.dto.VehicleDto;
import org.nes.vehicle.exception.CreateExistingEntitiesException;
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

// I prefer to test at each layer with more rigorous testing of functionality at the layer that's in charge
// of that functionality
// I still do simple functionality tests at higher layers to ensure that the data gets transferred correctly
// between layers
// see disclaimer on some other practices I do for test cases as well as some bonus stuff

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class CreateVehiclesTest {
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
	public void createSuccess() {
		final var restTemplate = new RestTemplate();
		final var vehicle = createVehicle(2000, "a", "a");
		// if only Java had string interpolation #rip
		final var createResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Collections.singletonList(vehicle)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

		assertNotEquals(null, createResponse.getBody());
		assertEquals(1, createResponse.getBody().size());

		assertEquals(vehicle.year, createResponse.getBody().get(0).year);
		assertEquals(vehicle.make, createResponse.getBody().get(0).make);
		assertEquals(vehicle.model, createResponse.getBody().get(0).model);
	}

	@Test
	public void createFailure() {
		final var restTemplate = new RestTemplate();
		final var vehicle = createVehicle(2000, "a", "a");
		final var createResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Collections.singletonList(vehicle)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);
		final var deleteResponse = assertThrows(HttpClientErrorException.Conflict.class, () -> restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(createResponse.getBody()),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		));

		assertEquals(ErrorUtilities.serverErrorMessage(HttpStatus.CONFLICT, new CreateExistingEntitiesException()), deleteResponse.getMessage());
	}
}