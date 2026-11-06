/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import gui.GUIControlling;
import gui.frames.panes.winter.SnowflakeAnimation;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import system.settings.Settings;

public abstract class PaneMainScoreboard extends Pane {
	// Height of the score bar at the bottom of the main scoreboard (layout pixels)
	public static final double SCORE_BAR_HEIGHT = 30;

	protected final int I_PADDING = 20;
	protected boolean paneSupportSnowing;
	private SnowflakeAnimation snow;

	/**
	 * Whether the main scoreboard shows the score bar at the bottom while this pane is visible. Panes that
	 * return true keep the bottom {@link #SCORE_BAR_HEIGHT} pixels free.
	 */
	public boolean showsScoreBar() {
		return false;
	}

	public PaneMainScoreboard(Pane rootPane) {
		this(rootPane,true);
	}
	
	public PaneMainScoreboard(Pane rootPane, boolean paneSupportSnowing) {
		this.paneSupportSnowing = paneSupportSnowing;
		prefWidthProperty().bind(rootPane.widthProperty());
		prefHeightProperty().bind(rootPane.heightProperty());
	}

	public PaneMainScoreboard(Background background, Pane rootPane) {
		this(background, rootPane, true);
	}

	public PaneMainScoreboard(Background background, Pane rootPane, boolean paneSupportSnowing) {
		this(rootPane, paneSupportSnowing);
		setBackground(background);
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
