package org.nes.vehicle.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nes.vehicle.ErrorUtilities;
import org.nes.vehicle.dto.VehicleDto;
import org.nes.vehicle.exception.FetchInvalidEntitiesException;
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
public class GetVehicleByIdTest {
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
	public void getSuccess() throws Exception {
		final var restTemplate = new RestTemplate();
		final var vehicle = createVehicle(2000, "a", "a");
		final var createResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Collections.singletonList(vehicle)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		final var getResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles/" + createResponse.getBody().get(0).id,
			HttpMethod.GET,
			HttpEntity.EMPTY,
			VehicleDto.class
		);

		assertEquals(HttpStatus.FOUND, getResponse.getStatusCode());

		assertEquals(createResponse.getBody().get(0).id, getResponse.getBody().id);
		assertEquals(createResponse.getBody().get(0).year, getResponse.getBody().year);
		assertEquals(createResponse.getBody().get(0).make, getResponse.getBody().make);
		assertEquals(createResponse.getBody().get(0).model, getResponse.getBody().model);
	}

	@Test
	public void getFailure() {
		final var restTemplate = new RestTemplate();
		final var getResponse = assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.exchange(
			"http://localhost:" + port + "/vehicles/5000",
			HttpMethod.GET,
			HttpEntity.EMPTY,
			VehicleDto.class
		));

		assertEquals(ErrorUtilities.serverErrorMessage(HttpStatus.NOT_FOUND, new FetchInvalidEntitiesException()), getResponse.getMessage());
	}
}