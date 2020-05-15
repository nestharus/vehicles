package org.nes.vehicle.mapper.converter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.nes.vehicle.domain.ApplicationEntity;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityConverterTest {
	@Getter
	@Setter
	@AllArgsConstructor
	private static class Entity implements ApplicationEntity {
		private int id;
	}

	@Test
	public void entityToIdTest() {
		final var entities = new ArrayList<Entity>();

		IntStream.range(1, 100)
			.mapToObj(Entity::new)
			.forEach(entities::add);

		final var ids = EntityConverter.ids(entities);

		IntStream.range(0, entities.size() - 1)
			.forEach(i -> assertEquals(entities.get(i).getId(), ids.get(i)));
	}
}