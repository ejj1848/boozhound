package com.astontech.boozhound.service.mapper;

import com.astontech.boozhound.domain.*;
import com.astontech.boozhound.service.dto.LocationDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Location and its DTO LocationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LocationMapper {

    @Mapping(source = "distiller.id", target = "distillerId")
    LocationDTO locationToLocationDTO(Location location);

    List<LocationDTO> locationsToLocationDTOs(List<Location> locations);

    @Mapping(source = "distillerId", target = "distiller")
    @Mapping(target = "checkin", ignore = true)
    Location locationDTOToLocation(LocationDTO locationDTO);

    List<Location> locationDTOsToLocations(List<LocationDTO> locationDTOs);

    default Distiller distillerFromId(Long id) {
        if (id == null) {
            return null;
        }
        Distiller distiller = new Distiller();
        distiller.setId(id);
        return distiller;
    }
}
