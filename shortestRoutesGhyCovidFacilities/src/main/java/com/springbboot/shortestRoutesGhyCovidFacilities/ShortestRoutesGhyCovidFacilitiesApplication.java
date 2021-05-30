package com.springbboot.shortestRoutesGhyCovidFacilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*This class is a Java-FX application class and a Spring Context*/

@SpringBootApplication
public class ShortestRoutesGhyCovidFacilitiesApplication extends Application implements CommandLineRunner {
	
	//Integrating spring boot with javaFX
	
	private ApplicationContext context; //class that extends ApplicationContext
	private Parent root;
	
	@Autowired
    MyRepo rep;
	
	public static void main(String[] args) {
			
			Application.launch(args);
		}
	
	public void init() throws IOException {	
		
		context = SpringApplication.run(ShortestRoutesGhyCovidFacilitiesApplication.class);
		
	}

	public void start(Stage primaryStage) throws Exception {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFiles/Welcome.fxml"));
		loader.setControllerFactory(context::getBean); //spring context will create the controller beans
		root = loader.load();

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show(); 
		
	}
	
    public void run(String... args) throws Exception {
	
	BufferedReader br = new BufferedReader(new FileReader(new File("src/main/resources/dataFiles/covidFacilities.txt")));
	String line = br.readLine();
		
	while(line !=null)
	  {
		CovidFacility cf = new CovidFacility();
		
		String[] arr = line.split("\"");
		cf.setType(arr[0]);
		cf.setLatitude(Double.parseDouble(arr[1]));
		cf.setLongitude(Double.parseDouble(arr[2]));
		cf.setName(arr[3]);
		cf.setAddress(arr[4]);
		
		rep.save(cf);
		line = br.readLine();
	  }
	
    }
	
}
