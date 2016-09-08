package com.astontech.boozhound.service.mapper;

import com.astontech.boozhound.domain.*;
import com.astontech.boozhound.service.dto.DistillerDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Distiller and its DTO DistillerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DistillerMapper {

    DistillerDTO distillerToDistillerDTO(Distiller distiller);

    List<DistillerDTO> distillersToDistillerDTOs(List<Distiller> distillers);

    @Mapping(target = "checkin", ignore = true)
    Distiller distillerDTOToDistiller(DistillerDTO distillerDTO);

    List<Distiller> distillerDTOsToDistillers(List<DistillerDTO> distillerDTOs);
}
