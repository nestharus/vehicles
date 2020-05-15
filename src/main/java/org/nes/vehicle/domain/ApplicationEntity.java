package org.nes.vehicle.domain;

// don't like coupling models with code other than its service, repository, and controller
public interface ApplicationEntity {
	int getId();
	void setId(int id);
}