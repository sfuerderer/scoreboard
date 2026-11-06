/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import football.match.Match;
import gui.frames.ControlFrame;
import gui.frames.panes.components.ClockProgress;
import gui.frames.panes.components.FixedDigitsLabel;
import gui.frames.panes.components.ScoreboardText;
import gui.frames.panes.components.TeamColumn;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import language.Language;
import system.Main;

/**
 * Main view of the scoreboard: competition and halftime at the top, logos, names and result in the
 * middle, clock with the progress of the halftime at the bottom.
 */
public class PaneMain extends PaneMainScoreboard {
	private static final double TOP_BAR_HEIGHT = 30;
	private static final double CLOCK_BAR_HEIGHT = 64;
	private static final double PROGRESS_HEIGHT = 4;
	// Largest result that still fits between the top bar and the clock bar in 280 pixels
	private static final int RESULT_SIZE = 140;

	private final Match match;
	private final FixedDigitsLabel lbGoalsHome;
	private final FixedDigitsLabel lbGoalsGuest;
	private final Label lbHalftime;
	private final Region rProgress;
	private final StackPane clockBar;
	private double dProgress;

	// Kept as fields: the match and the playtime live longer than this pane, so they only hold weak references
	private final ChangeListener<String> resultListener = (observable, oldValue, newValue) -> showResult();
	private final ChangeListener<String> clockListener = (observable, oldValue, newValue) -> showClock(newValue);

	public PaneMain(Pane rootPane, ControlFrame controlFrame) {
		super(ScoreboardBackground.crestDiagonal(), rootPane);
		match = Main.getMatch();

		// Top: competition and halftime
		Label lbCompetition = ScoreboardText.label(match.getsCompetition(), FontWeight.SEMI_BOLD, 15,
				ScoreboardColors.TEXT_DIM);
		lbHalftime = ScoreboardText.label("", FontWeight.BOLD, 15, ScoreboardColors.TEXT);
		lbHalftime.setPadding(new Insets(1, 9, 0, 9));
		lbHalftime.setBackground(new Background(new BackgroundFill(ScoreboardColors.ACCENT, null, null)));
		Region rTopSpacer = new Region();
		HBox.setHgrow(rTopSpacer, Priority.ALWAYS);
		HBox topBar = new HBox(lbCompetition, rTopSpacer, lbHalftime);
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setPadding(new Insets(0, 16, 0, 16));
		topBar.setMinHeight(TOP_BAR_HEIGHT);
		topBar.setPrefHeight(TOP_BAR_HEIGHT);

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

		// Bottom: clock and progress of the halftime
		FixedDigitsLabel lbClock = new FixedDigitsLabel("00:00", Main.getMainFont(FontWeight.BOLD, 50),
				ScoreboardColors.TEXT);
		Region rProgressTrack = new Region();
		rProgressTrack.setBackground(new Background(new BackgroundFill(ScoreboardColors.PROGRESS_TRACK, null, null)));
		rProgressTrack.setMinHeight(PROGRESS_HEIGHT);
		rProgressTrack.setMaxHeight(PROGRESS_HEIGHT);
		rProgress = new Region();
		rProgress.setBackground(new Background(new BackgroundFill(ScoreboardColors.ACCENT, null, null)));
		rProgress.setMinHeight(PROGRESS_HEIGHT);
		rProgress.setMaxHeight(PROGRESS_HEIGHT);
		rProgress.setMaxWidth(0);
		clockBar = new StackPane(lbClock, rProgressTrack, rProgress);
		StackPane.setAlignment(rProgressTrack, Pos.BOTTOM_LEFT);
		StackPane.setAlignment(rProgress, Pos.BOTTOM_LEFT);
		clockBar.setBackground(new Background(new BackgroundFill(ScoreboardColors.PANEL, null, null)));
		clockBar.setMinHeight(CLOCK_BAR_HEIGHT);
		clockBar.setPrefHeight(CLOCK_BAR_HEIGHT);
		clockBar.widthProperty().addListener((observable, oldWidth, newWidth) -> showProgress());

		VBox layout = new VBox(topBar, middle, clockBar);
		layout.prefWidthProperty().bind(widthProperty());
		layout.prefHeightProperty().bind(heightProperty());
		getChildren().add(layout);

		showResult();
		match.sResult.addListener(new WeakChangeListener<String>(resultListener));
		lbClock.textProperty().bind(Main.getServicePlaytime().messageProperty());
		Main.getServicePlaytime().messageProperty().addListener(new WeakChangeListener<String>(clockListener));
		showClock(Main.getServicePlaytime().getMessage());
	}

	private void showResult() {
		lbGoalsHome.setText(String.valueOf(match.getiGoalsHome()));
		lbGoalsGuest.setText(String.valueOf(match.getiGoalsGuest()));
	}

	private void showClock(String sClock) {
		lbHalftime.setText(ScoreboardText.upper(Language.getText(match.geteWhichHalftime().getsLanguageKey())));
		dProgress = ClockProgress.of(sClock, match.getTimePassedToHalftime(), match.getiHalftimeLength());
		showProgress();
	}

	private void showProgress() {
		rProgress.setMaxWidth(clockBar.getWidth() * dProgress);
	}
}
