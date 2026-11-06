/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes.components;

import football.Team;
import gui.frames.panes.ScoreboardColors;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;

/**
 * Logo, short name and team color of a team, stacked for the left and right side of the scoreboard.
 */
public final class TeamColumn {
	public static final double WIDTH = 150;

	private TeamColumn() {
	}

	public static VBox create(Team team) {
		Label lbName = ScoreboardText.fittingLabel(team.getDisplayName(), FontWeight.BOLD, 24, 16, WIDTH - 8,
				ScoreboardColors.TEXT);
		Region rColor = new Region();
		rColor.setMinSize(64, 4);
		rColor.setPrefSize(64, 4);
		rColor.setMaxSize(64, 4);
		rColor.setBackground(new Background(new BackgroundFill(Color.web(team.getDisplayColor()), null, null)));

		VBox column = new VBox(8, TeamCrest.of(team, 96), lbName, rColor);
		column.setAlignment(Pos.CENTER);
		column.setMinWidth(WIDTH);
		column.setPrefWidth(WIDTH);
		column.setMaxWidth(WIDTH);
		return column;
	}
}
