package com.springbboot.shortestRoutesGhyCovidFacilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/*Controller for App.fxml scene*/

@Controller
public class ControllerApp implements Initializable {
	
	private String nodeFile = "src/main/resources/dataFiles/nodes.csv";
	private String edgeFile = "src/main/resources/dataFiles/edges.csv";
	
	private String userLoc = ""; 
	private String destination = "";
	private HashMap<String, List<CovidFacility>> facilities;

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private MyGraph map;
	
	@Autowired
	private Autocomplete a;
	
	@Autowired
	private MyRepo rep;
	
	@FXML
	WebView view;
	
	@FXML
	WebEngine eng;
	
	@FXML
	RadioButton dch;
	
	@FXML
	RadioButton dchc;
	
	@FXML
	RadioButton cvh;
	
	@FXML
	RadioButton ctc;
	
	@FXML
	RadioButton ccc;
	
	@FXML
	RadioButton all;
	
	@FXML
	TextField search;
	
	@FXML
	Label message;
	
	@FXML
	ComboBox<String> cb;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		eng = view.getEngine();
		eng.load(getClass().getResource("/htmlFiles/ghymap.html").toString());
		
		try
		{ map.buildMap(nodeFile,edgeFile); }
		
		catch(Exception e)
		{ System.out.println("Exception while building graph"); }
				
		try
		{  a.buildTrie(getNames());  }
		
		catch(Exception e)
		{ System.out.println("Exception while building trie"); }
		
		facilities = new HashMap<>();
		
