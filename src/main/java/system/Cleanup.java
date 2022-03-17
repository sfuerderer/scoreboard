/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

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

	public void cleanLeagueFolder() throws IOException, FileNotCreatedException {
		List<League> leagues = Load.getLoad().loadLeagues();
		List<League> leaguesExisting = new ArrayList<League>();

		Files.list(new File("leagues").toPath()).forEach(path -> {
			File file = path.toFile();
			if (file.isDirectory()) {
				boolean leagueExist = false;
				for (League league : leagues) {
					if (file.getName().equals(league.getsFolderName())) {
						leaguesExisting.add(league);
						leagueExist = true;
						break;
					}
				}
				if (!leagueExist)
					try {
						FileUtils.deleteDirectory(file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
		
		List<League> leaguesToRemove = new ArrayList<League>();
		for(League league: leagues) {
			if(!leaguesExisting.contains(league)) {
				leaguesToRemove.add(league);
			}
		}
		leagues.removeAll(leaguesToRemove);
		FileHandling.getFileHandling().saveTToFile(leagues, Defaults.S_LEAGUES_FILE_NAME);
	}

	public void cleanPlayerImagesFolder(Team team) {
		File playerFolder = new File(team.getPathToFolder() + "/playerImages/");
		if (!playerFolder.exists())
			return;
		File[] allFiles = playerFolder.listFiles();
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
