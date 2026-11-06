/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import football.match.Match;
import gui.frames.panes.components.FixedDigitsLabel;
import gui.frames.panes.components.ScoreboardText;
import gui.frames.panes.components.TeamColumn;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import language.Language;
import system.Main;

/**
 * Corners of both teams, laid out like the main view with the corners instead of the result.
 */
public class PaneCorners extends PaneMainScoreboard {
	private final Match match;
	private final FixedDigitsLabel lbCornersHome;
	private final FixedDigitsLabel lbCornersGuest;
	// Kept as a field: the match lives longer than this pane and only holds a weak reference
	private final ChangeListener<String> cornersListener = (observable, oldValue, newValue) -> showCorners();

	public PaneCorners(Pane rootPane) {
		super(ScoreboardBackground.crestDiagonal(), rootPane);
		match = Main.getMatch();

		Label lbTitle = ScoreboardText.label(Language.getText("ecken"), FontWeight.BOLD, 34, ScoreboardColors.TEXT_DIM);

		lbCornersHome = new FixedDigitsLabel("0", Main.getMainFont(FontWeight.BOLD, 150), ScoreboardColors.TEXT);
		lbCornersGuest = new FixedDigitsLabel("0", Main.getMainFont(FontWeight.BOLD, 150), ScoreboardColors.TEXT);
		Label lbColon = ScoreboardText.label(":", FontWeight.MEDIUM, 110, ScoreboardColors.TEXT_DIM);
		lbColon.setTranslateY(-8);
		HBox corners = new HBox(6, lbCornersHome, lbColon, lbCornersGuest);
		corners.setAlignment(Pos.CENTER);
		HBox.setHgrow(corners, Priority.ALWAYS);

		HBox middle = new HBox(TeamColumn.create(match.gettTeamHome()), corners,
				TeamColumn.create(match.gettTeamGuest()));
		middle.setAlignment(Pos.CENTER);
		VBox.setVgrow(middle, Priority.ALWAYS);

		VBox layout = new VBox(lbTitle, middle);
		layout.setAlignment(Pos.TOP_CENTER);
		layout.setPadding(new Insets(14, 0, 14, 0));
		layout.prefWidthProperty().bind(widthProperty());
		layout.prefHeightProperty().bind(heightProperty());
		getChildren().add(layout);

		showCorners();
		match.sCorners.addListener(new WeakChangeListener<String>(cornersListener));
	}

	private void showCorners() {
		lbCornersHome.setText(String.valueOf(match.getiCornersHome()));
		lbCornersGuest.setText(String.valueOf(match.getiCornersGuest()));
	}
}
