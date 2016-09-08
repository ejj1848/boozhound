package com.astontech.boozhound.service.mapper;

import com.astontech.boozhound.domain.*;
import com.astontech.boozhound.service.dto.BourbonDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Bourbon and its DTO BourbonDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BourbonMapper {

    @Mapping(source = "distiller.id", target = "distillerId")
    BourbonDTO bourbonToBourbonDTO(Bourbon bourbon);

    List<BourbonDTO> bourbonsToBourbonDTOs(List<Bourbon> bourbons);

    @Mapping(source = "distillerId", target = "distiller")
    @Mapping(target = "checkin", ignore = true)
    Bourbon bourbonDTOToBourbon(BourbonDTO bourbonDTO);

    List<Bourbon> bourbonDTOsToBourbons(List<BourbonDTO> bourbonDTOs);

    default Distiller distillerFromId(Long id) {
        if (id == null) {
            return null;
        }
        Distiller distiller = new Distiller();
        distiller.setId(id);
        return distiller;
    }
}
