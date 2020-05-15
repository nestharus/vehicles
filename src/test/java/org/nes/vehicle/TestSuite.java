package org.nes.vehicle;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.ExcludeEngines;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;
import org.nes.vehicle.controller.*;
import org.nes.vehicle.mapper.VehicleMapperTest;
import org.nes.vehicle.mapper.converter.EntityConverter;
import org.nes.vehicle.repository.VehicleRepositoryTest;
import org.nes.vehicle.service.*;

@RunWith(JUnitPlatform.class)
@ExcludeEngines("junit-vintage")
@SelectClasses({
	EntityConverter.class,
	VehicleMapperTest.class,
	VehicleRepositoryTest.class,
	VehicleAllExistTest.class,
	VehicleCreateTest.class,
	VehicleDeleteTest.class,
	VehicleFetchTest.class,
	VehicleListAllTest.class,
	VehicleUpdateTest.class,
	CreateVehiclesTest.class,
	ValidationVehiclesTest.class,
	DeleteVehicleTest.class,
	GetVehicleByIdTest.class,
	ListAllVehiclesTest.class,
	UpdateVehiclesTest.class,
})
public class TestSuite {
}