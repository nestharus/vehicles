package org.nes.vehicle.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nes.vehicle.domain.Vehicle;
import org.nes.vehicle.dto.VehicleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class VehicleMapperTest {
	@Autowired
	private VehicleMapper vehicleMapper;

	@Test
	public void toDtoTest() {
		final var model = new Vehicle();

		model.setId(5000);
		model.setYear(2000);
		model.setModel("a");
		model.setMake("a");

		final var dto = vehicleMapper.toClientDto(model);

		assertEquals(model.getId(), dto.id);
		assertEquals(model.getYear(), dto.year);
		assertEquals(model.getModel(), dto.model);
		assertEquals(model.getMake(), dto.make);
	}

	@Test
	public void toDtoListTest() {
		final var model1 = new Vehicle();
		final var model2 = new Vehicle();
		final var model3 = new Vehicle();

		model1.setId(5000);
		model1.setYear(2000);
		model1.setModel("a");
		model1.setMake("a");

		model2.setId(5001);
		model2.setYear(2001);
		model2.setModel("b");
		model2.setMake("b");

		model3.setId(5002);
		model3.setYear(2002);
		model3.setModel("c");
		model3.setMake("c");

		final var dtos = vehicleMapper.toClientDto(Arrays.asList(model1, model2, model3));

		assertEquals(model1.getId(), dtos.get(0).id);
		assertEquals(model1.getYear(), dtos.get(0).year);
		assertEquals(model1.getModel(), dtos.get(0).model);
		assertEquals(model1.getMake(), dtos.get(0).make);

		assertEquals(model2.getId(), dtos.get(1).id);
		assertEquals(model2.getYear(), dtos.get(1).year);
		assertEquals(model2.getModel(), dtos.get(1).model);
		assertEquals(model2.getMake(), dtos.get(1).make);

		assertEquals(model3.getId(), dtos.get(2).id);
		assertEquals(model3.getYear(), dtos.get(2).year);
		assertEquals(model3.getModel(), dtos.get(2).model);
		assertEquals(model3.getMake(), dtos.get(2).make);
	}

	@Test
	public void toModelTest() {
		final var dto = new VehicleDto();

		dto.id = 5000;
		dto.year = 2000;
		dto.model = "a";
		dto.make = "a";

		final var model = vehicleMapper.fromClientDto(dto);

		assertEquals(dto.id, model.getId());
		assertEquals(dto.year, model.getYear());
		assertEquals(dto.model, model.getModel());
		assertEquals(dto.make, model.getMake());
	}

	@Test
	public void toModelListTest() {
		final var dto1 = new VehicleDto();
		final var dto2 = new VehicleDto();
		final var dto3 = new VehicleDto();

		dto1.id = 5000;
		dto1.year = 2000;
		dto1.model = "a";
		dto1.make = "a";

		dto2.id = 5001;
		dto2.year = 2001;
		dto2.model = "b";
		dto2.make = "b";

		dto3.id = 5002;
		dto3.year = 2002;
		dto3.model = "c";
		dto3.make = "c";

		final var models = vehicleMapper.fromClientDto(Arrays.asList(dto1, dto2, dto3));

		assertEquals(dto1.id, models.get(0).getId());
		assertEquals(dto1.year, models.get(0).getYear());
		assertEquals(dto1.model, models.get(0).getModel());
		assertEquals(dto1.make, models.get(0).getMake());

		assertEquals(dto2.id, models.get(1).getId());
		assertEquals(dto2.year, models.get(1).getYear());
		assertEquals(dto2.model, models.get(1).getModel());
		assertEquals(dto2.make, models.get(1).getMake());

		assertEquals(dto3.id, models.get(2).getId());
		assertEquals(dto3.year, models.get(2).getYear());
		assertEquals(dto3.model, models.get(2).getModel());
		assertEquals(dto3.make, models.get(2).getMake());
	}
}