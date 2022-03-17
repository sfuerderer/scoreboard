/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.frameFunctions;

import java.text.SimpleDateFormat;
import java.util.Date;

import background.services.ServiceCurrentTime;
import javafx.concurrent.Worker.State;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class FramesFunctionTime implements FramesFunction{
	private Text tClock;
	private ServiceCurrentTime sTime;
	
	public FramesFunctionTime() {
		sTime = new ServiceCurrentTime();
	}
	
	@Override
	public Pane getSceneToDisplay() {
		HBox displayPane = new HBox();
		displayPane.setPadding(new Insets(2));
		displayPane.setAlignment(Pos.CENTER);
		
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");  
	    Date date = new Date();
	    
	    tClock = new Text(formatter.format(date));
	    tClock.getStyleClass().add("sf-text-otherFrames");
	    
	    tClock.textProperty().bind(sTime.messageProperty());
	    
	    displayPane.getChildren().addAll(tClock);
	    
	    displayPane.setStyle("-fx-background-color: #000000;");
	    
	    if (sTime.getState() == State.READY) {
	    	sTime.start();
        }
	    
		return displayPane;
	}

}
