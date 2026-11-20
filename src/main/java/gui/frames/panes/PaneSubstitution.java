/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import advertising.AdPlacement;
import football.Team;
import football.person.Player;
import gui.frames.panes.components.ScoreboardText;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.text.FontWeight;
import language.Language;

/**
 * Substitution in one view: the player coming on at the top in full brightness, the player going off below
 * dimmed. The score bar stays visible below.
 */
public class PaneSubstitution extends PaneEvent {
	private static final double ROW_LEFT = 16;
	private static final double NAME_WIDTH = 514 - 2 * ROW_LEFT - 44 - 74 - 20;

	/**
	 * @param team    team that substitutes, may be null if unknown
	 * @param sMinute minute of the substitution, e.g. "71'", may be empty
	 */
	public PaneSubstitution(Pane rootPane, Team team, Player playerIn, Player playerOut, String sMinute) {
		super(rootPane);

		String sTitle = Language.getText("wechsel_anzeige") + (team == null ? "" : " · " + team.getDisplayName());
		Label lbTitle = placeTopLeft(ScoreboardText.label(sTitle, FontWeight.SEMI_BOLD, 17, ScoreboardColors.TEXT_DIM),
				12, ROW_LEFT);
		Label lbMinute = placeTopRight(ScoreboardText.label(sMinute, FontWeight.SEMI_BOLD, 17,
				ScoreboardColors.TEXT_DIM), 12, ROW_LEFT);

		HBox rowIn = placeTopLeft(row(playerIn, true), 40, ROW_LEFT);
		HBox rowOut = placeTopLeft(row(playerOut, false), 132, ROW_LEFT);
		rowOut.setOpacity(0.62);

		buildIn(lbTitle, lbMinute, rowIn, rowOut, showPresentedBy(AdPlacement.SUBSTITUTION));
	}

	private static HBox row(Player player, boolean bComingOn) {
		Polygon arrow = bComingOn ? new Polygon(0, 26, 36, 26, 18, 0) : new Polygon(0, 0, 36, 0, 18, 26);
		arrow.setFill(bComingOn ? ScoreboardColors.SUBSTITUTION_IN : ScoreboardColors.SUBSTITUTION_OUT);

		Label lbNumber = ScoreboardText.label(player.getiNumber() < 0 ? "" : String.valueOf(player.getiNumber()),
				FontWeight.EXTRA_BOLD, 56, ScoreboardColors.TEXT);
		lbNumber.setMinWidth(74);
		lbNumber.setPrefWidth(74);
		lbNumber.setAlignment(Pos.CENTER_RIGHT);

		Label lbFirstname = ScoreboardText.label(player.getsFirstname(), FontWeight.SEMI_BOLD, 20,
				ScoreboardColors.TEXT_DIM);
		Label lbLastname = ScoreboardText.fittingLabel(player.getsLastname(), FontWeight.EXTRA_BOLD, 46, 26,
				NAME_WIDTH, ScoreboardColors.TEXT);
		VBox name = new VBox(-4, lbFirstname, lbLastname);
		name.setAlignment(Pos.CENTER_LEFT);

		HBox row = new HBox(10, arrow, lbNumber, name);
		row.setAlignment(Pos.CENTER_LEFT);
		row.setMinHeight(84);
		row.setMaxHeight(84);
		row.setMaxWidth(514 - 2 * ROW_LEFT);
		return row;
	}

	@Override
	public boolean showsScoreBar() {
		return true;
	}
}
