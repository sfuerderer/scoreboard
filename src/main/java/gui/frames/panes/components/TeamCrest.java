/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes.components;

import java.net.MalformedURLException;

import football.Team;
import javafx.scene.image.ImageView;
import message.ErrorMsg;
import system.Images;

/**
 * Logo of a team for the scoreboard, or the placeholder if the team has none.
 */
public final class TeamCrest {

	private TeamCrest() {
	}

	public static ImageView of(Team team, double dHeight) {
		ImageView ivLogo;
		if (team == null || team.getsPathToLogo() == null) {
			ivLogo = new ImageView(Images.getImages().loadImageFromResource("no-image", "png"));
		} else {
			try {
				ivLogo = new ImageView(Images.getImages().loadImageFromPath(team.getsPathToLogo()));
			} catch (MalformedURLException e) {
				ErrorMsg.showErrorMessage(111);
				ivLogo = new ImageView(Images.getImages().loadImageFromResource("no-image", "png"));
			}
		}
		ivLogo.setFitHeight(dHeight);
		ivLogo.setPreserveRatio(true);
		ivLogo.setSmooth(true);
		return ivLogo;
	}
}
