/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import football.match.Match;
import gui.frames.ControlFrame;
import gui.frames.panes.components.FixedDigitsLabel;
import gui.frames.panes.components.ScoreboardText;
import gui.frames.panes.components.TeamColumn;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import system.Main;

/**
 * Main view of the scoreboard: logos and result in the middle, clock at the bottom.
 */
public class PaneMain extends PaneMainScoreboard {
	private static final double CLOCK_BAR_HEIGHT = 64;
	// Large result that fits above the clock bar in 280 pixels
	private static final int RESULT_SIZE = 140;

	private final Match match;
	private final FixedDigitsLabel lbGoalsHome;
	private final FixedDigitsLabel lbGoalsGuest;

	// Kept as a field: the match lives longer than this pane, so it only holds a weak reference
	private final ChangeListener<String> resultListener = (observable, oldValue, newValue) -> showResult();

	public PaneMain(Pane rootPane, ControlFrame controlFrame) {
		super(ScoreboardBackground.crestDiagonal(), rootPane);
		match = Main.getMatch();

		// Middle: teams and result
		lbGoalsHome = new FixedDigitsLabel("0", Main.getMainFont(FontWeight.BOLD, RESULT_SIZE), ScoreboardColors.TEXT);
		lbGoalsGuest = new FixedDigitsLabel("0", Main.getMainFont(FontWeight.BOLD, RESULT_SIZE), ScoreboardColors.TEXT);
		Label lbColon = ScoreboardText.label(":", FontWeight.MEDIUM, 100, ScoreboardColors.TEXT_DIM);
		lbColon.setTranslateY(-8);
		HBox result = new HBox(6, lbGoalsHome, lbColon, lbGoalsGuest);
		result.setAlignment(Pos.CENTER);
		HBox.setHgrow(result, Priority.ALWAYS);

		HBox middle = new HBox(TeamColumn.create(match.gettTeamHome()), result, TeamColumn.create(match.gettTeamGuest()));
		middle.setAlignment(Pos.CENTER);
		VBox.setVgrow(middle, Priority.ALWAYS);

		// Bottom: clock
		FixedDigitsLabel lbClock = new FixedDigitsLabel("00:00", Main.getMainFont(FontWeight.BOLD, 50),
				ScoreboardColors.TEXT);
		StackPane clockBar = new StackPane(lbClock);
		clockBar.setBackground(new Background(new BackgroundFill(ScoreboardColors.PANEL, null, null)));
		clockBar.setMinHeight(CLOCK_BAR_HEIGHT);
		clockBar.setPrefHeight(CLOCK_BAR_HEIGHT);

		VBox layout = new VBox(middle, clockBar);
		layout.prefWidthProperty().bind(widthProperty());
		layout.prefHeightProperty().bind(heightProperty());
		getChildren().add(layout);

		showResult();
		match.sResult.addListener(new WeakChangeListener<String>(resultListener));
		lbClock.textProperty().bind(Main.getServicePlaytime().messageProperty());
	}

	private void showResult() {
		lbGoalsHome.setText(String.valueOf(match.getiGoalsHome()));
		lbGoalsGuest.setText(String.valueOf(match.getiGoalsGuest()));
	}
}
