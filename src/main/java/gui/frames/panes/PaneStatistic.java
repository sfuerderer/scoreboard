/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import java.util.Locale;

import football.match.Match;
import football.match.Teams;
import gui.frames.panes.components.FixedDigitsLabel;
import gui.frames.panes.components.ScoreboardText;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import language.Language;
import system.Main;

/**
 * Statistic of both teams in one view: corners, yellow cards, red cards (only if there are any) and the
 * spectators. The bars show the values in proportion, in the colors of the teams.
 */
public class PaneStatistic extends PaneMainScoreboard {
	private static final double VALUE_WIDTH = 60;
	private static final double BAR_HEIGHT = 8;
	// Small enough for three rows (corners, yellow and red cards) plus the spectators in 280 pixels
	private static final int VALUE_SIZE = 38;
	private static final double VISITORS_HEIGHT = 44;

	public PaneStatistic(Pane rootPane) {
		super(ScoreboardBackground.crestDiagonal(), rootPane);
		Match match = Main.getMatch();
		Color colorHome = Color.web(match.gettTeamHome().getDisplayColor());
		Color colorGuest = Color.web(match.gettTeamGuest().getDisplayColor());

		Region rLeft = new Region();
		Region rRight = new Region();
		HBox.setHgrow(rLeft, Priority.ALWAYS);
		HBox.setHgrow(rRight, Priority.ALWAYS);
		HBox header = new HBox(
				ScoreboardText.label(match.gettTeamHome().getDisplayName(), FontWeight.SEMI_BOLD, 17,
						ScoreboardColors.TEXT_DIM),
				rLeft, ScoreboardText.label(Language.getText("statistik"), FontWeight.SEMI_BOLD, 17,
						ScoreboardColors.TEXT_DIM),
				rRight, ScoreboardText.label(match.gettTeamGuest().getDisplayName(), FontWeight.SEMI_BOLD, 17,
						ScoreboardColors.TEXT_DIM));
		header.setAlignment(Pos.CENTER);

		VBox rows = new VBox(6);
		rows.getChildren().add(statRow(Language.getText("ecken"), match.getiCornersHome(),
				match.getiCornersGuest(), colorHome, colorGuest));
		rows.getChildren().add(statRow(Language.getText("gelbe_karten"), match.countYellowCards(Teams.HOME),
				match.countYellowCards(Teams.GUEST), colorHome, colorGuest));
		int iRedHome = match.countRedCards(Teams.HOME);
		int iRedGuest = match.countRedCards(Teams.GUEST);
		if (iRedHome + iRedGuest > 0)
			rows.getChildren().add(statRow(Language.getText("rote_karten"), iRedHome, iRedGuest, colorHome,
					colorGuest));

		VBox content = new VBox(4, header, rows);
		content.setPadding(new Insets(8, 16, 0, 16));
		VBox.setVgrow(content, Priority.ALWAYS);

		VBox layout = new VBox(content);
		if (match.getiVisitors() > 0)
			layout.getChildren().add(visitorsBar(match.getiVisitors()));
		layout.prefWidthProperty().bind(widthProperty());
		layout.prefHeightProperty().bind(heightProperty());
		getChildren().add(layout);
	}

	private static VBox statRow(String sTitle, int iHome, int iGuest, Color colorHome, Color colorGuest) {
		GridPane values = new GridPane();
		ColumnConstraints side = new ColumnConstraints(VALUE_WIDTH);
		ColumnConstraints middle = new ColumnConstraints();
		middle.setHgrow(Priority.ALWAYS);
		values.getColumnConstraints().addAll(side, middle, new ColumnConstraints(VALUE_WIDTH));

		FixedDigitsLabel lbHome = new FixedDigitsLabel(String.valueOf(iHome),
				Main.getMainFont(FontWeight.EXTRA_BOLD, VALUE_SIZE), ScoreboardColors.TEXT);
		FixedDigitsLabel lbGuest = new FixedDigitsLabel(String.valueOf(iGuest),
				Main.getMainFont(FontWeight.EXTRA_BOLD, VALUE_SIZE), ScoreboardColors.TEXT);
		Label lbTitle = ScoreboardText.label(sTitle, FontWeight.SEMI_BOLD, 19, ScoreboardColors.TEXT_DIM);
		GridPane.setHalignment(lbTitle, javafx.geometry.HPos.CENTER);
		GridPane.setHalignment(lbHome, javafx.geometry.HPos.LEFT);
		GridPane.setHalignment(lbGuest, javafx.geometry.HPos.RIGHT);
		values.add(lbHome, 0, 0);
		values.add(lbTitle, 1, 0);
		values.add(lbGuest, 2, 0);

		// The larger value fills its half, the other one in proportion
		int iMax = Math.max(iHome, iGuest);
		HBox bars = new HBox(6, bar(iMax == 0 ? 0 : (double) iHome / iMax, colorHome, Pos.CENTER_RIGHT),
				bar(iMax == 0 ? 0 : (double) iGuest / iMax, colorGuest, Pos.CENTER_LEFT));

		return new VBox(2, values, bars);
	}

	private static StackPane bar(double dShare, Color color, Pos position) {
		Region rBar = new Region();
		rBar.setBackground(new Background(new BackgroundFill(color, null, null)));
		rBar.setMinHeight(BAR_HEIGHT);
		rBar.setMaxHeight(BAR_HEIGHT);
		StackPane half = new StackPane(rBar);
		StackPane.setAlignment(rBar, position);
		rBar.maxWidthProperty().bind(half.widthProperty().multiply(dShare));
		half.setMinHeight(BAR_HEIGHT);
		HBox.setHgrow(half, Priority.ALWAYS);
		half.setMaxWidth(Double.MAX_VALUE);
		return half;
	}

	private static HBox visitorsBar(int iVisitors) {
		HBox bar = new HBox(12,
				ScoreboardText.label(String.format(Locale.GERMAN, "%,d", iVisitors), FontWeight.EXTRA_BOLD, 32,
						ScoreboardColors.TEXT),
				ScoreboardText.label(Language.getText("zuschauer"), FontWeight.SEMI_BOLD, 20,
						ScoreboardColors.TEXT_DIM));
		bar.setAlignment(Pos.CENTER);
		bar.setMinHeight(VISITORS_HEIGHT);
		bar.setPrefHeight(VISITORS_HEIGHT);
		bar.setBackground(new Background(new BackgroundFill(ScoreboardColors.PANEL, null, null)));
		return bar;
	}
}
