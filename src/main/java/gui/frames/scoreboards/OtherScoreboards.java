/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.scoreboards;

import gui.frames.frameFunctions.FramesFunction;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import system.settings.Frame;

public class OtherScoreboards extends Stage{
	private Scene sScene;
	public OtherScoreboards(Frame frame, FramesFunction displayFunction) {
		initStyle(StageStyle.UNDECORATED);
		setAlwaysOnTop(true);
		
		setX(frame.iStartXPosition);
		setY(frame.iStartYPosition);
		
		sScene = new Scene(displayFunction.getSceneToDisplay(), frame.iWidth, frame.iHeight);
		sScene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
		
		setScene(sScene);
	}
	
	public void updatePosition(Frame frame) {
		setX(frame.iStartXPosition);
		setY(frame.iStartYPosition);
		setWidth(frame.iWidth);
		setHeight(frame.iHeight);
	}

	public void changeDisplayFunction(FramesFunction displayFunction) {
		sScene.setRoot(displayFunction.getSceneToDisplay());
	}
}
