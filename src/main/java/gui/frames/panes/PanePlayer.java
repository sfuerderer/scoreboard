/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import football.person.Player;
import javafx.scene.layout.Pane;

public class PanePlayer extends PanePerson {

	public PanePlayer(Pane rootPane, PanePersonIcons icon, Player playerToShow) {
		super(rootPane, icon,
				(playerToShow.getiNumber() == -1 ? "" : playerToShow.getiNumber()) + "", playerToShow.getsFirstname(),
				playerToShow.getsLastname(), playerToShow.getsImagePath());
	}
}
