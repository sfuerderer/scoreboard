/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import football.Team;
import football.person.Coach;
import football.person.Player;
import gui.frames.panes.components.PlayerPhoto;
import gui.frames.panes.components.ScoreboardText;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.FontWeight;
import language.Language;

/**
 * Introduction of a player or coach, e.g. the starting eleven before kick-off: label and shirt number on the
 * left, the name below, the photo large on the right.
 */
public class PanePersonIntro extends PaneEvent {
	private static final double PHOTO_HEIGHT = 300;
	private static final double WALL_WIDTH = 514;
	private static final double LEFT = 22;

	public PanePersonIntro(Pane rootPane, Player player, Team team) {
		this(rootPane, team == null ? "" : team.getDisplayName(),
				player.getiNumber() < 0 ? null : String.valueOf(player.getiNumber()), player.getsFirstname(),
				player.getsLastname(), player.getsImagePath());
	}

	public PanePersonIntro(Pane rootPane, Coach coach) {
		this(rootPane, coach.geteCoachType() == null ? Language.getText("trainer") : coach.geteCoachType().toString(),
				null, coach.getsFirstname(), coach.getsLastname(), coach.getsImagePath());
	}

	/**
	 * @param sNumber shirt number, null for coaches
	 */
	private PanePersonIntro(Pane rootPane, String sLabel, String sNumber, String sFirstname, String sLastname,
			String sPathToImage) {
		super(rootPane);

		Region photo = PlayerPhoto.create(sPathToImage, PHOTO_HEIGHT);
		if (photo != null)
			placeTopRight(photo, 6, 10);
		double dNameWidth = (photo == null ? WALL_WIDTH : WALL_WIDTH - photo.getPrefWidth()) - LEFT - 16;

		Label lbLabel = placeTopLeft(ScoreboardText.label(sLabel, FontWeight.SEMI_BOLD, 17, ScoreboardColors.TEXT_DIM),
				22, LEFT);
		// Without a number (coach) the name moves up
		double dNameTop = sNumber == null ? 100 : 182;
		Label lbNumber = null;
		Region rBar = null;
		if (sNumber != null) {
			lbNumber = placeTopLeft(ScoreboardText.label(sNumber, FontWeight.EXTRA_BOLD, 118, ScoreboardColors.TEXT),
					40, LEFT - 2);
			rBar = new Region();
			rBar.setMinSize(60, 5);
			rBar.setMaxSize(60, 5);
			rBar.setBackground(new Background(new BackgroundFill(ScoreboardColors.ACCENT_LIGHT, null, null)));
			placeTopLeft(rBar, 168, LEFT);
		}
		Label lbFirstname = placeTopLeft(ScoreboardText.label(sFirstname, FontWeight.SEMI_BOLD, 28,
				ScoreboardColors.TEXT_DIM), dNameTop, LEFT);
		Label lbLastname = placeTopLeft(ScoreboardText.fittingLabel(sLastname, FontWeight.EXTRA_BOLD, 62, 30,
				dNameWidth, ScoreboardColors.TEXT), dNameTop + 28, LEFT - 2);

		buildIn(photo, lbLabel, lbNumber, rBar, lbFirstname, lbLastname);
	}
}
