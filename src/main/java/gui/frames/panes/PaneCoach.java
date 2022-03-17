/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import football.person.Coach;
import javafx.scene.layout.Pane;

public class PaneCoach extends PanePerson {

	public PaneCoach(Pane rootPane, PanePersonIcons icon, Coach coachToShow) {
		super(rootPane, icon, coachToShow.geteCoachType() == null ? null : coachToShow.geteCoachType().toString(),
				coachToShow.getsFirstname(), coachToShow.getsLastname(), coachToShow.getsImagePath());
	}
}
