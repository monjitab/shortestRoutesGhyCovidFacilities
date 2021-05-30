package com.springbboot.shortestRoutesGhyCovidFacilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/*Using a Graph data structure to build the map and run routing algorithms*/

@Service
public class MyGraph {
	
	private HashMap<Location, ArrayList<Road>> map; // Adjacency list for the directed-graph with intersections as nodes and roads as edges
	private HashMap<Long, Location> nodes;
	private ArrayList<String> visualisation;
	private double routeDist;
	
	private boolean setLoc;
	private boolean setSource;
	private boolean setDest;
	
	public MyGraph() //constructor
	{
		this.map = new HashMap<>();
		this.nodes = new HashMap<>();
		this.visualisation = new ArrayList<>();
		this.routeDist = 0.0;
	}
	
	public ArrayList<String> getVisualisation()
	{
		ArrayList<String> vis = new ArrayList<>();
		
		for(String st : visualisation)
			vis.add(st);
		
		return vis;	
	}
	
	public void setVisualisation( ArrayList<String> vis)
	{
		this.visualisation = vis;
	}
	
	public double getRouteDist()
	{
		return this.routeDist;
	}
	
	public void setRouteDist(double d)
	{
		this.routeDist = d;
	}
	

	private class Location  //inner class Location for representing the locations on the map
	{  		
		double latitude;
		double longitude;
		
		Location(double latitude, double longitude) //constructor
		{
		   this.latitude =  latitude;
		   this.longitude = longitude;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			long temp;
			temp = Double.doubleToLongBits(latitude);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(longitude);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Location other = (Location) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
				return false;
			if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
				return false;
			return true;
		}

		private MyGraph getEnclosingInstance() {
			return MyGraph.this;
		}
		
		public String toString()
		{
			return this.latitude+" "+this.longitude;
		}
				
	}
	
	private class Road  // inner class for representing the roads on the map
	{		
		Location source;
		Location target;
		double length;
		
		Road(Location source, Location target, double length)
		{
			this.source = source;
			this.target = target;
			this.length = length;
		
		}
		
	}
	
	private class GeoPoint implements Comparable<GeoPoint>
	{
		Location loc;
		double dist;
		
		GeoPoint(Location loc, double dist)
		{
			this.loc = loc;
			this.dist = dist;
		}
		
		public int compareTo(GeoPoint g)
		{
			return ((Double)this.dist).compareTo(g.dist);
		}
	}
	
	private interface Heuristic {
		
		double hfunc(Location curr, Location end);
	}
	
	
	private static double getDist (Location from, Location to)
	{
		double lat1 = from.latitude;
		double lon1 = from.longitude;
		
		double lat2 = to.latitude;
		double lon2 = to.longitude;
		
		int R = 6371000; // radius of the earth in m
    	double lat1rad = Math.toRadians(lat1);
    	double lat2rad = Math.toRadians(lat2);
    	double deltaLat = Math.toRadians(lat2-lat1);
    	double deltaLon = Math.toRadians(lon2-lon1);

    	double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
    	        Math.cos(lat1rad) * Math.cos(lat2rad) *
    	        Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
    	double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

    	double d = R * c;
    	return d;
	}
	
	public double findDist(double lat1, double lon1, double lat2, double lon2)
	{
		return getDist(new Location(lat1,lon1),new Location(lat2,lon2));
	}
	
	public void buildMap(String nodeFile, String edgeFile) throws IOException
	{
		buildNodes(nodeFile);
		buildEdges(edgeFile);
	}
	
	private void buildNodes(String filename) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String link = br.readLine();
		link = br.readLine();
			
