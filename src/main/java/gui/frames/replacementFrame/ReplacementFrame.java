/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.replacementFrame;

import football.match.Teams;
import gui.GUIControlling;
import gui.frames.BasicFrame;
import gui.frames.ControlFrame;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import language.Language;
import system.settings.Settings;

public class ReplacementFrame extends BasicFrame {

	private TabPane tabPane;

	public ReplacementFrame(Settings sSettings, GUIControlling guiControlling, ControlFrame controlFrame) {
		super(sSettings, guiControlling, "aufstellung", controlFrame);
	}

	@Override
	protected Pane buildCenterPane(Object defaultParams) {
		BorderPane centerPane = new BorderPane();
		
		tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		ReplacementFrameTeam homeTeam = new ReplacementFrameTeam(Teams.HOME,sSettings,guiControlling,this,(ControlFrame) defaultParams);
		ReplacementFrameTeam guestTeam = new ReplacementFrameTeam(Teams.GUEST,sSettings,guiControlling,this,(ControlFrame) defaultParams);

		Tab tabTeam = new Tab(Language.getText("heimmannschaft"), homeTeam);
		Tab tabPlayers = new Tab(Language.getText("gastmannschaft"), guestTeam);

		tabPane.getTabs().addAll(tabTeam, tabPlayers);

		centerPane.setCenter(tabPane);

		return centerPane;
	}

	@Override
	protected Pane buildBottomPane() {
		return null;
	}
	
	public void closeStage() {
		close();
	}
}
