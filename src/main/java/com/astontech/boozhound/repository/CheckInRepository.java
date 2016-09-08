package com.astontech.boozhound.repository;

import com.astontech.boozhound.domain.CheckIn;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CheckIn entity.
 */
@SuppressWarnings("unused")
public interface CheckInRepository extends JpaRepository<CheckIn,Long> {

}
