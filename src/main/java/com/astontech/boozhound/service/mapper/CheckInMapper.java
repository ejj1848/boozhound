package com.astontech.boozhound.service.mapper;

import com.astontech.boozhound.domain.*;
import com.astontech.boozhound.service.dto.CheckInDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity CheckIn and its DTO CheckInDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CheckInMapper {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "distiller.id", target = "distillerId")
    @Mapping(source = "bourbon.id", target = "bourbonId")
    @Mapping(source = "person.id", target = "personId")
    CheckInDTO checkInToCheckInDTO(CheckIn checkIn);

    List<CheckInDTO> checkInsToCheckInDTOs(List<CheckIn> checkIns);

    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "distillerId", target = "distiller")
    @Mapping(source = "bourbonId", target = "bourbon")
    @Mapping(source = "personId", target = "person")
    CheckIn checkInDTOToCheckIn(CheckInDTO checkInDTO);

    List<CheckIn> checkInDTOsToCheckIns(List<CheckInDTO> checkInDTOs);

    default Location locationFromId(Long id) {
        if (id == null) {
            return null;
        }
        Location location = new Location();
        location.setId(id);
        return location;
    }

    default Distiller distillerFromId(Long id) {
        if (id == null) {
            return null;
        }
        Distiller distiller = new Distiller();
        distiller.setId(id);
        return distiller;
    }

    default Bourbon bourbonFromId(Long id) {
        if (id == null) {
            return null;
        }
        Bourbon bourbon = new Bourbon();
        bourbon.setId(id);
        return bourbon;
    }

    default Person personFromId(Long id) {
        if (id == null) {
            return null;
        }
        Person person = new Person();
        person.setId(id);
        return person;
    }
}
