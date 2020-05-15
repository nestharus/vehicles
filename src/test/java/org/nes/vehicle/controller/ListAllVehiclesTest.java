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
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class ListAllVehiclesTest {
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
	public void listSuccess() throws Exception {
		final var restTemplate = new RestTemplate();
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2001, "b", "b");
		final var vehicle3 = createVehicle(2002, "c", "c");
		final var createResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Arrays.asList(vehicle1, vehicle2, vehicle3)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		final var getResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.GET,
			HttpEntity.EMPTY,
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		assertEquals(HttpStatus.OK, getResponse.getStatusCode());

		assertEquals(createResponse.getBody().size(), getResponse.getBody().size());
	}

	@Test
	public void yearTest() throws Exception {
		final var restTemplate = new RestTemplate();
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2000, "b", "b");
		final var vehicle3 = createVehicle(2002, "c", "c");
		final var createResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Arrays.asList(vehicle1, vehicle2, vehicle3)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		final var getResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles?year=2000",
			HttpMethod.GET,
			HttpEntity.EMPTY,
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		assertEquals(HttpStatus.OK, getResponse.getStatusCode());

		assertEquals(2, getResponse.getBody().size());
	}

	@Test
	public void makeTest() throws Exception {
		final var restTemplate = new RestTemplate();
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2001, "a", "b");
		final var vehicle3 = createVehicle(2002, "c", "c");
		final var createResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Arrays.asList(vehicle1, vehicle2, vehicle3)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		final var getResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles?make=a",
			HttpMethod.GET,
			HttpEntity.EMPTY,
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		assertEquals(HttpStatus.OK, getResponse.getStatusCode());

		assertEquals(2, getResponse.getBody().size());
	}

	@Test
	public void modelTest() throws Exception {
		final var restTemplate = new RestTemplate();
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2001, "b", "dd");
		final var vehicle3 = createVehicle(2002, "c", "dd");
		final var createResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Arrays.asList(vehicle1, vehicle2, vehicle3)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		final var getResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles?model=dd",
			HttpMethod.GET,
			HttpEntity.EMPTY,
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		assertEquals(HttpStatus.OK, getResponse.getStatusCode());

		assertEquals(2, getResponse.getBody().size());
	}

	@Test
	public void yearMakeTest() throws Exception {
		final var restTemplate = new RestTemplate();
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2000, "b", "b");
		final var vehicle3 = createVehicle(2002, "b", "c");
		final var createResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Arrays.asList(vehicle1, vehicle2, vehicle3)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		final var getResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles?year=2000&make=b",
			HttpMethod.GET,
			HttpEntity.EMPTY,
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		assertEquals(HttpStatus.OK, getResponse.getStatusCode());

		assertEquals(1, getResponse.getBody().size());

		assertEquals(vehicle2.model, getResponse.getBody().get(0).model);
	}

	@Test
	public void yearMakeModelTest() throws Exception {
		final var restTemplate = new RestTemplate();
		final var vehicle1 = createVehicle(2000, "a", "a");
		final var vehicle2 = createVehicle(2000, "b", "b");
		final var vehicle3 = createVehicle(2000, "c", "c");
		final var vehicle4 = createVehicle(2001, "a", "a");
		final var vehicle5 = createVehicle(2001, "b", "b");
		final var vehicle6 = createVehicle(2001, "c", "c");
		final var createResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles",
			HttpMethod.POST,
			new HttpEntity<>(Arrays.asList(vehicle1, vehicle2, vehicle3, vehicle4, vehicle5, vehicle6)),
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		final var getResponse = restTemplate.exchange(
			"http://localhost:" + port + "/vehicles?year=2001&make=b&model=b",
			HttpMethod.GET,
			HttpEntity.EMPTY,
			new ParameterizedTypeReference<List<VehicleDto>>() { }
		);

		assertEquals(HttpStatus.OK, getResponse.getStatusCode());

		assertEquals(1, getResponse.getBody().size());

		assertEquals(vehicle5.year, getResponse.getBody().get(0).year);
		assertEquals(vehicle5.make, getResponse.getBody().get(0).make);
		assertEquals(vehicle5.model, getResponse.getBody().get(0).model);
	}
}