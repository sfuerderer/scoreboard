/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import gui.GUIControlling;
import gui.frames.panes.winter.SnowflakeAnimation;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import system.settings.Settings;

public abstract class PaneMainScoreboard extends Pane {
	protected final int I_PADDING = 20;
	protected boolean paneSupportSnowing;
	private SnowflakeAnimation snow;

	public PaneMainScoreboard(Pane rootPane) {
		this(rootPane,true);
	}
	
	public PaneMainScoreboard(Pane rootPane, boolean paneSupportSnowing) {
		this.paneSupportSnowing = paneSupportSnowing;
		prefWidthProperty().bind(rootPane.widthProperty());
		prefHeightProperty().bind(rootPane.heightProperty());
	}

	public PaneMainScoreboard(Image backgroundImage, Pane rootPane) {
		this(backgroundImage, rootPane,true);
	}
	
	public PaneMainScoreboard(Image backgroundImage, Pane rootPane, boolean paneSupportSnowing) {
		this(rootPane,paneSupportSnowing);
		BackgroundImage myBI = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		setBackground(new Background(myBI));
	}

	public void paneWillAppear() {
		Settings settings = GUIControlling.getSettings();
		if (paneSupportSnowing && settings.bSnowing) {
			snow = new SnowflakeAnimation(this, settings);
			snow.animate();
		}
	}

	public void paneDidAppear() {

	}

	public void paneWillDisappear() {

	}

	public void paneDidDisappear() {
		// Snowflake animations run endlessly, so they have to be stopped explicitly
		if (snow != null) {
			snow.stop();
			snow = null;
		}
	}
}
