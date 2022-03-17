/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football;

import com.google.gson.annotations.Expose;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TeamWrapper {
	private SimpleStringProperty sTeamName;
	@Expose
	private String sTeamNameGson;
	@Expose
	private String sFolderName;
	@Expose
	private String sPathConfigFile;


	public TeamWrapper(String sTeamName, String sFolderName, String sPathConfigFile) {
		super();
		this.sTeamName = new SimpleStringProperty(sTeamName);
		this.sTeamNameGson = sTeamName;
		this.sFolderName = sFolderName;
		this.sPathConfigFile = sPathConfigFile;
	}

	/**
	 * Returns the sLeagueName.
	 *
	 * @return the sLeagueName
	 */
	public String getsTeamName() {
		return sTeamName.get();
	}

	/**
	 * Sets the sLeagueName to the new value.
	 *
	 * @param sLeagueName the sLeagueName to set
	 */
	public void setsTeamName(String sLeagueName) {
		this.sTeamName.set(sLeagueName);
		this.sTeamNameGson = sLeagueName;
	}
	
	public StringProperty sTeamNameProperty() {
        return sTeamName;
    }

	/**
	 * Returns the sLeagueNameGson.
	 *
	 * @return the sLeagueNameGson
	 */
	public String getsTeamNameGson() {
		return sTeamNameGson;
	}

	/**
	 * Sets the sLeagueNameGson to the new value.
	 *
	 * @param sLeagueNameGson the sLeagueNameGson to set
	 */
	public void setsTeamNameGson(String sLeagueNameGson) {
		this.sTeamNameGson = sLeagueNameGson;
		this.sTeamName = new SimpleStringProperty(sLeagueNameGson);
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
	 * Returns the sPathConfigFile.
	 *
	 * @return the sPathConfigFile
	 */
	public String getsPathConfigFile() {
		return sPathConfigFile;
	}

	/**
	 * Sets the sPathConfigFile to the new value.
	 *
	 * @param sPathConfigFile the sPathConfigFile to set
	 */
	public void setsPathConfigFile(String sPathConfigFile) {
		this.sPathConfigFile = sPathConfigFile;
	}

	public void callAfterDeserialization() {
		this.sTeamName = new SimpleStringProperty(sTeamNameGson);
	}
	
	@Override
	public String toString() {
		return sTeamNameGson;
	}
}
