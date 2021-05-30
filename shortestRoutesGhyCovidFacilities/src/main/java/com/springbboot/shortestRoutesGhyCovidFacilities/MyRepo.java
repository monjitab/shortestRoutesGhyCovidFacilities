package com.springbboot.shortestRoutesGhyCovidFacilities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/*Spring Data Repository for CovidFacility entity*/

public interface MyRepo extends JpaRepository<CovidFacility,Integer> {

	List<CovidFacility> findByType(String type);
	
	List<CovidFacility> findByName(String name);

}
