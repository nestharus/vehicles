package org.nes.vehicle.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nes.vehicle.ErrorUtilities;
import org.nes.vehicle.dto.VehicleDto;
import org.nes.vehicle.exception.DeleteInvalidEntitiesException;
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
public class DeleteVehicleTest {
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
	public void deleteSuccess() throws Exception {
		final var restTemplate = new RestTemplate();
		final var vehicle = createVehicle(2000, "a", "a");
		final var createResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Collections.singletonList(vehicle)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		final var deleteResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles/" + createResponse.getBody().get(0).id,
			HttpMethod.DELETE,
			HttpEntity.EMPTY,
			Void.class
		);

		assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
		assertNull(deleteResponse.getBody());
	}

	@Test
	public void deleteFailure() {
		final var restTemplate = new RestTemplate();
		final var deleteResponse = assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.exchange(
			"http://localhost:" + port + "/vehicles/5000",
			HttpMethod.DELETE,
			HttpEntity.EMPTY,
			Void.class
		));

		assertEquals(ErrorUtilities.serverErrorMessage(HttpStatus.NOT_FOUND, new DeleteInvalidEntitiesException()), deleteResponse.getMessage());
	}
}