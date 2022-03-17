/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control;

import gui.Components;
import gui.frames.ControlFrame;
import gui.frames.control.buttons.ButtonAbout;
import gui.frames.control.buttons.ButtonAdvertisment;
import gui.frames.control.buttons.ButtonBlack;
import gui.frames.control.buttons.ButtonCard;
import gui.frames.control.buttons.ButtonChange;
import gui.frames.control.buttons.ButtonGoalGuest;
import gui.frames.control.buttons.ButtonGoalHome;
import gui.frames.control.buttons.ButtonLineup;
import gui.frames.control.buttons.ButtonMain;
import gui.frames.control.buttons.ButtonPicture;
import gui.frames.control.buttons.ButtonStatistic;
import gui.frames.control.buttons.ButtonStopTime;
import gui.frames.control.buttons.ButtonVideo;
import gui.frames.control.buttons.ButtonWelcome;
import javafx.geometry.Insets;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class ControlFrameStateTimeRunning implements ControlFrameState {

	@Override
	public GridPane buildButtons(ControlFrame controlFrame) {
		GridPane gpResult = new GridPane();
		gpResult.setPadding(new Insets(10));
		gpResult.setHgap(10);
		gpResult.setVgap(10);

		ColumnConstraints bottomPaneCol1 = new ColumnConstraints();
		bottomPaneCol1.setPercentWidth(50);
		ColumnConstraints bottomPaneCol2 = new ColumnConstraints();
		bottomPaneCol2.setPercentWidth(50);
		gpResult.getColumnConstraints().addAll(bottomPaneCol1, bottomPaneCol2);

		gpResult.add(new ButtonStopTime(controlFrame), 0, 0, 2, 2);

		gpResult.add(Components.getSeparator(), 0, 2, 2, 1);

		gpResult.add(new ButtonMain(controlFrame), 0, 3);
		gpResult.add(new ButtonBlack(controlFrame), 1, 3);

		gpResult.add(Components.getSeparator(), 0, 4, 2, 1);

		gpResult.add(new ButtonGoalHome(controlFrame), 0, 5);
		gpResult.add(new ButtonGoalGuest(controlFrame), 1, 5);
		gpResult.add(new ButtonLineup(controlFrame), 0, 6);
		gpResult.add(new ButtonChange(controlFrame), 1, 6);

		gpResult.add(Components.getSeparator(), 0, 7, 2, 1);

		gpResult.add(new ButtonCard(controlFrame), 0, 8);
		gpResult.add(new ButtonStatistic(controlFrame), 1, 8);

		gpResult.add(Components.getSeparator(), 0, 9, 2, 1);

		gpResult.add(new ButtonAdvertisment(controlFrame), 0, 10);
		gpResult.add(new ButtonWelcome(controlFrame), 1, 10);
		gpResult.add(new ButtonPicture(controlFrame), 0, 11);
		gpResult.add(new ButtonVideo(controlFrame), 1, 11);
		gpResult.add(new ButtonAbout(controlFrame), 0, 12, 2, 1);

		return gpResult;
	}

}
