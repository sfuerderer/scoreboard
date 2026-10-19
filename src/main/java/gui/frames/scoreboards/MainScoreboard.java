/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.scoreboards;

import football.match.Match;
import gui.animation.IAnimation;
import gui.frames.panes.PaneAbout;
import gui.frames.panes.PaneMainScoreboard;
import javafx.animation.Animation;
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
	private Animation runningAnimation;
	private Runnable finishAnimation;

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
		// A new pane can be shown while the last transition is still running (e.g. goal video and
		// scorer), so the running one is finished first. Otherwise both would remove panes at the end.
		finishRunningAnimation();

		PaneMainScoreboard oldPane = currentPane;
		IAnimation animation = settings.animation.getAnimation();
		oldPane.paneWillDisappear();
		newPane.paneWillAppear();
		rootPane.getChildren().add(newPane);

		runningAnimation = animation.createAnimation(newPane, oldPane, rootPane);
		finishAnimation = () -> {
			rootPane.getChildren().remove(oldPane);
			animation.cleanUp(newPane, oldPane, rootPane);
			oldPane.paneDidDisappear();
			newPane.paneDidAppear();
		};
		runningAnimation.setOnFinished(evt -> finishRunningAnimation());
		currentPane = newPane;
		runningAnimation.play();
	}

	private void finishRunningAnimation() {
		if (runningAnimation == null) {
			return;
		}
		Runnable finish = finishAnimation;
		runningAnimation.stop();
		runningAnimation = null;
		finishAnimation = null;
		finish.run();
	}
	
	public Pane getRootPane() {
		return rootPane;
	}
}