		while(link !=null)
		{
			String[] arr = link.split(",");

			long id = Long.parseLong(arr[0]);
			double lang = Double.parseDouble(arr[1]);
			double lat = Double.parseDouble(arr[2]);
			
			Location loc = new Location(lat,lang);
			
			nodes.put(id,loc);
			
			link = br.readLine();
			
		}
	
	}
	
	private void buildEdges(String filename) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String link = br.readLine();
		link = br.readLine();
			
		while(link !=null)
		{
			String[] arr = link.split(",");

			long from = Long.parseLong(arr[1]);
			long to = Long.parseLong(arr[2]);
		    double length = Double.parseDouble(arr[3]);
			
			int forw = Integer.parseInt(arr[5]);
			int back = Integer.parseInt(arr[6]);
			
			Location source = nodes.get(from);
			Location target = nodes.get(to);
			
			
			if(!map.containsKey(source))
				map.put(source, new ArrayList<>());
			
			if(!map.containsKey(target))
				map.put(target, new ArrayList<>());
			
			if(forw>0)
			 { 
				Road r = new Road(source,target,length);
				map.get(source).add(r);
			 }
			
			if(back>0)
			 { 
				Road r = new Road(target,source,length);
				map.get(target).add(r);
			 }
					
			link = br.readLine();
			
		}
	
	}
	
	private void addLocation(Location loc)
	{
		if(map.containsKey(loc))
			return;
		
		double minDist = Double.MAX_VALUE;
		Location nearest = null;
		
		for(Location intersec : map.keySet())
		{
			double dist = getDist(loc,intersec);
			if(dist<minDist)
			{
				minDist = dist;
				nearest = intersec;
			}
		}
				
		map.put(loc, new ArrayList<>());		
		map.get(loc).add(new Road(loc,nearest,minDist));		
		map.get(nearest).add(new Road(nearest,loc,minDist));
		setLoc = true;
		
	}
	
	private void remLocation(Location loc)
	{
		if(!map.containsKey(loc))
			return;
		
		Location next = map.get(loc).get(0).target;
		map.remove(loc);
		
		for(Road r : map.get(next))
		{
			if(r.target.equals(loc))
			  { map.get(next).remove(r); break; }		
		}
		
	}
	
	private Stack<String> shortestPath (Location start, Location end, Heuristic h) //shortest path algo with heuristic function
	{
		PriorityQueue<GeoPoint> pq = new PriorityQueue<>();
		HashSet<Location> marked = new HashSet<>();
		HashMap<Location, Double> distfromstart = new HashMap<>();
		HashMap<Location, Double> dist = new HashMap<>();
		HashMap<Location, Location> edgeTo = new HashMap<>();
		
		for(Location loc : map.keySet())
		   { 
			 distfromstart.put(loc,Double.MAX_VALUE);
		     dist.put(loc,Double.MAX_VALUE);
		   }
		
		distfromstart.put(start,0.0);
		dist.put(start,0.0 + h.hfunc(start,end));
		
		pq.add(new GeoPoint(start, dist.get(start)));		
		
		while(!pq.isEmpty())
		{
			if((pq.peek().loc).equals(end))
				break;
			
			GeoPoint g = pq.poll();
			
			marked.add(g.loc);
			visualisation.add(g.loc.toString());
			
			for(Road r : map.get(g.loc))
		    {
				   Location next = r.target;
				   
				   if(!marked.contains(next))
				   { 
					 if( distfromstart.get(g.loc) + r.length + h.hfunc(next, end) < dist.get(next) )
				     {
					   dist.put(next, distfromstart.get(g.loc) + r.length + h.hfunc(next, end) );
					   distfromstart.put(next, distfromstart.get(g.loc) + r.length );
					   edgeTo.put(next,g.loc);
					   pq.add(new GeoPoint(next, dist.get(next)));
				     }
				   }				
		    }  			
		}
		
		if(pq.isEmpty())
			return new Stack<>();
		
		Stack<String> route = new Stack<>();
		Location curr = end;
		
		while(curr!=start)
		{
			route.push(curr.latitude+" "+curr.longitude);
			curr = edgeTo.get(curr);
		}
		
		route.push(start.latitude+" "+start.longitude);
		visualisation.add(end.toString());
		routeDist = distfromstart.get(end);
		
		return route;
		
	}
	
	private Location getLoc(String loc)
	{
		String[] s  = loc.split(" ");
		Location location = new Location(Double.parseDouble(s[0]),Double.parseDouble(s[1]));
		return location;
	}
	
	private void addSourceDest (Location source, Location destination)
	{
		setSource = false;
		setDest = false;
		
		setLoc = false;
		addLocation(source);
		
		if(setLoc==true)
			setSource = true;
		
		setLoc = false;
		addLocation(destination);
		
		if(setLoc==true)
			setDest = true;
	}
	
	private void remSourceDest(Location source, Location destination)
	{
		if(setSource)
		  remLocation(source);
			
		if(setDest)
		  remLocation(destination);
	}
	
	@RouteFinder //remove annotation before testing
	public Stack<String> dijkstra (String source, String destination) {
		
		Location start = getLoc(source);
		Location end = getLoc(destination);
		
		addSourceDest(start,end);
				
		visualisation = new ArrayList<>();
		Stack<String> route = shortestPath(start,end,(p,q)-> 0); //heuristic function : f(curr,end) = 0
		
		remSourceDest(start,end);
			
		return route;
	}
	
	@RouteFinder //remove annotation before testing
	public Stack<String> astar (String source, String destination) {
		
		Location start = getLoc(source);
		Location end = getLoc(destination);
		
		addSourceDest(start,end);
		
		visualisation = new ArrayList<>();
		Stack<String> route = shortestPath(start,end,(p,q)-> getDist(p,q)); //heuristic function : f(curr,end) = straightline distance
		
		remSourceDest(start,end);
		
		return route;
	}
		
	
	public static void main(String args[]) throws IOException
	{

	}
}

