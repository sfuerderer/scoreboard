/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.teamFrame;

import java.io.IOException;

import exceptions.FileDidNotExistException;
import football.Team;
import football.TeamWrapper;
import gui.GUIControlling;
import gui.frames.BasicFrame;
import javafx.collections.ObservableList;
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
import language.Language;
import message.ErrorMsg;
import system.FileHandling;
import system.Resetable;
import system.settings.Settings;

public class TeamFrame extends BasicFrame {

	private Team team;
	private TeamFrameTeam teamFrame;
	private TeamFramePlayers playersFrame;
	private TeamFrameCoaches coachesFrame;
	private TabPane tabPane;
	
	public TeamFrame(Settings sSettings, GUIControlling guiControlling, TeamWrapper teamWrapper) {
		super(sSettings, guiControlling, "mannschaft", teamWrapper);
	}

	@Override
	protected Pane buildCenterPane(Object defaultParams) {
		try {
			team = loadTeamFromTeamWrapper((TeamWrapper) defaultParams);
			
			BorderPane centerPane = new BorderPane();

			tabPane = new TabPane();
			tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

			teamFrame = new TeamFrameTeam(team);
			playersFrame = new TeamFramePlayers(team);
			coachesFrame = new TeamFrameCoaches(team);
			Tab tabTeam = new Tab(Language.getText("mannschaft"), teamFrame);
			Tab tabPlayers = new Tab(Language.getText("spieler"), playersFrame);
			Tab tabCoaches = new Tab(Language.getText("trainer"), coachesFrame);

			tabPane.getTabs().addAll(tabTeam, tabPlayers, tabCoaches);

			centerPane.setCenter(tabPane);

			return centerPane;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected Pane buildBottomPane() {
		GridPane bottomPane = new GridPane();
		
		bottomPane.setPadding(new Insets(20));
		bottomPane.setHgap(20);
		bottomPane.setVgap(20);
		
		ColumnConstraints bottomPaneCol1 = new ColumnConstraints();
		bottomPaneCol1.setPercentWidth(50);
		ColumnConstraints bottomPaneCol2 = new ColumnConstraints();
		bottomPaneCol2.setPercentWidth(50);
		bottomPane.getColumnConstraints().addAll(bottomPaneCol1, bottomPaneCol2);
		
		Button btnResetTeam = new Button(Language.getText("reset"));
		btnResetTeam.setMaxWidth(Double.MAX_VALUE);
		btnResetTeam.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(team != null) {
					team.reset();
					ObservableList<Tab> allTabs = tabPane.getTabs();
					for(Tab tab: allTabs)
						((Resetable)tab.getContent()).reset();
				}
			}
		});
		
		bottomPane.add(btnResetTeam, 0, 0,2,1);
		return bottomPane;
	}

	private Team loadTeamFromTeamWrapper(TeamWrapper teamWrapper) throws IOException {
		try {
			return FileHandling.getFileHandling().loadTFromFile(teamWrapper.getsPathConfigFile(), Team.class);
		} catch (FileDidNotExistException e1) {
			ErrorMsg.showErrorMessage(109);
		}

		return null;
	}
}
