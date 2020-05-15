package org.nes.vehicle.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nes.vehicle.ErrorUtilities;
import org.nes.vehicle.dto.VehicleDto;
import org.nes.vehicle.exception.UpdateInvalidEntitiesException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class UpdateVehiclesTest {
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
	public void updateSuccess() throws Exception {
		final var restTemplate = new RestTemplate();
		final var vehicle = createVehicle(2000, "a", "a");
		final var createResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Collections.singletonList(vehicle)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		vehicle.id = createResponse.getBody().get(0).id;
		vehicle.year = 2010;
		vehicle.make = "d";
		vehicle.model = "d";

		final var updateResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.PUT,
			new HttpEntity<>(Collections.singletonList(vehicle)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		assertEquals(HttpStatus.ACCEPTED, updateResponse.getStatusCode());

		assertEquals(vehicle.id, updateResponse.getBody().get(0).id);
		assertEquals(vehicle.year, updateResponse.getBody().get(0).year);
		assertEquals(vehicle.make, updateResponse.getBody().get(0).make);
		assertEquals(vehicle.model, updateResponse.getBody().get(0).model);
	}

	@Test
	public void updateFailure() {
		final var restTemplate = new RestTemplate();
		final var vehicle = createVehicle(2000, "a", "a");
		vehicle.id = 10000;
		final var updateResponse = assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.PUT,
			new HttpEntity<>(Collections.singletonList(vehicle)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		));

		assertEquals(ErrorUtilities.serverErrorMessage(HttpStatus.NOT_FOUND, new UpdateInvalidEntitiesException()), updateResponse.getMessage());
	}
}