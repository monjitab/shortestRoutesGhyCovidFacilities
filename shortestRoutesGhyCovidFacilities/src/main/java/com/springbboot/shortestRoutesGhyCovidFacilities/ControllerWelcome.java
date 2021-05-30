package com.springbboot.shortestRoutesGhyCovidFacilities;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*Controller for Welcome.fxml scene*/

@Controller
public class ControllerWelcome {
	
	@Autowired
	private ApplicationContext context;
	
	public void clickset (ActionEvent e) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFiles/App.fxml"));
		loader.setControllerFactory(context::getBean);
		Parent rootn = loader.load();
		Scene scenen = new Scene(rootn);
		Stage stagen = (Stage)((Node)e.getSource()).getScene().getWindow();
		stagen.setScene(scenen);
		stagen.show();
		
	}

}
