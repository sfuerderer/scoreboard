/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import exceptions.FileNotCreatedException;
import exceptions.LeagueNotCreatedException;
import football.League;
import football.Team;
import football.TeamWrapper;
import football.person.Player;
import gui.GUIControlling;
import gui.frames.teamFrame.TeamFrame;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import language.Language;
import message.ErrorMsg;
import system.Cleanup;
import system.Defaults;
import system.FileHandling;
import system.Load;
import system.settings.Settings;

public class LeagueFrame extends BasicFrame {
	private TextField tfNewLeagueName,tfNewTeamName;

	public LeagueFrame(Settings sSettings, GUIControlling guiControlling) {
		super(sSettings, guiControlling, "ligen", null);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Pane buildCenterPane(Object defaultParams) {
		try {
			// Clean up first, so the list below matches what is saved on disk
			Cleanup.getCleanup().cleanLeagueFolder();
			List<League> leagues = Load.getLoad().loadLeagues();
			List<TeamWrapper> teams = new ArrayList<TeamWrapper>();
			for (League league : leagues)
				league.callAfterDeserialization();

			ObservableList<League> tableDataLeagues = FXCollections.observableArrayList(leagues);
			ObservableList<TeamWrapper> tableDataTeams = FXCollections.observableArrayList(teams);

			// Create the center Pane
			GridPane centerPane = new GridPane();
			centerPane.setPadding(new Insets(20));
			centerPane.setHgap(20);
			centerPane.setVgap(20);

			ColumnConstraints bottomPaneCol1 = new ColumnConstraints();
			bottomPaneCol1.setPercentWidth(25);
			ColumnConstraints bottomPaneCol2 = new ColumnConstraints();
			bottomPaneCol2.setPercentWidth(25);
			ColumnConstraints bottomPaneCol3 = new ColumnConstraints();
			bottomPaneCol3.setPercentWidth(25);
			ColumnConstraints bottomPaneCol4 = new ColumnConstraints();
			bottomPaneCol4.setPercentWidth(25);
			centerPane.getColumnConstraints().addAll(bottomPaneCol1, bottomPaneCol2, bottomPaneCol3, bottomPaneCol4);

			TableView<League> leagueTable = new TableView<League>();
			TableView<TeamWrapper> teamTable = new TableView<TeamWrapper>();

			TableColumn<League, String> leagueFirstColum = new TableColumn<League, String>(
					Language.getText("ligaName"));
			leagueFirstColum.setMinWidth(100);
			leagueFirstColum.setCellValueFactory(new PropertyValueFactory<League, String>("sLeagueName"));

			leagueTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			leagueFirstColum.setMaxWidth(1f * Integer.MAX_VALUE * 100); // 50% width

			TableColumn<TeamWrapper, String> teamFirstColum = new TableColumn<TeamWrapper, String>(
					Language.getText("team"));
			teamFirstColum.setMinWidth(100);
			teamFirstColum.setCellValueFactory(new PropertyValueFactory<TeamWrapper, String>("sTeamName"));

			teamTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			teamFirstColum.setMaxWidth(1f * Integer.MAX_VALUE * 100); // 50% width

			leagueTable.setItems(tableDataLeagues);
			leagueTable.getColumns().addAll(leagueFirstColum);

			teamTable.setItems(tableDataTeams);
			teamTable.getColumns().addAll(teamFirstColum);

			tfNewLeagueName = new TextField();
			tfNewLeagueName.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					addNewLeague(tableDataLeagues,leagues);
				}
			});

