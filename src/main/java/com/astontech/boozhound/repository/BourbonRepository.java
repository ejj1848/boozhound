package com.astontech.boozhound.repository;

import com.astontech.boozhound.domain.Bourbon;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bourbon entity.
 */
@SuppressWarnings("unused")
public interface BourbonRepository extends JpaRepository<Bourbon,Long> {

}