		cb.setPromptText("Search for Covid Facilities");
		cb.setEditable(true);
				
	}
	
	public String getUserLoc()
	{
		return this.userLoc;
	}
	
	public String getDestination()
	{
		return this.destination;
	}

	private List<String> getNames()
	{
		List<CovidFacility> list = rep.findAll();
		List<String> names = new ArrayList<>();
		for(CovidFacility cf : list)
			names.add(cf.getName());
		return names;
			
	}
	
	public void complete (KeyEvent ev) 
	{
		String word = cb.getEditor().getText();
		
		if(word.equals(""))
			cb.getItems().clear();
			
		else
		{
		ArrayList<String> list = a.complete(word);
		ArrayList<String> items = new ArrayList<>();
		
		for(int i = 0; i<Math.min(5, list.size()); i++)
			items.add(list.get(i));
		
		cb.getItems().clear();
		cb.getItems().addAll(items);
		}
	}
	
	public void clickcomplete(ActionEvent e)
	{
		String val = cb.getValue();
		List<CovidFacility> list = rep.findByName(val);
		
		for(CovidFacility cf : list)
		 {
	        resetDest();
			showFacility(cf,"ltblue"); 	
		 }
		
		setMessage("Click on the markers to set the destination and then click on the respective button to show the shortest route");
		
	}
	
	private void resetDest()
	{
		eng.executeScript("newroute()") ;
		eng.executeScript("rempoints()") ;		
		map.setVisualisation(new ArrayList<String>());
		map.setRouteDist(0.0);
		eng.executeScript("remfacilities()") ;
		eng.executeScript("newDest()") ;		
		eng.executeScript("zoomout()") ;
	}
	
	public void setMessage(String mess)
	{
		message.setText(mess);	
	}
	
	public void clickset (ActionEvent ev) throws IOException, InterruptedException
	{
		userLoc = (String)eng.executeScript("sendUserLoc()") ;
	}
	
	public void clickunset (ActionEvent ev) throws IOException, InterruptedException
	{
		eng.executeScript("remuser()") ;
		eng.executeScript("zoomout()") ;
	}
	
	public void clickshow (ActionEvent e) throws IOException, InterruptedException
	{
		resetDest();
		
		boolean f = false;
		
		if(all.isSelected())
		{ dch.setSelected(true);  dchc.setSelected(true); cvh.setSelected(true);  
		  ctc.setSelected(true);  ccc.setSelected(true); f = true; }  
		
		if(dch.isSelected())	
		    { loadFacility("dch","purple"); f = true; }
		
		if(dchc.isSelected())
			{ loadFacility("dchc","blue"); f = true; }
		
		if(cvh.isSelected())	
			{ loadFacility("cvh","pink"); f = true; }
		
		if(ctc.isSelected())	
			{ loadFacility("ctc","orange"); f = true; }
		
		if(ccc.isSelected())	
			{ loadFacility("ccc","ltblue"); f = true; }
		
		if(f==true)
		  setMessage("Click on the markers to set the destination and then click on the respective button to show the shortest route");
		
		if(f==false)
		  setMessage("Select required options to show facilities");
		
	}
	
	public void clickDijkstra (ActionEvent e) throws IOException, InterruptedException
	{	
		userLoc = (String)eng.executeScript("sendUserLoc()") ;
		destination = (String)eng.executeScript("sendDestination()") ;	
		
		Stack<String> route  = map.dijkstra(userLoc,destination);	    
		drawRoute(route);
		
	}
	
	public void clickAStar (ActionEvent e) throws IOException, InterruptedException
	{
		userLoc = (String)eng.executeScript("sendUserLoc()") ;
		destination = (String)eng.executeScript("sendDestination()") ;
		
		Stack<String> route  = map.astar(userLoc,destination);		
		drawRoute(route);
	}
	
	private void drawRoute (Stack<String> route)
	{
			eng.executeScript("newroute()");
			eng.executeScript("rempoints()") ;
			
			while(!route.isEmpty())
		 	{
				String s = route.pop();
				eng.executeScript("addroute('"+s+"')");
		 	}
			
			eng.executeScript("drawroute()");
		
	}
	
	public double getRouteDist()
	{
		return map.getRouteDist();
	}
	
	
	public void clickshowvis (ActionEvent e) throws IOException, InterruptedException
	{
		ArrayList<String> vis = map.getVisualisation();
		if(vis.size()>0)
		  showPoints(vis,"green");	
	}
	
	public void clickshowtraffic (ActionEvent e) throws IOException
	{
		eng.executeScript("showTraffic()") ;
	}
	
	public void clickhidetraffic (ActionEvent e) throws IOException
	{
		eng.executeScript("hideTraffic()") ;
	}

	private void loadFacility(String type, String colour) throws IOException, InterruptedException
	{
		
		facilities.put(type, rep.findByType(type));
	
		for(CovidFacility cf : facilities.get(type))
			showFacility(cf,colour);
	}
	
	private void showFacility(CovidFacility cf, String colour)
	{
		StringBuilder title = new StringBuilder(cf.getType().toUpperCase());
		title.append(" : ");
		title.append(cf.getName()+", ");
		title.append(cf.getAddress());
		
		String info = title.toString();		
		eng.executeScript("addfacility("+cf.getLatitude()+","+cf.getLongitude()+",'"+colour+"','"+info+"')");
	}
	
	private void showPoints (ArrayList<String> list, String color) throws IOException, InterruptedException
	{
		int n = list.size();
		int delay = 8000/n;
		
	    for(int i = 0; i<n; i++)
		  {
			  String location = list.get(i);
			  eng.executeScript("addpoint('"+location+"','"+color+"',"+(delay*i)+")");  
		  }
	}
	
	
	public void clickhideroute (ActionEvent e) throws IOException
	{
		eng.executeScript("newroute()") ;
		map.setVisualisation(new ArrayList<String>());
		map.setRouteDist(0.0);
	}
	
	public void clickhidevis (ActionEvent e) throws IOException
	{
		eng.executeScript("rempoints()") ;
	}
	
	public void clickreload (ActionEvent e) throws IOException
	{
		eng.reload() ;
		setMessage("Choose the required facility category and click the \"Show Covid Facilities Near Me\" button or search for a desired facility");
		dch.setSelected(false);  dchc.setSelected(false);cvh.setSelected(false); 
		ctc.setSelected(false);  ccc.setSelected(false); all.setSelected(false);
		resetDest();
	}
	
	public void clickinfo (ActionEvent e) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFiles/Info.fxml"));
		loader.setControllerFactory(context::getBean);
		Parent rootn = loader.load();
		Scene scenen = new Scene(rootn);
		Stage stagen = (Stage)((Node)e.getSource()).getScene().getWindow();
		stagen.setScene(scenen);
		stagen.show();
		
	}


}
