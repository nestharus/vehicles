package org.nes.vehicle.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.nes.vehicle.domain.Vehicle;
import org.nes.vehicle.dto.VehicleDto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-05-15T06:01:42-0700",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 12 (Oracle Corporation)"
)
@Component
public class VehicleMapperImpl extends VehicleMapper {

    @Override
    public Vehicle fromClientDto(VehicleDto source) {
        if ( source == null ) {
            return null;
        }

        Vehicle vehicle = new Vehicle();

        vehicle.setId( source.id );
        vehicle.setYear( source.year );
        if ( source.make != null ) {
            vehicle.setMake( source.make );
        }
        if ( source.model != null ) {
            vehicle.setModel( source.model );
        }

        return vehicle;
    }

    @Override
    public List<Vehicle> fromClientDto(List<VehicleDto> source) {
        if ( source == null ) {
            return null;
        }

        List<Vehicle> list = new ArrayList<Vehicle>( source.size() );
        for ( VehicleDto vehicleDto : source ) {
            list.add( fromClientDto( vehicleDto ) );
        }

        return list;
    }

    @Override
    public VehicleDto toClientDto(Vehicle source) {
        if ( source == null ) {
            return null;
        }

        VehicleDto vehicleDto = new VehicleDto();

        vehicleDto.id = source.getId();
        vehicleDto.year = source.getYear();
        if ( source.getMake() != null ) {
            vehicleDto.make = source.getMake();
        }
        if ( source.getModel() != null ) {
            vehicleDto.model = source.getModel();
        }

        return vehicleDto;
    }

    @Override
    public List<VehicleDto> toClientDto(List<Vehicle> source) {
        if ( source == null ) {
            return null;
        }

        List<VehicleDto> list = new ArrayList<VehicleDto>( source.size() );
        for ( Vehicle vehicle : source ) {
            list.add( toClientDto( vehicle ) );
        }

        return list;
    }
}
