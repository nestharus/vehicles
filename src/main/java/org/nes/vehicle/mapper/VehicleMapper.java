package org.nes.vehicle.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.nes.vehicle.domain.Vehicle;
import org.nes.vehicle.dto.VehicleDto;
import org.springframework.stereotype.Service;

import java.util.List;

// the naming convention of the methods here seems to work pretty well even as the number of dtos a model maps to
// grows

@Service
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class VehicleMapper {
	@Named("fromClientDto")
	public abstract Vehicle fromClientDto(final VehicleDto source);

	@IterableMapping(qualifiedByName = "fromClientDto")
	public abstract List<Vehicle> fromClientDto(final List<VehicleDto> source);

	@Named("toClientDto")
	public abstract VehicleDto toClientDto(final Vehicle source);

	@IterableMapping(qualifiedByName = "toClientDto")
	public abstract List<VehicleDto> toClientDto(final List<Vehicle> source);
}