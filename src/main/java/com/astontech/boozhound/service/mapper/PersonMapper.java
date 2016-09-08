package com.astontech.boozhound.service.mapper;

import com.astontech.boozhound.domain.*;
import com.astontech.boozhound.service.dto.PersonDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Person and its DTO PersonDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface PersonMapper {

    @Mapping(source = "user.id", target = "userId")
    PersonDTO personToPersonDTO(Person person);

    List<PersonDTO> peopleToPersonDTOs(List<Person> people);

    @Mapping(source = "userId", target = "user")
    Person personDTOToPerson(PersonDTO personDTO);

    List<Person> personDTOsToPeople(List<PersonDTO> personDTOs);
}