			tfNewTeamName = new TextField();
			tfNewTeamName.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					League currentSelectedLeague = leagueTable.getSelectionModel().getSelectedItem();
					addNewTeam(tableDataTeams,currentSelectedLeague,leagues);
				}
			});

			// update team list if the league changes
			leagueTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
				if (newSelection != null) {
					List<TeamWrapper> lTeams = newSelection.getlTeams();
					for (TeamWrapper team : lTeams)
						team.callAfterDeserialization();
					// teamTable.getSelectionModel().clearSelection();
					tableDataTeams.clear();
					tableDataTeams.addAll(lTeams);
				}
			});

			// Detect double click on an team
			teamTable.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
						new TeamFrame(sSettings, guiControlling, teamTable.getSelectionModel().getSelectedItem())
								.show();
					}
				}
			});

			// Creates the add button for the leagues.
			Button btnAdd = new Button(Language.getText("hinzufuegen"));
			btnAdd.setMaxWidth(Double.MAX_VALUE);
			btnAdd.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					addNewLeague(tableDataLeagues,leagues);
				}
			});

			// Creates the add button for the teams.
			Button btnAddTeam = new Button(Language.getText("hinzufuegen"));
			btnAddTeam.setMaxWidth(Double.MAX_VALUE);
			btnAddTeam.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					League currentSelectedLeague = leagueTable.getSelectionModel().getSelectedItem();
					addNewTeam(tableDataTeams,currentSelectedLeague,leagues);
				}
			});

			centerPane.add(tfNewLeagueName, 0, 0);
			centerPane.add(btnAdd, 1, 0);
			centerPane.add(leagueTable, 0, 1, 2, 1);
			centerPane.add(tfNewTeamName, 2, 0);
			centerPane.add(btnAddTeam, 3, 0);
			centerPane.add(teamTable, 2, 1, 2, 1);

			return centerPane;
		} catch (FileNotCreatedException e) {
			ErrorMsg.showErrorMessage(107, e.getMessage());
		} catch (IOException e) {
			ErrorMsg.showErrorMessage(107, e.getMessage());
		}

		return null;
	}

	@Override
	protected Pane buildBottomPane() {
		return null;
	}

	private void addNewLeague(ObservableList<League> tableDataLeagues,List<League> leagues) {
		try {
			League newLeague = createNewLeague(tfNewLeagueName.getText(), leagues);
			leagues.add(newLeague);
			saveLeagues(leagues);
			tableDataLeagues.add(newLeague);
			tfNewLeagueName.clear();
		} catch (LeagueNotCreatedException e) {
			ErrorMsg.showErrorMessage(108);
		} catch (FileNotCreatedException e) {
			ErrorMsg.showErrorMessage(108, e.getMessage());
		} catch (IOException e) {
			ErrorMsg.showErrorMessage(108, e.getMessage());
		}
	}

	private void addNewTeam(ObservableList<TeamWrapper> tableDataTeams, League currentSelectedLeague, List<League> leagues) {
		if (currentSelectedLeague != null) {
			try {
				TeamWrapper newTeam = createNewTeam(tfNewTeamName.getText(),
						currentSelectedLeague.getsFolderName());
				currentSelectedLeague.addTeam(newTeam);
				saveLeagues(leagues);
				tableDataTeams.add(newTeam);
				tfNewTeamName.clear();
			} catch (LeagueNotCreatedException e) {
				ErrorMsg.showErrorMessage(108);
			} catch (FileNotCreatedException e) {
				ErrorMsg.showErrorMessage(108, e.getMessage());
			} catch (IOException e) {
				ErrorMsg.showErrorMessage(108, e.getMessage());
			}

		} else {
			ErrorMsg.showErrorMessage(200);
		}
	}

	private League createNewLeague(String sLeagueName, List<League> leagues) throws LeagueNotCreatedException {
		String folderName = sLeagueName.replaceAll("\\W+", "");
		if (folderName.isEmpty())
			throw new LeagueNotCreatedException();
		for (League league : leagues)
			if (folderName.equals(league.getsFolderName()))
				throw new LeagueNotCreatedException();

		// An existing folder that belongs to no league is reused (it is no longer deleted on startup)
		File fullPath = new File("leagues/" + folderName);
		if (!fullPath.mkdirs() && !fullPath.isDirectory())
			throw new LeagueNotCreatedException();

		return new League(sLeagueName, folderName);
	}

	private TeamWrapper createNewTeam(String sTeamName, String leagueFolder)
			throws LeagueNotCreatedException, FileNotCreatedException, IOException {
		String folderName = sTeamName.replaceAll("\\W+", "");
		File fullPath = new File("leagues/" + leagueFolder + "/" + folderName);

		if (!fullPath.mkdirs())
			throw new LeagueNotCreatedException();

		String path = "leagues/" + leagueFolder + "/" + folderName + "/" + folderName + ".json";
		Team team = new Team(sTeamName, new ArrayList<Player>(), null, null, path);
		saveTeam(team, path);
		return new TeamWrapper(sTeamName, folderName, path);
	}

	private void saveLeagues(List<League> lLeagues) throws FileNotCreatedException, IOException {
		FileHandling.getFileHandling().saveTToFile(lLeagues, Defaults.S_LEAGUES_FILE_NAME);

	}

	private void saveTeam(Team team, String path) throws FileNotCreatedException, IOException {
		FileHandling.getFileHandling().saveTToFile(team, path);

	}
}
