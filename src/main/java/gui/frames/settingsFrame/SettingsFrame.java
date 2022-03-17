/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.settingsFrame;

import java.util.ArrayList;
import java.util.List;

import gui.GUIControlling;
import gui.frames.BasicFrame;
import gui.frames.panes.PaneAbout;
import gui.frames.panes.PaneMain;
import gui.frames.panes.PaneSettings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.WindowEvent;
import language.Language;
import system.Main;
import system.settings.Settings;

public class SettingsFrame extends BasicFrame {
	private TabPane tabPane;
	private List<SettingsComponent> tabs;

	public SettingsFrame(Settings sSettings, GUIControlling guiControlling) {
		super(sSettings, guiControlling, "einstellungen", null);

		setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if (Main.getMatch() != null) {
					guiControlling.changeMainScoreboardPane(
							new PaneMain(guiControlling.getRootPaneMainScoreboard(), guiControlling.getControlFrame()));
				} else {
					guiControlling.changeMainScoreboardPane(new PaneAbout(guiControlling.getRootPaneMainScoreboard()));
				}
			}

		});
	}

	@Override
	protected Pane buildCenterPane(Object defaultParams) {
		tabs = new ArrayList<SettingsComponent>();
		tabs.add(new SettingsFrameGeneral(sSettings));
		tabs.add(new SettingsFrameBoards(sSettings));

		guiControlling.changeMainScoreboardPane(new PaneSettings(guiControlling.getRootPaneMainScoreboard()));

		BorderPane centerPane = new BorderPane();

		tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		for (SettingsComponent tab : tabs) {
			Tab t = new Tab(Language.getText(tab.getTitleKey()), tab);
			tabPane.getTabs().addAll(t);
		}

		centerPane.setCenter(tabPane);

		return centerPane;
	}

	@Override
	protected Pane buildBottomPane() {
		// Create the bottom Pane
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

		// Creates the save button.
		Button btnSave = new Button(Language.getText("speichern"));
		btnSave.setMaxWidth(Double.MAX_VALUE);
		btnSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				save();
			}
		});

		// Creates the reset button.
		Button btnReset = new Button(Language.getText("reset"));
		btnReset.setMaxWidth(Double.MAX_VALUE);
		btnReset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				reset();
			}
		});

		// Creates the restore to default button.
		Button btnRestoreDefault = new Button(Language.getText("restoreDefault"));
		btnRestoreDefault.setMaxWidth(Double.MAX_VALUE);
		btnRestoreDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				restoreDefault();
			}
		});

		bottomPane.add(btnSave, 0, 0);
		bottomPane.add(btnReset, 1, 0);
		bottomPane.add(btnRestoreDefault, 0, 1, 2, 1);

		return bottomPane;
	}

	private void save() {

		for (SettingsComponent tab : tabs)
			tab.saveToSettings();

		sSettings.save();

		guiControlling.updateScoreboards();
		reset();

//			if(langChanged)
//				Message.show(AlertType.INFORMATION, Language.getText("info"), "langChanged", "");

	}

	public void reset() {
		for (SettingsComponent tab : tabs)
			tab.reset();
	}

	public void restoreDefault() {
		for (SettingsComponent tab : tabs)
			tab.restoreDefaultValues();

		save();
	}
}
