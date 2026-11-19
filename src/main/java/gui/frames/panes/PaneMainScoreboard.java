/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import advertising.AdPlacement;
import advertising.Advertising;
import advertising.Sponsor;
import gui.GUIControlling;
import gui.frames.panes.components.PresentedBy;
import gui.frames.panes.winter.SnowflakeAnimation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import system.settings.Settings;

public abstract class PaneMainScoreboard extends Pane {
	// Height of the score bar at the bottom of the main scoreboard (layout pixels)
	public static final double SCORE_BAR_HEIGHT = 30;

	protected final int I_PADDING = 20;
	private static final double PRESENTED_BY_MARGIN = 8;
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

	/**
	 * Shows "presented by" with the logo of the sponsor of the place in the bottom right corner (above the
	 * score bar). Without a sponsor nothing is added, so the view stays exactly as it was. Call it at the end of
	 * the constructor, so the badge lies on top.
	 *
	 * @param dBottom distance from the bottom edge, e.g. above a bar of the view
	 * @return the badge or null without a sponsor
	 */
	protected Node showPresentedBy(AdPlacement placement, double dBottom) {
		Sponsor sponsor = Advertising.getAdvertising().getPresentedBy(placement);
		if (sponsor == null)
			return null;
		HBox badge = PresentedBy.create(sponsor);
		StackPane overlay = new StackPane(badge);
		overlay.prefWidthProperty().bind(widthProperty());
		overlay.prefHeightProperty().bind(heightProperty());
		// Clicks and the layout of the view are not affected by the overlay
		overlay.setPickOnBounds(false);
		overlay.setMouseTransparent(true);
		StackPane.setAlignment(badge, Pos.BOTTOM_RIGHT);
		StackPane.setMargin(badge, new Insets(0, PRESENTED_BY_MARGIN,
				dBottom + (showsScoreBar() ? SCORE_BAR_HEIGHT : 0) + PRESENTED_BY_MARGIN, 0));
		getChildren().add(overlay);
		return badge;
	}

	/**
	 * Like {@link #showPresentedBy(AdPlacement, double)} directly above the bottom edge or the score bar.
	 */
	protected Node showPresentedBy(AdPlacement placement) {
		return showPresentedBy(placement, 0);
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
