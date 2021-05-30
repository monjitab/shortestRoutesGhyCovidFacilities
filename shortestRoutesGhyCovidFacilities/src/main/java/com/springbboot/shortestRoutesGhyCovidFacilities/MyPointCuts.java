package com.springbboot.shortestRoutesGhyCovidFacilities;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/*Creating point-cuts for cross-cutting concerns*/

public class MyPointCuts {
	
	@Pointcut("@annotation(com.springbboot.shortestRoutesGhyCovidFacilities.RouteFinder)")
	void routeFinder() {}

}
