/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames;

import football.match.Teams;
import gui.GUIControlling;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import message.ErrorMsg;
import system.Main;
import system.settings.Settings;

public class ResultFrame extends BasicFrame {

	public ResultFrame(Settings sSettings, GUIControlling guiControlling) {
		super(sSettings, guiControlling, "ergebnis", null, 300, 200);
	}

	@Override
	protected Pane buildCenterPane(Object defaultParams) {
		if (Main.getMatch() == null) {
			ErrorMsg.showErrorMessage(206);
			return null;
		}
		

		GridPane centerPane = new GridPane();
		centerPane.setPadding(new Insets(20));
		centerPane.setHgap(20);
		centerPane.setVgap(20);

		ColumnConstraints bottomPaneCol1 = new ColumnConstraints();
		bottomPaneCol1.setPercentWidth(25);
		ColumnConstraints bottomPaneCol2 = new ColumnConstraints();
		bottomPaneCol2.setPercentWidth(50);
		ColumnConstraints bottomPaneCol3 = new ColumnConstraints();
		bottomPaneCol3.setPercentWidth(25);
		centerPane.getColumnConstraints().addAll(bottomPaneCol1, bottomPaneCol2, bottomPaneCol3);

		Label lbResult = new Label(Main.getMatch().getiGoalsHome()+":"+Main.getMatch().getiGoalsGuest());
		GridPane.setHalignment(lbResult, HPos.CENTER);
		lbResult.textProperty().bind(Main.getMatch().sResult);

		Button btnPlusHome = new Button("+");
		btnPlusHome.setMaxWidth(Double.MAX_VALUE);
		btnPlusHome.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (Main.getMatch() != null)
					Main.getMatch().addGoal(Teams.HOME);
				else
					ErrorMsg.showErrorMessage(206);
			}
		});

		Button btnMinusHome = new Button("-");
		btnMinusHome.setMaxWidth(Double.MAX_VALUE);
		btnMinusHome.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (Main.getMatch() != null)
					Main.getMatch().removeGoal(Teams.HOME);
				else
					ErrorMsg.showErrorMessage(206);
			}
		});

		Button btnPlusGuest = new Button("+");
		btnPlusGuest.setMaxWidth(Double.MAX_VALUE);
		btnPlusGuest.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (Main.getMatch() != null)
					Main.getMatch().addGoal(Teams.GUEST);
				else
					ErrorMsg.showErrorMessage(206);
			}
		});

		Button btnMinusGuest = new Button("-");
		btnMinusGuest.setMaxWidth(Double.MAX_VALUE);
		btnMinusGuest.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (Main.getMatch() != null)
					Main.getMatch().removeGoal(Teams.GUEST);
				else
					ErrorMsg.showErrorMessage(206);
			}
		});

		centerPane.add(btnPlusHome, 0, 0);
		centerPane.add(btnMinusHome, 0, 1);
		centerPane.add(lbResult, 1, 0, 1, 2);
		centerPane.add(btnPlusGuest, 2, 0);
		centerPane.add(btnMinusGuest, 2, 1);

		return centerPane;

	}

	@Override
	protected Pane buildBottomPane() {
		return null;
	}
}
