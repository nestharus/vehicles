package org.nes.vehicle.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Vehicle implements ApplicationEntity {
	@Id
	@GeneratedValue
	private int id;

	@Min(1950)
	@Max(2050)
	private int year;

	@NotEmpty
	@NotNull
	private String make;

	@NotEmpty
	@NotNull
	private String model;
}