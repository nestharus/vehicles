package org.nes.vehicle.service.implementation;

import lombok.RequiredArgsConstructor;
import org.nes.vehicle.mapper.converter.EntityConverter;
import org.nes.vehicle.domain.Vehicle;
import org.nes.vehicle.exception.CreateExistingEntitiesException;
import org.nes.vehicle.exception.DeleteInvalidEntitiesException;
import org.nes.vehicle.exception.FetchInvalidEntitiesException;
import org.nes.vehicle.exception.UpdateInvalidEntitiesException;
import org.nes.vehicle.repository.VehicleRepository;
import org.nes.vehicle.service.VehicleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
	private final VehicleRepository vehicleRepository;

	@Override
	public List<Vehicle> findVehicles(final int year, final String make, final String model) {
		return vehicleRepository.findByYearAndMakeAndModel(year, make, model);
	}

	@Override
	public List<Vehicle> listAllVehicles() {
		return (List<Vehicle>) vehicleRepository.findAll();
	}

	@Override
	public Vehicle getVehicleById(final int id) throws FetchInvalidEntitiesException {
		final var vehicle = vehicleRepository.findById(id);

		if (vehicle.isPresent()) {
			return vehicle.get();
		}
		else {
			throw new FetchInvalidEntitiesException();
		}
	}

	@Override
	public List<Vehicle> createVehicles(@Valid final List<Vehicle> vehicles) throws CreateExistingEntitiesException {
		if (!anyVehiclesExist(vehicles)) {
			return (List<Vehicle>) vehicleRepository.saveAll(vehicles);
		}
		else {
			throw new CreateExistingEntitiesException();
		}
	}

	@Override
	public List<Vehicle> updateVehicles(@Valid final List<Vehicle> vehicles) throws UpdateInvalidEntitiesException {
		if (allVehiclesExist(vehicles)) {
			return (List<Vehicle>) vehicleRepository.saveAll(vehicles);
		}
		else {
			throw new UpdateInvalidEntitiesException();
		}
	}

	@Override
	public void deleteVehicleById(final int id) throws DeleteInvalidEntitiesException {
		if (allVehiclesExistById(Collections.singletonList(id))) {
			vehicleRepository.deleteById(id);
		}
		else {
			throw new DeleteInvalidEntitiesException();
		}
	}

	@Override
	public boolean allVehiclesExist(@Valid final List<Vehicle> vehicles) {
		return allVehiclesExistById(EntityConverter.ids(vehicles));
	}

	@Override
	public boolean anyVehiclesExist(@Valid final List<Vehicle> vehicles) {
		return anyVehiclesExistById(EntityConverter.ids(vehicles));
	}

	private boolean anyVehiclesExistById(final List<Integer> ids) {
		return vehicleRepository.countByIdIn(ids) > 0;
	}

	private boolean allVehiclesExistById(final List<Integer> ids) {
		return vehicleRepository.countByIdIn(ids) == ids.size();
	}
}