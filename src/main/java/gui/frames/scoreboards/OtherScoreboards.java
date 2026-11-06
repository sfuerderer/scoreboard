/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.scoreboards;

import gui.frames.frameFunctions.FramesFunction;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import system.Defaults;
import system.settings.Frame;
import system.settings.Settings;

public class OtherScoreboards extends Stage{
	private Scene sScene;
	private Settings settings;
	private StackPane rootPane;
	private ScaledPane windowPane;

	public OtherScoreboards(Frame frame, Settings settings, FramesFunction displayFunction) {
		this.settings = settings;

		initStyle(StageStyle.UNDECORATED);
		setAlwaysOnTop(true);

		setX(frame.iStartXPosition);
		setY(frame.iStartYPosition);

		rootPane = new StackPane(displayFunction.getSceneToDisplay());
		windowPane = new ScaledPane(rootPane);
		applyScaling(frame);

		sScene = new Scene(windowPane, frame.iWidth, frame.iHeight, Color.BLACK);
		sScene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());

		setScene(sScene);
	}

	public void updatePosition(Frame frame) {
		setX(frame.iStartXPosition);
		setY(frame.iStartYPosition);
		setWidth(frame.iWidth);
		setHeight(frame.iHeight);
		applyScaling(frame);
	}

	// Same as the main scoreboard, but with the resolution of the small displays.
	private void applyScaling(Frame frame) {
		windowPane.setLayoutSize(settings.scalingMode.getLayoutWidth(frame, Defaults.OTHER_SCOREBOARD_DESIGN_WIDTH),
				settings.scalingMode.getLayoutHeight(frame, Defaults.OTHER_SCOREBOARD_DESIGN_HEIGHT));
	}

	public void changeDisplayFunction(FramesFunction displayFunction) {
		rootPane.getChildren().setAll(displayFunction.getSceneToDisplay());
	}
}
