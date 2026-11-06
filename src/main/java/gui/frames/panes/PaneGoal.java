/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import football.person.Player;
import gui.frames.panes.components.PlayerPhoto;
import gui.frames.panes.components.ScoreboardText;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import language.Language;

/**
 * Goal scorer: the player in front of her outlined shirt number, "TOR!", her name and the minute. Without a
 * photo the large number stands alone on the left. The score bar stays visible below.
 */
public class PaneGoal extends PaneEvent {
	private static final double PHOTO_HEIGHT = 300;
	private static final double WALL_WIDTH = 514;

	/**
	 * @param sMinute minute of the goal, e.g. "68'", may be empty
	 */
	public PaneGoal(Pane rootPane, Player scorer, String sMinute) {
		super(rootPane);

		// A photo card ends above the score bar, a cut-out photo reaches behind it
		Region photo = PlayerPhoto.create(scorer.getsImagePath(), PHOTO_HEIGHT, 280 - SCORE_BAR_HEIGHT);
		boolean bCard = PlayerPhoto.isCard(photo);
		double dPhotoLeft = bCard ? 0 : 24;
		double dTextLeft = photo == null ? 200 : dPhotoLeft + photo.getPrefWidth() + (bCard ? 18 : 38);

		Text number = placeTopLeft(outlineNumber(scorer.getiNumber(), 320), -40, photo == null ? -10 : 120);
		if (photo != null)
			placeTopLeft(photo, bCard ? 0 : 6, dPhotoLeft);

		Label lbGoal = ScoreboardText.label(Language.getText("tor_anzeige"), FontWeight.EXTRA_BOLD, 60,
				ScoreboardColors.TEXT);
		Label lbExclamation = ScoreboardText.label("!", FontWeight.EXTRA_BOLD, 60, ScoreboardColors.ACCENT_LIGHT);
		HBox goal = placeTopLeft(new HBox(8, lbGoal, lbExclamation), 14, dTextLeft);
		Label lbFirstname = placeTopLeft(ScoreboardText.label(scorer.getsFirstname(), FontWeight.SEMI_BOLD, 30,
				ScoreboardColors.TEXT_DIM), 94, dTextLeft + 2);
		Label lbLastname = placeTopLeft(ScoreboardText.fittingLabel(scorer.getsLastname(), FontWeight.EXTRA_BOLD, 86,
				40, WALL_WIDTH - dTextLeft - 16, ScoreboardColors.TEXT), 122, dTextLeft);
		Label lbMinute = placeTopRight(ScoreboardText.label(sMinute, FontWeight.BOLD, 28, ScoreboardColors.TEXT_DIM),
				22, 16);

		buildIn(number, photo, goal, lbFirstname, lbLastname, lbMinute);
	}

	@Override
	public boolean showsScoreBar() {
		return true;
	}
}
