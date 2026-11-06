/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import football.Cards;
import football.Team;
import football.person.Player;
import gui.frames.panes.components.ScoreboardText;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;

/**
 * Card for a player: the card drawn as a shape (stays sharp on the LED wall), shirt number in the card color,
 * name and a line with card, team and minute. The score bar stays visible below.
 */
public class PaneCard extends PaneEvent {
	private static final double WALL_WIDTH = 514;
	// Right of the cards, also of the offset red card for yellow-red
	private static final double TEXT_LEFT = 196;

	/**
	 * @param team    team of the player, may be null if unknown
	 * @param sMinute minute of the card, e.g. "58'", may be empty
	 */
	public PaneCard(Pane rootPane, Player player, Team team, Cards card, String sMinute) {
		super(rootPane);

		Group cards = placeTopLeft(drawCards(card), 40, 40);
		Color numberColor = card == Cards.YELLOW ? ScoreboardColors.CARD_YELLOW : ScoreboardColors.CARD_RED_TEXT;
		Label lbNumber = placeTopLeft(ScoreboardText.label(player.getiNumber() < 0 ? "" : String.valueOf(player.getiNumber()),
				FontWeight.EXTRA_BOLD, 44, numberColor), 34, TEXT_LEFT);
		Label lbName = placeTopLeft(ScoreboardText.fittingLabel(player.getsLastname(), FontWeight.EXTRA_BOLD, 58, 32,
				WALL_WIDTH - TEXT_LEFT - 16, ScoreboardColors.TEXT), 80, TEXT_LEFT);

		StringBuilder sbMeta = new StringBuilder(card.toString());
		if (team != null)
			sbMeta.append(" · ").append(team.getDisplayName());
		if (sMinute != null && !sMinute.isEmpty())
			sbMeta.append(" · ").append(sMinute);
		Label lbMeta = placeTopLeft(ScoreboardText.fittingLabel(sbMeta.toString(), FontWeight.SEMI_BOLD, 22, 15,
				WALL_WIDTH - TEXT_LEFT - 16, ScoreboardColors.TEXT_DIM), 150, TEXT_LEFT + 2);

		buildIn(cards, lbNumber, lbName, lbMeta);
	}

	private static Group drawCards(Cards card) {
		switch (card) {
		case RED:
			return new Group(card(ScoreboardColors.CARD_RED, -8));
		case YELLOW_RED:
			Rectangle yellow = card(ScoreboardColors.CARD_YELLOW, -14);
			Rectangle red = card(ScoreboardColors.CARD_RED, 6);
			red.setTranslateX(26);
			red.setTranslateY(6);
			return new Group(yellow, red);
		default:
			return new Group(card(ScoreboardColors.CARD_YELLOW, -8));
		}
	}

	private static Rectangle card(Color color, double dRotation) {
		Rectangle card = new Rectangle(96, 136, color);
		card.setArcWidth(12);
		card.setArcHeight(12);
		card.setRotate(dRotation);
		return card;
	}

	@Override
	public boolean showsScoreBar() {
		return true;
	}
}
