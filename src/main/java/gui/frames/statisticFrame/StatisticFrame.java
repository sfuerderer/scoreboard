/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.statisticFrame;

import football.match.Teams;
import gui.Components;
import gui.GUIControlling;
import gui.frames.BasicFrame;
import gui.frames.ControlFrame;
import gui.frames.panes.PaneCorners;
import gui.frames.panes.PaneMain;
import gui.frames.panes.PaneVisitors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import language.Language;
import message.ErrorMsg;
import system.Main;
import system.settings.Settings;

public class StatisticFrame extends BasicFrame implements Runnable {

	private ControlFrame controlFrame;
	private boolean isShowing = false;

	public StatisticFrame(Settings sSettings, GUIControlling guiControlling, ControlFrame controlFrame) {
		super(sSettings, guiControlling, "statistik", controlFrame, 400, 200);
	}

	@Override
	protected Pane buildCenterPane(Object defaultParams) {
		controlFrame = (ControlFrame) defaultParams;

		if (Main.getMatch() == null) {
			ErrorMsg.showErrorMessage(206);
			return null;
		}

		BorderPane centerPane = new BorderPane();
		centerPane.prefWidthProperty().bind(this.widthProperty());
		centerPane.prefHeightProperty().bind(this.heightProperty());

		centerPane.setLeft(getPaneVisitors());
		centerPane.setCenter(getPaneCorners());

		return centerPane;
	}

	@Override
	protected Pane buildBottomPane() {
		return null;
	}

	private GridPane getPaneVisitors() {
		GridPane result = new GridPane();
		result.setPadding(new Insets(10));
		result.setHgap(10);
		result.setVgap(10);

		Label lbTitleVisitors = Components.getLabelControlFrame(Language.getText("zuschauer"), Pos.CENTER, 14);
		GridPane.setHalignment(lbTitleVisitors, HPos.CENTER);

		TextField tfVisitors = new TextField(Main.getMatch().getiVisitors() + "");

		Button btnShowVisitors = new Button(Language.getText("anzeigen"));
		btnShowVisitors.setMaxWidth(Double.MAX_VALUE);
		btnShowVisitors.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Main.getMatch().setiVisitors(Integer.parseInt(tfVisitors.getText()));
					guiControlling
							.changeMainScoreboardPane(new PaneVisitors(guiControlling.getRootPaneMainScoreboard()));
				} catch (NumberFormatException nfe) {
					ErrorMsg.showErrorMessage(205);
				}
			}
		});

		result.add(lbTitleVisitors, 0, 0);
		result.add(tfVisitors, 0, 1);
		result.add(btnShowVisitors, 0, 2);
		return result;
	}

	private GridPane getPaneCorners() {
		GridPane result = new GridPane();
		result.setPadding(new Insets(10));
		result.setHgap(10);
		result.setVgap(10);
		
		ColumnConstraints bottomPaneCol1 = new ColumnConstraints();
		bottomPaneCol1.setPercentWidth(30);
		ColumnConstraints bottomPaneCol2 = new ColumnConstraints();
		bottomPaneCol2.setPercentWidth(40);
		bottomPaneCol2.setHalignment(HPos.CENTER);
		ColumnConstraints bottomPaneCol3 = new ColumnConstraints();
		bottomPaneCol3.setPercentWidth(30);
		result.getColumnConstraints().addAll(bottomPaneCol1, bottomPaneCol2, bottomPaneCol3);

		Label lbTitleCorners = Components.getLabelControlFrame(Language.getText("ecken"), Pos.CENTER, 14);
		GridPane.setHalignment(lbTitleCorners, HPos.CENTER);

		Button btnAddCornerHome = new Button("+");
		btnAddCornerHome.setMaxWidth(Double.MAX_VALUE);
		btnAddCornerHome.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.getMatch().addCorner(Teams.HOME);
				showCornerPane();
			}
		});

		Button btnRemoveCornerHome = new Button("-");
		btnRemoveCornerHome.setMaxWidth(Double.MAX_VALUE);
		btnRemoveCornerHome.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.getMatch().removeCorner(Teams.HOME);
				showCornerPane();
			}
		});

		Button btnAddCornerGuest = new Button("+");
		btnAddCornerGuest.setMaxWidth(Double.MAX_VALUE);
		btnAddCornerGuest.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.getMatch().addCorner(Teams.GUEST);
				showCornerPane();
			}
		});

		Button btnRemoveCornerGuest = new Button("-");
		btnRemoveCornerGuest.setMaxWidth(Double.MAX_VALUE);
		btnRemoveCornerGuest.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.getMatch().removeCorner(Teams.GUEST);
				showCornerPane();
			}
		});

		Label lbCorners = new Label("0:0");
		GridPane.setHalignment(lbCorners, HPos.CENTER);
		lbCorners.textProperty().bind(Main.getMatch().sCorners);

		result.add(lbTitleCorners, 0, 0, 3, 1);
		result.add(btnAddCornerHome, 0, 1);
		result.add(btnRemoveCornerHome, 0, 2);
		result.add(lbCorners, 1, 1, 1, 2);
		result.add(btnAddCornerGuest, 2, 1);
		result.add(btnRemoveCornerGuest, 2, 2);

		return result;
	}

	public void run() {
		try {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					guiControlling
							.changeMainScoreboardPane(new PaneCorners(guiControlling.getRootPaneMainScoreboard()));
				}
			});

			Thread.sleep(sSettings.iTimeUntilGoToMainInMiliseconds);

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					guiControlling.changeMainScoreboardPane(
							new PaneMain(guiControlling.getRootPaneMainScoreboard(), controlFrame));
				}
			});

		} catch (InterruptedException ie) {
			ErrorMsg.showErrorMessage(116);
		} finally {
			isShowing = false;
		}
	}

	private void showCornerPane() {
		if (!isShowing) {
			isShowing = true;
			new Thread(this).start();
		}
	}
}
