package com.springbboot.shortestRoutesGhyCovidFacilities;

import java.util.Stack;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/*Aspect for cross-cutting concerns*/

@Component
@Aspect
public class MyAop {
	
	@Autowired
	ControllerApp control;
	
	/*Around advice for checking valid source and destination before routing algorithm methods and checking valid route after methods return */
	
	@Around("com.springbboot.shortestRoutesGhyCovidFacilities.MyPointCuts.routeFinder()")
	Stack<String> aroundRoutes(ProceedingJoinPoint jp) throws Throwable
	{
		if( control.getUserLoc().isEmpty() || control.getDestination().isEmpty() )
	      { 
			control.setMessage("Kindly set both the user location and destination before finding routes!");
	        return new Stack<String>();
	      }
		
		Stack<String> route = (Stack<String>)jp.proceed();
	       
	    if(route.isEmpty())
	       control.setMessage("No route found");
	    else
	       control.setMessage("Approximate distance is "+(String.format("%.4f",control.getRouteDist()))+" meters or "+(String.format("%.4f",control.getRouteDist()/1000.0))+" kilometers");     
	       
	    return route;
		
	}

}
