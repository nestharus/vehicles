package org.nes.vehicle.mapper.converter;

import org.nes.vehicle.domain.ApplicationEntity;
import org.nes.vehicle.mapper.annotation.EntityToId;

import java.util.List;
import java.util.stream.Collectors;

// converters can be utilized by mappers via qualifiedBy
// converters are kinda my own convention

public class EntityConverter {
	@EntityToId
	public static <T extends ApplicationEntity> List<Integer> ids(final List<T> entities) {
		return entities.stream()
			.map(ApplicationEntity::getId)
			.collect(Collectors.toList());
	}
}