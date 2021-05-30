package com.springbboot.shortestRoutesGhyCovidFacilities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/*Custom annotation for cross-cutting concerns*/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RouteFinder {

}
