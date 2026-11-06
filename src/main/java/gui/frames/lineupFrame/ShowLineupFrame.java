/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.lineupFrame;

import java.util.List;

import football.Team;
import football.match.Match;
import football.match.Teams;
import football.person.Coach;
import football.person.Player;
import gui.GUIControlling;
import gui.frames.BasicFrame;
import gui.frames.ControlFrame;
import gui.frames.panes.PaneLineupComplete;
import gui.frames.panes.PanePersonIntro;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import language.Language;
import message.ErrorMsg;
import system.Main;
import system.settings.Settings;

public class ShowLineupFrame extends BasicFrame {

	private TabPane tabPane;

	public ShowLineupFrame(Settings sSettings, GUIControlling guiControlling, ControlFrame controlFrame) {
		super(sSettings, guiControlling, "aufstellung", controlFrame, 400, 500);
	}

	@Override
	protected Pane buildCenterPane(Object defaultParams) {
		BorderPane centerPane = new BorderPane();

		tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		ShowLineupFrameTeam homeTeam = new ShowLineupFrameTeam(Teams.HOME, (ControlFrame) defaultParams);
		ShowLineupFrameTeam guestTeam = new ShowLineupFrameTeam(Teams.GUEST, (ControlFrame) defaultParams);

		Tab tabTeam = new Tab(Language.getText("heimmannschaft"), homeTeam);
		Tab tabPlayers = new Tab(Language.getText("gastmannschaft"), guestTeam);

		tabPane.getTabs().addAll(tabTeam, tabPlayers);

		centerPane.setCenter(tabPane);

		return centerPane;
	}

	@Override
	protected Pane buildBottomPane() {
		BorderPane bottomPane = new BorderPane();
		bottomPane.setPadding(new Insets(20));

		VBox buttonContainer = new VBox();
		buttonContainer.setSpacing(10);

		Button btnShowCompleteLineup = new Button(Language.getText("komplette_aufstellung"));
		btnShowCompleteLineup.setMaxWidth(Double.MAX_VALUE);
		btnShowCompleteLineup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Match match = Main.getMatch();
				switch (tabPane.getSelectionModel().getSelectedIndex()) {
				case 0:
					changePaneOnScoreboardComplete(match.getlLineup().getlHomePlayersOnField(),
							match.gettTeamHome(), match.gettTeamHome().getHeadCoach());
					break;
				case 1:
					changePaneOnScoreboardComplete(match.getlLineup().getlGuestPlayersOnField(),
							match.gettTeamGuest(), match.gettTeamGuest().getHeadCoach());
					break;
				}
			}
		});

		Button btnShowCoach = new Button(Language.getText("trainer_anzeigen"));
		btnShowCoach.setMaxWidth(Double.MAX_VALUE);
		btnShowCoach.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Match match = Main.getMatch();
				switch (tabPane.getSelectionModel().getSelectedIndex()) {
				case 0:
					if (match.gettTeamHome().getlCoaches().size() > 0)
						changePaneOnScoreboardCoach(match.gettTeamHome().getHeadCoach());
					else
						ErrorMsg.showErrorMessage(210);
					break;
				case 1:
					if (match.gettTeamGuest().getlCoaches().size() > 0)
						changePaneOnScoreboardCoach(match.gettTeamGuest().getHeadCoach());
					else
						ErrorMsg.showErrorMessage(210);
					break;
				}
			}
		});

		buttonContainer.getChildren().addAll(btnShowCoach, btnShowCompleteLineup);

		bottomPane.setCenter(buttonContainer);

		return bottomPane;
	}

	private void changePaneOnScoreboardComplete(List<Player> playersToShow, Team team, Coach coach) {
		guiControlling.changeMainScoreboardPane(
				new PaneLineupComplete(guiControlling.getRootPaneMainScoreboard(), playersToShow, team, coach));
	}

	private void changePaneOnScoreboardCoach(Coach coachToShow) {
		guiControlling.changeMainScoreboardPane(
				new PanePersonIntro(guiControlling.getRootPaneMainScoreboard(), coachToShow));
	}
}
