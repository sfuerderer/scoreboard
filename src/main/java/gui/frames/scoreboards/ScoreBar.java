/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.scoreboards;

import background.services.ServicePlaytime;
import football.Team;
import football.match.Match;
import gui.frames.panes.PaneMainScoreboard;
import gui.frames.panes.ScoreboardColors;
import gui.frames.panes.components.FixedDigitsLabel;
import gui.frames.panes.components.ScoreboardText;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import system.Main;

/**
 * Narrow bar with result and clock at the bottom of the main scoreboard. It is a layer of its own above
 * the panes, so it stays in place while the panes change (advertising, pictures).
 */
class ScoreBar extends HBox {
	private Match match;
	private FixedDigitsLabel lbResult;
	// Kept as a field: the match lives longer than the bar shows it, so it only holds a weak reference
	private final ChangeListener<String> resultListener = (observable, oldValue, newValue) -> showResult();

	ScoreBar() {
		setAlignment(Pos.CENTER);
		setSpacing(10);
		setBackground(new Background(new BackgroundFill(ScoreboardColors.PANEL, null, null)));
		setMinHeight(PaneMainScoreboard.SCORE_BAR_HEIGHT);
		setPrefHeight(PaneMainScoreboard.SCORE_BAR_HEIGHT);
		setMaxHeight(PaneMainScoreboard.SCORE_BAR_HEIGHT);
	}

	/**
	 * Shows the given match. Called each time the bar becomes visible, so a new match is picked up.
	 */
	void showMatch(Match newMatch, ServicePlaytime playtime) {
		if (newMatch == match)
			return;
		match = newMatch;

		lbResult = new FixedDigitsLabel("0:0", Main.getMainFont(FontWeight.BOLD, 22), Color.BLACK);
		lbResult.setPadding(new Insets(0, 9, 0, 9));
		lbResult.setBackground(new Background(new BackgroundFill(ScoreboardColors.TEXT, null, null)));
		FixedDigitsLabel lbClock = new FixedDigitsLabel("00:00", Main.getMainFont(FontWeight.BOLD, 21),
				ScoreboardColors.TEXT_DIM);
		lbClock.textProperty().bind(playtime.messageProperty());
		HBox.setMargin(lbClock, new Insets(0, 0, 0, 10));

		getChildren().setAll(colorChip(match.gettTeamHome()), teamName(match.gettTeamHome()), lbResult,
				teamName(match.gettTeamGuest()), colorChip(match.gettTeamGuest()), lbClock);

		showResult();
		match.sResult.addListener(new WeakChangeListener<String>(resultListener));
	}

	private void showResult() {
		lbResult.setText(match.getiGoalsHome() + ":" + match.getiGoalsGuest());
	}

	private static Region teamName(Team team) {
		return ScoreboardText.fittingLabel(team.getDisplayName(), FontWeight.BOLD, 21, 15, 150, ScoreboardColors.TEXT);
	}

	private static Region colorChip(Team team) {
		Region rChip = new Region();
		rChip.setMinSize(6, 16);
		rChip.setMaxSize(6, 16);
		rChip.setBackground(new Background(new BackgroundFill(Color.web(team.getDisplayColor()), null, null)));
		return rChip;
	}
}
