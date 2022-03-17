/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import java.io.IOException;
import java.util.Optional;

import exceptions.FileDidNotExistException;
import football.League;
import football.Team;
import football.TeamWrapper;
import gui.dialogs.DialogNewMatch;
import gui.frames.ControlFrame;
import javafx.event.ActionEvent;
import javafx.util.Pair;
import language.Language;
import message.ErrorMsg;
import system.FileHandling;

public class ButtonStartMatch extends ControlButton{
	private TeamWrapper selectedTeam;
	public ButtonStartMatch(ControlFrame controlFrame) {
		super(Language.getText("starteSpiel"), controlFrame);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == this) {
			try {
				Team teamHome = askForTeam(Language.getText("waehle_heim"));
				if(teamHome != null) {
					Team teamGuest = askForTeam(Language.getText("waehle_gast"));
					if(teamGuest != null) {
						controlFrame.startMatch(teamHome, teamGuest);
					}else {
						ErrorMsg.showErrorMessage(202);
					}
				}else {
					ErrorMsg.showErrorMessage(202);
				}
			} catch (IOException e) {
				ErrorMsg.showErrorMessage(109);
			}
		}
	}
	
	public Team askForTeam(String sHeaderText) throws IOException {
		Optional<Pair<League, TeamWrapper>> result = new DialogNewMatch(sHeaderText, controlFrame.getSettings()).showAndWait();
		result.ifPresent(selected -> {
			selectedTeam = selected.getValue();
		});
		
		if(selectedTeam == null)
			return null;
		
		return loadTeamFromTeamWrapper(selectedTeam);
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
