package com.astontech.boozhound.repository;

import com.astontech.boozhound.domain.Distiller;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Distiller entity.
 */
@SuppressWarnings("unused")
public interface DistillerRepository extends JpaRepository<Distiller,Long> {

}
