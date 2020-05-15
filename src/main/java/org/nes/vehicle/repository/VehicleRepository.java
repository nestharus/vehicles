package org.nes.vehicle.repository;

import org.nes.vehicle.domain.Vehicle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

// I was excited to use java14 text blocks and ... didn't work D:
public interface VehicleRepository extends CrudRepository<Vehicle, Integer> {
	@Query(
		"select vehicle " +
			"from #{#entityName} vehicle " +
			"where " +
			"(:year = 0 or :year = vehicle.year) and " +
			"(:make = '' or :make = vehicle.make) and " +
			"(:model = '' or :model = vehicle.model)"
	)
	List<Vehicle> findByYearAndMakeAndModel(
		@Param("year") final int year,
		@Param("make") final String make,
		@Param("model") final String model
	);

	int countByIdIn(Iterable<Integer> ids);
}