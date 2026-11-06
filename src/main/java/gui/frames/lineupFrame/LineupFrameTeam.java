/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.lineupFrame;

import java.util.Collections;
import java.util.List;

import football.Team;
import football.match.Teams;
import football.person.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import language.Language;
import message.ErrorMsg;
import system.Main;

public class LineupFrameTeam extends GridPane {
	private ObservableList<Player> tableDataOnField, tableDataOnChange;

	@SuppressWarnings("unchecked")
	public LineupFrameTeam(Teams selectedTeam) {
		if (Main.getMatch() == null) {
			return;
		}
		Team team = getCurrentTeam(selectedTeam);

		tableDataOnField = FXCollections.observableArrayList(getPlayersOnField(selectedTeam));
		tableDataOnChange = FXCollections.observableArrayList(getPlayersOnChange(selectedTeam));

		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);

		ColumnConstraints bottomPaneCol1 = new ColumnConstraints();
		bottomPaneCol1.setPercentWidth(25);
		ColumnConstraints bottomPaneCol2 = new ColumnConstraints();
		bottomPaneCol2.setPercentWidth(25);
		ColumnConstraints bottomPaneCol3 = new ColumnConstraints();
		bottomPaneCol3.setPercentWidth(25);
		ColumnConstraints bottomPaneCol4 = new ColumnConstraints();
		bottomPaneCol4.setPercentWidth(25);
		getColumnConstraints().addAll(bottomPaneCol1, bottomPaneCol2, bottomPaneCol3, bottomPaneCol4);

		// Title rows use their natural height, the two list rows share the remaining space.
		// Percent heights would turn the fixed label heights into a minimum height of the whole grid
		// (e.g. 25px / 5% = 500px) and push the content out of the window with larger fonts (Windows).
		RowConstraints paneRow1 = new RowConstraints();
		RowConstraints paneRow2 = buildGrowingRow();
		RowConstraints paneRow3 = new RowConstraints();
		RowConstraints paneRow4 = buildGrowingRow();
		getRowConstraints().addAll(paneRow1, paneRow2, paneRow3, paneRow4);

		ScrollPane spAllPlayers = new ScrollPane();
		spAllPlayers.setHbarPolicy(ScrollBarPolicy.NEVER);
		spAllPlayers.setFitToWidth(true);
		spAllPlayers.setFitToHeight(true);
		spAllPlayers.setContent(buildAllPlayers(team, selectedTeam));

		TableView<Player> tableOnField = new TableView<Player>();

		TableColumn<Player, Integer> tableOnFieldFirstCol = new TableColumn<Player, Integer>(
				Language.getText("nummer"));
		tableOnFieldFirstCol.setCellValueFactory(new PropertyValueFactory<Player, Integer>("siNumber"));

		TableColumn<Player, String> tableOnFieldSecondCol = new TableColumn<Player, String>(
				Language.getText("vorname"));
		tableOnFieldSecondCol.setCellValueFactory(new PropertyValueFactory<Player, String>("sFirstname"));

		TableColumn<Player, String> tableOnFieldThirdCol = new TableColumn<Player, String>(
				Language.getText("nachname"));
		tableOnFieldThirdCol.setCellValueFactory(new PropertyValueFactory<Player, String>("sLastname"));

		tableOnField.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableOnFieldFirstCol.setMaxWidth(1f * Integer.MAX_VALUE * 10);
		tableOnFieldSecondCol.setMaxWidth(1f * Integer.MAX_VALUE * 45);
		tableOnFieldThirdCol.setMaxWidth(1f * Integer.MAX_VALUE * 45);

		tableOnField.setItems(tableDataOnField);
		tableOnField.getColumns().addAll(tableOnFieldFirstCol, tableOnFieldSecondCol, tableOnFieldThirdCol);

		TableView<Player> tableOnChange = new TableView<Player>();

		TableColumn<Player, Integer> tableOnChangeFirstCol = new TableColumn<Player, Integer>(
				Language.getText("nummer"));
		tableOnChangeFirstCol.setCellValueFactory(new PropertyValueFactory<Player, Integer>("siNumber"));

		TableColumn<Player, String> tableOnChangeSecondCol = new TableColumn<Player, String>(
				Language.getText("vorname"));
		tableOnChangeSecondCol.setCellValueFactory(new PropertyValueFactory<Player, String>("sFirstname"));

		TableColumn<Player, String> tableOnChangeThirdCol = new TableColumn<Player, String>(
				Language.getText("nachname"));
		tableOnChangeThirdCol.setCellValueFactory(new PropertyValueFactory<Player, String>("sLastname"));

		tableOnChange.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableOnChangeFirstCol.setMaxWidth(1f * Integer.MAX_VALUE * 10);
		tableOnChangeSecondCol.setMaxWidth(1f * Integer.MAX_VALUE * 45);
		tableOnChangeThirdCol.setMaxWidth(1f * Integer.MAX_VALUE * 45);

		tableOnChange.setItems(tableDataOnChange);
		tableOnChange.getColumns().addAll(tableOnChangeFirstCol, tableOnChangeSecondCol, tableOnChangeThirdCol);

