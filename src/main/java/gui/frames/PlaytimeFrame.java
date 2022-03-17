/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames;

import gui.GUIControlling;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import language.Language;
import message.ErrorMsg;
import system.Main;
import system.settings.Settings;

public class PlaytimeFrame extends BasicFrame {
	private TextField tfPlaytimeMinutes, tfPlaytimeSeconds;

	public PlaytimeFrame(Settings sSettings, GUIControlling guiControlling) {
		super(sSettings, guiControlling, "zeit", null, 300, 200);
	}

	@Override
	protected Pane buildCenterPane(Object defaultParams) {

		GridPane centerPane = new GridPane();
		centerPane.setPadding(new Insets(20));
		centerPane.setHgap(20);
		centerPane.setVgap(20);

		tfPlaytimeMinutes = new TextField("00");
		tfPlaytimeMinutes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

			}
		});

		tfPlaytimeSeconds = new TextField("00");
		tfPlaytimeSeconds.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

			}
		});

		Label lbDoublePoint = new Label(":");
		lbDoublePoint.getStyleClass().add("sf-label-headline");

		centerPane.add(tfPlaytimeMinutes, 0, 0);
		centerPane.add(lbDoublePoint, 1, 0);
		centerPane.add(tfPlaytimeSeconds, 2, 0);

		return centerPane;
	}

	@Override
	protected Pane buildBottomPane() {
		//FIXME Kann nur Länge der Halbzeit angeben also nicht z.B 85. Spielminute sondern 40. Spielminute
		Button btnSave = new Button(Language.getText("speichern"));
		btnSave.setMaxWidth(Double.MAX_VALUE);
		btnSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					int iMinutes = Integer.parseInt(tfPlaytimeMinutes.getText());
					int iSeconds = Integer.parseInt(tfPlaytimeSeconds.getText());
					if (iSeconds < 60) {
						int iTimeInMilliseconds = (iMinutes * 60 + iSeconds) * 1000;
						if (Main.getMatch() != null)
							Main.getMatch().setlStartTimeMillis(System.currentTimeMillis() - iTimeInMilliseconds);
						else
							ErrorMsg.showErrorMessage(206);
					} else {
						ErrorMsg.showErrorMessage(208);
					}
				} catch (NumberFormatException nfe) {
					ErrorMsg.showErrorMessage(205);
				}
			}
		});

		GridPane bottomPane = new GridPane();
		bottomPane.setPadding(new Insets(20));
		bottomPane.setHgap(20);
		bottomPane.setVgap(20);
		bottomPane.prefWidthProperty().bind(widthProperty());

		ColumnConstraints bottomPaneCol1 = new ColumnConstraints();
		bottomPaneCol1.setPercentWidth(50);
		ColumnConstraints bottomPaneCol2 = new ColumnConstraints();
		bottomPaneCol2.setPercentWidth(50);
		bottomPane.getColumnConstraints().addAll(bottomPaneCol1, bottomPaneCol2);

		bottomPane.add(btnSave, 0, 0, 2, 1);

		return bottomPane;

	}
}
