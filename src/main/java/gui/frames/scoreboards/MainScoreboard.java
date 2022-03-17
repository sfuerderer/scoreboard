/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.scoreboards;

import football.match.Match;
import gui.animation.IAnimation;
import gui.frames.panes.PaneAbout;
import gui.frames.panes.PaneMainScoreboard;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import system.settings.Frame;
import system.settings.Settings;

public class MainScoreboard extends Stage{
	private Scene mainScene;
	private Settings settings;
	private StackPane rootPane;
	private PaneMainScoreboard currentPane;
	
	public MainScoreboard(Frame mainFrame, Settings settings, Match match) {
		this.settings = settings;
		
		rootPane = new StackPane();
		initStyle(StageStyle.UNDECORATED);
		setAlwaysOnTop(true);
		setX(mainFrame.iStartXPosition);
		setY(mainFrame.iStartYPosition);
		
		currentPane = new PaneAbout(rootPane);
		
		currentPane.paneWillAppear();
		rootPane.getChildren().add(currentPane);
		
		mainScene = new Scene(rootPane, mainFrame.iWidth, mainFrame.iHeight);
		setScene(mainScene);
	}

	public void updatePosition(Frame mainFrame) {
		setX(mainFrame.iStartXPosition);
		setY(mainFrame.iStartYPosition);
		setWidth(mainFrame.iWidth);
		setHeight(mainFrame.iHeight);
		rootPane.setPrefWidth(mainFrame.iWidth);
		rootPane.setPrefHeight(mainFrame.iHeight);
	}
	
	public void changePane(PaneMainScoreboard newPane) {
		currentPane.paneWillDisappear();
		newPane.paneWillAppear();
		IAnimation animation = settings.animation.getAnimation();
		animation.doAnimationIn(newPane, currentPane, rootPane);
		currentPane = newPane;
	}
	
	public Pane getRootPane() {
		return rootPane;
	}
}
