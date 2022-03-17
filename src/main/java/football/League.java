/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class League {
	private SimpleStringProperty sLeagueName;
	@Expose
	private String sLeagueNameGson;
	@Expose
	private String sFolderName;
	@Expose
	private List<TeamWrapper> lTeams;

	public League(String sLeagueName, String sFolderName, List<TeamWrapper> lTeams) {
		super();
		this.sLeagueName = new SimpleStringProperty(sLeagueName);
		this.sLeagueNameGson = sLeagueName;
		this.sFolderName = sFolderName;
		this.lTeams = lTeams;
	}

	public League(String sLeagueName, String sFolderName) {
		this(sLeagueName, sFolderName, new ArrayList<TeamWrapper>());
	}

	/**
	 * Returns the sLeagueName.
	 *
	 * @return the sLeagueName
	 */
	public String getsLeagueName() {
		return sLeagueName.get();
	}

	/**
	 * Sets the sLeagueName to the new value.
	 *
	 * @param sLeagueName the sLeagueName to set
	 */
	public void setsLeagueName(String sLeagueName) {
		this.sLeagueName.set(sLeagueName);
		this.sLeagueNameGson = sLeagueName;
	}
	
	public StringProperty sLeagueNameProperty() {
        return sLeagueName;
    }

	/**
	 * Returns the sLeagueNameGson.
	 *
	 * @return the sLeagueNameGson
	 */
	public String getsLeagueNameGson() {
		return sLeagueNameGson;
	}

	/**
	 * Sets the sLeagueNameGson to the new value.
	 *
	 * @param sLeagueNameGson the sLeagueNameGson to set
	 */
	public void setsLeagueNameGson(String sLeagueNameGson) {
		this.sLeagueNameGson = sLeagueNameGson;
		this.sLeagueName = new SimpleStringProperty(sLeagueNameGson);
	}

	/**
	 * Returns the sFolderName.
	 *
	 * @return the sFolderName
	 */
	public String getsFolderName() {
		return sFolderName;
	}

	/**
	 * Sets the sFolderName to the new value.
	 *
	 * @param sFolderName the sFolderName to set
	 */
	public void setsFolderName(String sFolderName) {
		this.sFolderName = sFolderName;
	}

	/**
	 * Returns the lTeams.
	 *
	 * @return the lTeams
	 */
	public List<TeamWrapper> getlTeams() {
		return lTeams;
	}

	/**
	 * Sets the lTeams to the new value.
	 *
	 * @param lTeams the lTeams to set
	 */
	public void setlTeams(List<TeamWrapper> lTeams) {
		this.lTeams = lTeams;
	}
	
	public void addTeam(TeamWrapper newTeam) {
		lTeams.add(newTeam);
	}
	
	public void callAfterDeserialization() {
		this.sLeagueName = new SimpleStringProperty(sLeagueNameGson);
	}

	@Override
	public String toString() {
		return sLeagueNameGson;
	}
}
