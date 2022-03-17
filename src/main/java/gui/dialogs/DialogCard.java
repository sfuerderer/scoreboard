/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import football.Cards;
import football.Team;
import football.match.Lineup;
import football.match.Match;
import football.person.Player;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import language.Language;
import system.Main;
import system.settings.Settings;

public class DialogCard extends Dialog<Pair<Player, Cards>> {

//	private Team selectedTeam = null;
	private Player selectedPlayer = null;
	private Cards selectedCard = null;

	public DialogCard(String sHeaderText, Settings sSettings) {
		setTitle(Language.getText("karte"));

		setHeaderText(sHeaderText);

		setX(sSettings.mainFrame.iStartXPosition + sSettings.mainFrame.iWidth + 10);
		
		getDialogPane().setMinWidth(400);

		Match match = Main.getMatch();

		// Set the button types.
		ButtonType saveButton = new ButtonType(Language.getText("anzeigen"), ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(saveButton);

		// Create the chooserButtons
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 20, 10, 10));

		Label lbTeam = new Label(Language.getText("mannschaft"));
		Label lbCardType = new Label(Language.getText("welche_karte"));
		Label lbPlayer = new Label(Language.getText("spieler"));

		List<Team> lTeams = new ArrayList<Team>();
		lTeams.add(match.gettTeamHome());
		lTeams.add(match.gettTeamGuest());

		List<Cards> lCards = new ArrayList<Cards>();
		lCards.add(Cards.YELLOW);
		lCards.add(Cards.RED);
		lCards.add(Cards.YELLOW_RED);

		ComboBox<Player> cbPlayers = new ComboBox<Player>(FXCollections.observableArrayList());
		cbPlayers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Player>() {
			@Override
			public void changed(ObservableValue<? extends Player> observable, Player oldValue, Player newValue) {
				selectedPlayer = newValue;
			}

		});

		ComboBox<Cards> cbCards = new ComboBox<Cards>(FXCollections.observableArrayList(lCards));
		cbCards.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Cards>() {
			@Override
			public void changed(ObservableValue<? extends Cards> observable, Cards oldValue, Cards newValue) {
				selectedCard = newValue;
			}

		});

		ComboBox<Team> cbTeam = new ComboBox<Team>(FXCollections.observableArrayList(lTeams));
		cbTeam.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Team>() {
			@Override
			public void changed(ObservableValue<? extends Team> observable, Team oldValue, Team newValue) {
				Lineup lineup = match.getlLineup();
				if(newValue.equals(match.gettTeamHome())) {					
					cbPlayers.setItems(FXCollections.observableArrayList(Stream.concat(lineup.getlHomePlayersOnField().stream(), lineup.getlHomePlayersOnExchange().stream()).collect(Collectors.toList())));
				}else if(newValue.equals(match.gettTeamGuest())) {
					cbPlayers.setItems(FXCollections.observableArrayList(Stream.concat(lineup.getlGuestPlayersOnField().stream(), lineup.getlGuestPlayersOnExchange().stream()).collect(Collectors.toList())));
				}else {
					cbPlayers.setItems(FXCollections.observableArrayList());
				}
			}

		});

		grid.add(lbTeam, 0, 0);
		grid.add(cbTeam, 1, 0);
		grid.add(lbCardType, 0, 1);
		grid.add(cbCards, 1, 1);
		grid.add(lbPlayer, 0, 2);
		grid.add(cbPlayers, 1, 2);

		getDialogPane().setContent(grid);

		setResultConverter(dialogButton -> {
			if (dialogButton == saveButton) {
				return new Pair<>(selectedPlayer, selectedCard);
			}
			return null;
		});

		initStyle(StageStyle.DECORATED);
	}

}
