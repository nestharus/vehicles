package org.nes.vehicle.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nes.vehicle.domain.Vehicle;
import org.nes.vehicle.dto.VehicleDto;
import org.nes.vehicle.mapper.VehicleMapper;
import org.nes.vehicle.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// made root route of application / and controller /vehicles
// didn't implement security because it's all public anyways

@Validated
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/vehicles")
public class VehicleController {
	// the way I prefer to do dependency injection
	// I don't like boilerplate code ^)^
	private final VehicleService vehicleService;
	private final VehicleMapper vehicleMapper;

	@GetMapping
	public ResponseEntity<List<VehicleDto>> listAllVehicles(
		@RequestParam(value = "year", required = false, defaultValue = "0") final int year,
		@RequestParam(value = "make", required = false, defaultValue = "") final String make,
		@RequestParam(value = "model", required = false, defaultValue = "") final String model
	) {
		log.info("Listing Filtered Vehicles");

		// it is smarter to filter on the database rather than the server as findAll is a bad operation anyways
		final var vehicles = vehicleService.findVehicles(year, make, model);

		vehicles.forEach(vehicle -> log.info(vehicle.toString()));

		return response(vehicles, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<VehicleDto> getVehicleById(@PathVariable("id") final int id) throws Exception {
		log.info("Searching for vehicle with id (" + id + ")");

		final var vehicle = vehicleService.getVehicleById(id);

		log.info("Found vehicle");
		log.info(vehicle.toString());

		return response(vehicle, HttpStatus.FOUND);
	}

	@PostMapping
	public ResponseEntity<List<VehicleDto>> createVehicles(@Valid @RequestBody final List<VehicleDto> vehiclesDto) throws Exception {
		log.info("Creating vehicles");
		vehiclesDto.forEach(vehicle -> log.info(vehicle.toString()));

		final var vehicles = vehicleMapper.fromClientDto(vehiclesDto);
		final var createdVehicles = vehicleService.createVehicles(vehicles);

		log.info("Created vehicles");
		createdVehicles.forEach(vehicle -> log.info(vehicle.toString()));

		return response(createdVehicles, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<List<VehicleDto>> updateVehicles(@Valid @RequestBody final List<VehicleDto> vehiclesDto) throws Exception {
		log.info("Updating vehicles");
		vehiclesDto.forEach(vehicle -> log.info(vehicle.toString()));

		final var vehicles = vehicleMapper.fromClientDto(vehiclesDto);
		final var updatedVehicles = vehicleService.updateVehicles(vehicles);

		log.info("Updated vehicles");
		vehiclesDto.forEach(vehicle -> log.info(vehicle.toString()));

		return response(updatedVehicles, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteVehicle(@PathVariable("id") final int id) throws Exception {
		log.info("Deleting vehicle with id (" + id + ")");

		vehicleService.deleteVehicleById(id);

		log.info("Deleted vehicle with id (" + id + ")");

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// these are kinda specific to this controller
	private ResponseEntity<VehicleDto> response(final Vehicle entity, final HttpStatus status) {
		return new ResponseEntity<>(vehicleMapper.toClientDto(entity), status);
	}

	private ResponseEntity<List<VehicleDto>> response(final List<Vehicle> entity, final HttpStatus status) {
		return new ResponseEntity<>(vehicleMapper.toClientDto(entity), status);
	}
}