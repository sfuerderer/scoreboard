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
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import system.Defaults;
import system.settings.Frame;
import system.settings.Settings;

public class MainScoreboard extends Stage{
	private Scene mainScene;
	private Settings settings;
	private StackPane rootPane;
	private Pane windowPane;
	private PaneMainScoreboard currentPane;
	private Animation runningAnimation;
	private Runnable finishAnimation;

	public MainScoreboard(Frame mainFrame, Settings settings, Match match) {
		this.settings = settings;
		
		// The panes are always laid out in the resolution of the LED wall and then stretched to the
		// window size, so the wall shows the same picture regardless of the configured window size.
		rootPane = new StackPane();
		rootPane.setMinSize(Defaults.SCOREBOARD_DESIGN_WIDTH, Defaults.SCOREBOARD_DESIGN_HEIGHT);
		rootPane.setPrefSize(Defaults.SCOREBOARD_DESIGN_WIDTH, Defaults.SCOREBOARD_DESIGN_HEIGHT);
		rootPane.setMaxSize(Defaults.SCOREBOARD_DESIGN_WIDTH, Defaults.SCOREBOARD_DESIGN_HEIGHT);

		windowPane = new Pane(rootPane);
		windowPane.setStyle("-fx-background-color: #000000;");
		Scale scale = new Scale();
		scale.xProperty().bind(windowPane.widthProperty().divide(Defaults.SCOREBOARD_DESIGN_WIDTH));
		scale.yProperty().bind(windowPane.heightProperty().divide(Defaults.SCOREBOARD_DESIGN_HEIGHT));
		rootPane.getTransforms().add(scale);

		initStyle(StageStyle.UNDECORATED);
		setAlwaysOnTop(true);
		setX(mainFrame.iStartXPosition);
		setY(mainFrame.iStartYPosition);
		
		currentPane = new PaneAbout(rootPane);
		
		currentPane.paneWillAppear();
		rootPane.getChildren().add(currentPane);
		
		mainScene = new Scene(windowPane, mainFrame.iWidth, mainFrame.iHeight, Color.BLACK);
		setScene(mainScene);
	}

	public void updatePosition(Frame mainFrame) {
		setX(mainFrame.iStartXPosition);
		setY(mainFrame.iStartYPosition);
		setWidth(mainFrame.iWidth);
		setHeight(mainFrame.iHeight);
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
