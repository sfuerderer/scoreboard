/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import exceptions.FileNotCreatedException;
import football.League;
import football.Team;

public class Cleanup {

	private static Cleanup cleanup;

	private Cleanup() {

	}

	public static Cleanup getCleanup() {
		if (cleanup == null)
			cleanup = new Cleanup();
		return cleanup;
	}

	public void cleanFolderStructure() {
		try {
			checkBasicFolderStructureAndCorrect();
			cleanLeagueFolder();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotCreatedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Removes leagues from the leagues file whose folder no longer exists.
	 * Folders that are not listed in the leagues file are never deleted, because
	 * a missing or broken leagues file would otherwise wipe all league data.
	 */
	public void cleanLeagueFolder() throws IOException, FileNotCreatedException {
		List<League> leagues = Load.getLoad().loadLeagues();
		if (leagues.isEmpty())
			return;

		List<League> leaguesToRemove = new ArrayList<League>();
		for (League league : leagues) {
			File leagueFolder = new File("leagues/" + league.getsFolderName());
			if (league.getsFolderName() == null || !leagueFolder.isDirectory())
				leaguesToRemove.add(league);
		}

		if (leaguesToRemove.isEmpty())
			return;

		leagues.removeAll(leaguesToRemove);
		FileHandling.getFileHandling().saveTToFile(leagues, Defaults.S_LEAGUES_FILE_NAME);
	}

	public void cleanPlayerImagesFolder(Team team) {
		File playerFolder = new File(team.getPathToFolder() + "/playerImages/");
		if (!playerFolder.exists())
			return;
		File[] allFiles = playerFolder.listFiles();
		if (allFiles == null)
			return;
		for (File currentFile : allFiles)
			currentFile.delete();
	}
	
	private void checkBasicFolderStructureAndCorrect() {
		// Advertisment Folder
		File adFolder = new File("advertisment");
		if(!adFolder.exists())
			adFolder.mkdir();
		
		// Media Folder
		File mediaFolder = new File("media");
		if(!mediaFolder.exists())
			mediaFolder.mkdir();
	}

	public boolean removeFile(String pathToFile) {
		if (pathToFile == null)
			return false;
		File file = new File(pathToFile);
		if (!file.exists())
			return false;
		return file.delete();
	}
}
