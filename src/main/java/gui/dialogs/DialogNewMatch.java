/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.dialogs;

import java.io.IOException;

import exceptions.FileNotCreatedException;
import football.League;
import football.TeamWrapper;
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
import message.ErrorMsg;
import system.Load;
import system.settings.Settings;

public class DialogNewMatch extends Dialog<Pair<League, TeamWrapper>> {
	
	private TeamWrapper selectedTeam = null;
	private League selectedLeague = null;

	public DialogNewMatch(String sHeaderText, Settings sSettings) {
		setTitle(Language.getText("neues_spiel"));

		setHeaderText(sHeaderText);
		
		setX(sSettings.mainFrame.iStartXPosition + sSettings.mainFrame.iWidth + 10);
		
		// Set the button types.
		ButtonType saveButton = new ButtonType("Speichern", ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(saveButton);

		// Create the chooserButtons
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 20, 10, 10));

		Label lbLeague = new Label(Language.getText("liga"));
		Label lbTeam = new Label(Language.getText("mannschaft"));

		try {
			ComboBox<TeamWrapper> cbTeam = new ComboBox<TeamWrapper>(FXCollections.observableArrayList());
			cbTeam.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TeamWrapper>() {
				@Override
				public void changed(ObservableValue<? extends TeamWrapper> observable, TeamWrapper oldValue, TeamWrapper newValue) {
					selectedTeam = newValue;
				}

			});
			
			ComboBox<League> cbLeague = new ComboBox<League>(FXCollections.observableArrayList(Load.getLoad().loadLeagues()));
			cbLeague.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<League>() {
				@Override
				public void changed(ObservableValue<? extends League> observable, League oldValue, League newValue) {
					cbTeam.setItems(FXCollections.observableArrayList(newValue.getlTeams()));
					selectedLeague = newValue;
				}

			});
			
			grid.add(lbLeague, 0, 0);
			grid.add(cbLeague, 1, 0);
			grid.add(lbTeam, 0, 1);
			grid.add(cbTeam, 1, 1);
		} catch (IOException e) {
			ErrorMsg.showErrorMessage(107);
		} catch (FileNotCreatedException e) {
			ErrorMsg.showErrorMessage(108);
		}


		getDialogPane().setContent(grid);

		setResultConverter(dialogButton -> {
			if (dialogButton == saveButton) {
				return new Pair<>(selectedLeague, selectedTeam);
			}
			return null;
		});

		initStyle(StageStyle.DECORATED);
	}

}
