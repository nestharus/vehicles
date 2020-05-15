package org.nes.vehicle.dto;

import lombok.ToString;

// it's a good idea to return a dto in a controller even if the fields are the same
// swapping out the model for the dto later can be a hassle

@ToString
public class VehicleDto {
	public int id;
	public int year;
	public String make;
	public String model;
}