		Label lbTitlePlayer = new Label(Language.getText("spieler"));
		lbTitlePlayer.getStyleClass().add("sf-label-title");
		Label lbTitleOnField = new Label(Language.getText("startaufstellung"));
		lbTitleOnField.getStyleClass().add("sf-label-title");
		Label lbTitleReplacement = new Label(Language.getText("ersatzbank"));
		lbTitleReplacement.getStyleClass().add("sf-label-title");

		add(lbTitlePlayer, 0, 0, 4, 1);
		add(spAllPlayers, 0, 1, 4, 1);
		add(lbTitleOnField, 0, 2, 2, 1);
		add(lbTitleReplacement, 2, 2, 2, 1);
		add(tableOnField, 0, 3, 2, 1);
		add(tableOnChange, 2, 3, 2, 1);
	}

	private RowConstraints buildGrowingRow() {
		RowConstraints row = new RowConstraints();
		row.setPrefHeight(0);
		row.setVgrow(Priority.ALWAYS);
		return row;
	}

	private VBox buildAllPlayers(Team team, Teams selectedTeam) {
		VBox allPlayer = new VBox();
		Collections.sort(team.getlPlayers());

		for (Player player : team.getlPlayers()) {
			GridPane gpPlayerLine = new GridPane();
			gpPlayerLine.setPadding(new Insets(5));
			gpPlayerLine.setHgap(5);
			gpPlayerLine.setVgap(10);

			ColumnConstraints gpPlayerLineCol1 = new ColumnConstraints();
			gpPlayerLineCol1.setPercentWidth(25);
			ColumnConstraints gpPlayerLineCol2 = new ColumnConstraints();
			gpPlayerLineCol2.setPercentWidth(25);
			ColumnConstraints gpPlayerLineCol3 = new ColumnConstraints();
			gpPlayerLineCol3.setPercentWidth(25);
			ColumnConstraints gpPlayerLineCol4 = new ColumnConstraints();
			gpPlayerLineCol4.setPercentWidth(25);

			gpPlayerLine.getColumnConstraints().addAll(gpPlayerLineCol1, gpPlayerLineCol2, gpPlayerLineCol3,
					gpPlayerLineCol4);

			Label lbPlayerName = new Label(
					player.getiNumber() + " - " + player.getsFirstname() + " " + player.getsLastname());

			Button btnAddOnField = new Button(Language.getText("startaufstellung"));
			btnAddOnField.setMaxWidth(Double.MAX_VALUE);
			btnAddOnField.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					addPlayerToField(player, selectedTeam);
				}
			});

			Button btnAddOnChange = new Button(Language.getText("ersatzbank"));
			btnAddOnChange.setMaxWidth(Double.MAX_VALUE);
			btnAddOnChange.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					addPlayerToChange(player, selectedTeam);
				}
			});

			gpPlayerLine.add(lbPlayerName, 0, 0, 2, 1);
			gpPlayerLine.add(btnAddOnField, 2, 0);
			gpPlayerLine.add(btnAddOnChange, 3, 0);

			allPlayer.getChildren().add(gpPlayerLine);
		}

		return allPlayer;
	}

	private void addPlayerToField(Player player, Teams team) {
		if (Main.getMatch().getlLineup().isPlayerOnField(team, player)) {
			Main.getMatch().getlLineup().removePlayerOnField(team, player);
			tableDataOnField.remove(player);
		} else {
			if (Main.getMatch().getlLineup().countOnField(team) >= 11) {
				ErrorMsg.showErrorMessage(203);
				return;
			}

			if (Main.getMatch().getlLineup().isPlayerOnChange(team, player)) {
				Main.getMatch().getlLineup().removePlayerOnExchange(team, player);
				tableDataOnChange.remove(player);
			}
			Main.getMatch().getlLineup().addPlayerOnField(team, player);
			tableDataOnField.add(player);
		}
	}

	private void addPlayerToChange(Player player, Teams team) {
		if (Main.getMatch().getlLineup().isPlayerOnChange(team, player)) {
			Main.getMatch().getlLineup().removePlayerOnExchange(team, player);
			tableDataOnChange.remove(player);
		} else {
			if (Main.getMatch().getlLineup().isPlayerOnField(team, player)) {
				Main.getMatch().getlLineup().removePlayerOnField(team, player);
				tableDataOnField.remove(player);
			}
			Main.getMatch().getlLineup().addPlayerOnExchange(team, player);
			tableDataOnChange.add(player);
		}
	}

	private Team getCurrentTeam(Teams team) {
		switch (team) {
		case HOME:
			return Main.getMatch().gettTeamHome();
		case GUEST:
			return Main.getMatch().gettTeamGuest();
		default:
			throw new IllegalArgumentException("Unexpected value: " + team);
		}
	}

	private List<Player> getPlayersOnField(Teams team) {
		switch (team) {
		case HOME:
			return Main.getMatch().getlLineup().getlHomePlayersOnField();
		case GUEST:
			return Main.getMatch().getlLineup().getlGuestPlayersOnField();
		default:
			throw new IllegalArgumentException("Unexpected value: " + team);
		}
	}

	private List<Player> getPlayersOnChange(Teams team) {
		switch (team) {
		case HOME:
			return Main.getMatch().getlLineup().getlHomePlayersOnExchange();
		case GUEST:
			return Main.getMatch().getlLineup().getlGuestPlayersOnExchange();
		default:
			throw new IllegalArgumentException("Unexpected value: " + team);
		}
	}
}
