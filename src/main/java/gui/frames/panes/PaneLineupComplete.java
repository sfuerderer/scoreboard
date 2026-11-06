/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import java.util.List;

import football.Team;
import football.match.Lineup;
import football.person.Coach;
import football.person.Player;
import football.person.Position;
import gui.frames.panes.components.ScoreboardText;
import gui.frames.panes.components.TeamCrest;
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
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import language.Language;

/**
 * Starting eleven of a team in two columns: logo, title and coach in the header, below each player with
 * her shirt number in a box (goalkeeper in yellow), initial of the first name and last name.
 */
public class PaneLineupComplete extends PaneMainScoreboard {
	private static final double HEADER_HEIGHT = 46;
	private static final double COLUMN_GAP = 18;
	private static final double NUMBER_WIDTH = 34;

	/**
	 * @param coach head coach shown in the header, may be null
	 */
	public PaneLineupComplete(Pane rootPane, List<Player> playersToShow, Team team, Coach coach) {
		super(ScoreboardBackground.crestDiagonal(), rootPane);

		// Header
		Label lbTitle = ScoreboardText.label(Language.getText("startelf"), FontWeight.EXTRA_BOLD, 28,
				ScoreboardColors.TEXT);
		Region rSpacer = new Region();
		HBox.setHgrow(rSpacer, Priority.ALWAYS);
		HBox header = new HBox(10, TeamCrest.of(team, 34), lbTitle, rSpacer);
		if (coach != null) {
			Label lbCoachTitle = ScoreboardText.label(Language.getText("trainer"), FontWeight.SEMI_BOLD, 14,
					ScoreboardColors.TEXT_DIM);
			String sFirstname = coach.getsFirstname();
			String sCoachName = sFirstname == null || sFirstname.isEmpty() ? coach.getsLastname()
					: sFirstname.charAt(0) + ". " + coach.getsLastname();
			Label lbCoachName = ScoreboardText.label(sCoachName, FontWeight.BOLD, 16, ScoreboardColors.TEXT);
			VBox coachBox = new VBox(-2, lbCoachTitle, lbCoachName);
			coachBox.setAlignment(Pos.CENTER_RIGHT);
			header.getChildren().add(coachBox);
		}
		header.setAlignment(Pos.CENTER_LEFT);
		header.setPadding(new Insets(0, 14, 0, 14));
		header.setMinHeight(HEADER_HEIGHT);
		header.setPrefHeight(HEADER_HEIGHT);
		header.setBackground(new Background(new BackgroundFill(ScoreboardColors.PANEL, null, null)));

		// Players: first column top to bottom, then the second column
		List<Player> lPlayers = Lineup.sortedForScoreboard(playersToShow);
		int iRows = Math.max(1, (lPlayers.size() + 1) / 2);
		GridPane grid = new GridPane();
		grid.setHgap(COLUMN_GAP);
		grid.setVgap(4);
		grid.setPadding(new Insets(8, 14, 8, 14));
		for (int i = 0; i < 2; i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setPercentWidth(50);
			grid.getColumnConstraints().add(column);
		}
		for (int i = 0; i < iRows; i++) {
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(100.0 / iRows);
			grid.getRowConstraints().add(row);
		}
		for (int i = 0; i < lPlayers.size(); i++)
			grid.add(playerLine(lPlayers.get(i), iRows), i / iRows, i % iRows);
		VBox.setVgrow(grid, Priority.ALWAYS);

		VBox layout = new VBox(header, grid);
		layout.prefWidthProperty().bind(widthProperty());
		layout.prefHeightProperty().bind(heightProperty());
		getChildren().add(layout);
	}

	private static HBox playerLine(Player player, int iRows) {
		// More than six rows (e.g. with substitutes) get a smaller font, so every name stays on the wall
		int iSize = iRows <= 6 ? 21 : 16;
		Label lbNumber = ScoreboardText.label(player.getiNumber() < 0 ? "" : String.valueOf(player.getiNumber()),
				FontWeight.EXTRA_BOLD, iSize - 1, Color.BLACK);
		lbNumber.setAlignment(Pos.CENTER);
		lbNumber.setMinWidth(NUMBER_WIDTH);
		lbNumber.setPrefWidth(NUMBER_WIDTH);
		lbNumber.setBackground(new Background(new BackgroundFill(
				player.getePosition() == Position.GOALKEEPER ? ScoreboardColors.CARD_YELLOW : ScoreboardColors.TEXT,
				null, null)));

		String sInitial = player.getsFirstname() == null || player.getsFirstname().isEmpty() ? ""
				: player.getsFirstname().charAt(0) + ".";
		Label lbInitial = ScoreboardText.label(sInitial, FontWeight.MEDIUM, iSize - 4, ScoreboardColors.TEXT_DIM);
		Label lbLastname = ScoreboardText.label(player.getsLastname(), FontWeight.BOLD, iSize, ScoreboardColors.TEXT);
		lbLastname.setMinWidth(0);

		HBox line = new HBox(9, lbNumber, lbInitial, lbLastname);
		HBox.setMargin(lbInitial, new Insets(0, -5, 0, 0));
		line.setAlignment(Pos.CENTER_LEFT);
		return line;
	}
}
