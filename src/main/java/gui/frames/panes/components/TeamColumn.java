/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes.components;

import football.Team;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 * Logo of a team for the left and right side of the main view and the corners, centered next to the result.
 */
public final class TeamColumn {
	public static final double WIDTH = 150;
	// Larger than the logos elsewhere: there is no name below it
	private static final double LOGO_HEIGHT = 120;

	private TeamColumn() {
	}

	public static VBox create(Team team) {
		VBox column = new VBox(TeamCrest.of(team, LOGO_HEIGHT));
		column.setAlignment(Pos.CENTER);
		column.setMinWidth(WIDTH);
		column.setPrefWidth(WIDTH);
		column.setMaxWidth(WIDTH);
		return column;
	}
}
