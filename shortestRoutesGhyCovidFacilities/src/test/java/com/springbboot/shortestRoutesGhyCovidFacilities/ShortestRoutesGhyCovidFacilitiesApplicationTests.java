package com.springbboot.shortestRoutesGhyCovidFacilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/*SpringBoot Test using JUnit5 for unit-testing service methods*/

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class ShortestRoutesGhyCovidFacilitiesApplicationTests {

	@Autowired
	MyGraph map;
	
	@Autowired
	Autocomplete a;
	
	private String testNodes = "src/main/resources/testFiles/testnodes.txt";
	private String testEdges = "src/main/resources/testFiles/testedges.txt";
	private String testTrie = "src/main/resources/testFiles/trial.txt";
	
	@BeforeAll
    void buildTestMap() throws IOException
    {
    	map.buildMap(testNodes,testEdges);
    }
    
	@BeforeAll
    void buildTestTrie() throws IOException
    {
		ArrayList<String> list = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(new File(testTrie)));
	    String line = br.readLine();
		
		while(line!=null)
		{
			list.add(line);
			line = br.readLine();
		}
		
		a.buildTrie(list);		
    }
	
	@Test
	void testGetDist()
	{
		double dist = map.findDist(23.456, 67.8753, 56.7688, 45.667);
		double ans = 4116812.7293773135;
		assertEquals(ans,dist);
	}
	
	@Test
	void testDijkstra()
	{
		Stack<String> route = map.dijkstra("0 0", "6 6");
		Stack<String> ansroute = new Stack<>();
		ansroute.push(6.0+" "+6.0);
		ansroute.push(5.0+" "+5.0);
		ansroute.push(4.0+" "+4.0);
		ansroute.push(3.0+" "+3.0);
		ansroute.push(1.0+" "+1.0);
		ansroute.push(0.0+" "+0.0);
		assertEquals(ansroute,route);
		
		ArrayList<String> vis = map.getVisualisation();
		ArrayList<String> ansvis = new ArrayList<>();
		ansvis.add(0.0+" "+0.0);
		ansvis.add(1.0+" "+1.0);
		ansvis.add(2.0+" "+2.0);
		ansvis.add(3.0+" "+3.0);
		ansvis.add(4.0+" "+4.0);
		ansvis.add(5.0+" "+5.0);
		ansvis.add(6.0+" "+6.0);
		assertEquals(ansvis,vis);
		
		double dist = map.getRouteDist();
		double ansdist = 1034140.1627575245;
		assertEquals(ansdist,dist);
	}
	
	@Test
	void testAstar()
	{
		Stack<String> route = map.astar("0 0", "6 6");
		Stack<String> ansroute = new Stack<>();
		ansroute.push(6.0+" "+6.0);
		ansroute.push(5.0+" "+5.0);
		ansroute.push(4.0+" "+4.0);
		ansroute.push(3.0+" "+3.0);
		ansroute.push(1.0+" "+1.0);
		ansroute.push(0.0+" "+0.0);
		assertEquals(ansroute,route);
		
		ArrayList<String> vis = map.getVisualisation();
		ArrayList<String> ansvis = new ArrayList<>();
		ansvis.add(0.0+" "+0.0);
		ansvis.add(1.0+" "+1.0);
		ansvis.add(3.0+" "+3.0);
		ansvis.add(4.0+" "+4.0);
		ansvis.add(5.0+" "+5.0);
		ansvis.add(6.0+" "+6.0);
		assertEquals(ansvis,vis);
		
		double dist = map.getRouteDist();
		double ansdist = 1034140.1627575245;
		assertEquals(ansdist,dist);
	}
	
	@Test
	void testSearchHit()
	{
		ArrayList<String> ans = new ArrayList<>();
		ans.add("monj");
		ans.add("monk");
		ans.add("monji");
		ans.add("monjita");
		
		ArrayList<String> list = a.complete("mon");
		assertEquals(ans,list);
				
	}
	
	@Test
	void testSearchMiss()
	{
		ArrayList<String> list = a.complete("bo");
		assertEquals(true,list.isEmpty());
	}
	
	@Test
	void testSearchHitCase()
	{
		ArrayList<String> ans = new ArrayList<>();
		ans.add("monji");
		ans.add("monjita");
		
		ArrayList<String> list = a.complete("MoNji");
		assertEquals(ans,list);
	}
	
}
