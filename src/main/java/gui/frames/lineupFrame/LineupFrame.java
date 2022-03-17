/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.lineupFrame;

import football.match.Teams;
import gui.GUIControlling;
import gui.frames.BasicFrame;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import language.Language;
import message.ErrorMsg;
import system.Main;
import system.settings.Settings;

public class LineupFrame extends BasicFrame {
	
	private TabPane tabPane;

	public LineupFrame(Settings sSettings, GUIControlling guiControlling) {
		super(sSettings, guiControlling, "aufstellung", null);
	}

	@Override
	protected Pane buildCenterPane(Object defaultParams) {
		if(Main.getMatch()==null) {
			ErrorMsg.showErrorMessage(206);
			close();
		}
		BorderPane centerPane = new BorderPane();

		tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		LineupFrameTeam homeTeam = new LineupFrameTeam(Teams.HOME);
		LineupFrameTeam guestTeam = new LineupFrameTeam(Teams.GUEST);
		
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
}
