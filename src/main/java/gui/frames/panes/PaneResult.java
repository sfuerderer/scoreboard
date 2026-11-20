/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import advertising.AdPlacement;
import football.match.Match;
import football.match.MatchEvent;
import football.match.Teams;
import gui.frames.panes.components.FixedDigitsLabel;
import gui.frames.panes.components.ScoreboardText;
import gui.frames.panes.components.TeamCrest;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import language.Language;
import system.Main;

/**
 * Result with the scorers of both teams, e.g. at half-time: "HALBZEIT", logos and result, below each team
 * its scorers with minutes.
 */
public class PaneResult extends PaneMainScoreboard {
	private static final double SCORERS_WIDTH = 236;

	public PaneResult(Pane rootPane) {
		super(ScoreboardBackground.crestDiagonal(), rootPane);
		Match match = Main.getMatch();

		Label lbTitle = ScoreboardText.label(Language.getText(match.getResultTitleKey()), FontWeight.EXTRA_BOLD, 56,
				ScoreboardColors.TEXT);

		FixedDigitsLabel lbResult = new FixedDigitsLabel(match.getiGoalsHome() + ":" + match.getiGoalsGuest(),
				Main.getMainFont(FontWeight.EXTRA_BOLD, 84), ScoreboardColors.TEXT);
		HBox result = new HBox(26, TeamCrest.of(match.gettTeamHome(), 76), lbResult,
				TeamCrest.of(match.gettTeamGuest(), 76));
		result.setAlignment(Pos.CENTER);

		Label lbScorersHome = scorers(match, Teams.HOME);
		Label lbScorersGuest = scorers(match, Teams.GUEST);
		Region rSpacer = new Region();
		HBox.setHgrow(rSpacer, Priority.ALWAYS);
		HBox scorers = new HBox(lbScorersHome, rSpacer, lbScorersGuest);
		scorers.setPadding(new Insets(0, 16, 0, 16));
		scorers.setAlignment(Pos.TOP_CENTER);

		VBox layout = new VBox(4, lbTitle, result, scorers);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(10, 0, 18, 0));
		layout.prefWidthProperty().bind(widthProperty());
		layout.prefHeightProperty().bind(heightProperty());
		getChildren().add(layout);
		showPresentedBy(AdPlacement.HALFTIME);
	}

	private static Label scorers(Match match, Teams team) {
		Label lbScorers = ScoreboardText.fittingLabel(MatchEvent.formatScorers(match.getGoals(team)),
				FontWeight.SEMI_BOLD, 19, 13, SCORERS_WIDTH, ScoreboardColors.TEXT_DIM);
		lbScorers.setWrapText(true);
		lbScorers.setMaxWidth(SCORERS_WIDTH);
		lbScorers.setAlignment(team == Teams.HOME ? Pos.TOP_LEFT : Pos.TOP_RIGHT);
		lbScorers.setTextAlignment(team == Teams.HOME ? javafx.scene.text.TextAlignment.LEFT
				: javafx.scene.text.TextAlignment.RIGHT);
		return lbScorers;
	}
}
