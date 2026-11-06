/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import exceptions.FileNotCreatedException;
import football.League;
import football.TeamWrapper;
import football.match.Match;

/**
 * Deletes leagues and teams: first the entry is removed from the leagues file,
 * then the folder (JSON + images) is deleted. If deleting the folder fails only
 * an unreferenced folder is left behind, which is harmless.
 */
public class LeagueDeletion {

	private static LeagueDeletion leagueDeletion;

	private final File fLeaguesFolder;
	private final String sLeaguesFile;

	LeagueDeletion(File fLeaguesFolder, String sLeaguesFile) {
		this.fLeaguesFolder = fLeaguesFolder;
		this.sLeaguesFile = sLeaguesFile;
	}

	public static LeagueDeletion getLeagueDeletion() {
		if (leagueDeletion == null)
			leagueDeletion = new LeagueDeletion(new File("leagues"), Defaults.S_LEAGUES_FILE_NAME);
		return leagueDeletion;
	}

	/**
	 * Returns true if the team is the home or guest team of the match.
	 */
	public boolean isTeamInUse(TeamWrapper team, Match match) {
		if (match == null)
			return false;
		String sHome = match.gettTeamHome() == null ? null : match.gettTeamHome().getsOwnPath();
		String sGuest = match.gettTeamGuest() == null ? null : match.gettTeamGuest().getsOwnPath();
		return isTeamInUse(team, sHome, sGuest);
	}

	public boolean isLeagueInUse(League league, Match match) {
		for (TeamWrapper team : league.getlTeams())
			if (isTeamInUse(team, match))
				return true;
		return false;
	}

	boolean isTeamInUse(TeamWrapper team, String... sPathsInUse) {
		if (team == null || team.getsPathConfigFile() == null)
			return false;
		File teamFile = new File(team.getsPathConfigFile()).getAbsoluteFile().toPath().normalize().toFile();
		for (String sPath : sPathsInUse) {
			if (sPath != null
					&& teamFile.equals(new File(sPath).getAbsoluteFile().toPath().normalize().toFile()))
				return true;
		}
		return false;
	}

	public void deleteTeam(List<League> lLeagues, League league, TeamWrapper team)
			throws FileNotCreatedException, IOException {
		int iIndex = league.getlTeams().indexOf(team);
		if (iIndex < 0)
			return;
		league.getlTeams().remove(iIndex);
		try {
			FileHandling.getFileHandling().saveTToFile(lLeagues, sLeaguesFile);
		} catch (FileNotCreatedException | IOException e) {
			// Keep memory and file in sync
			league.getlTeams().add(iIndex, team);
			throw e;
		}
		deleteFolder(new File(new File(fLeaguesFolder, league.getsFolderName()), team.getsFolderName()),
				team.getsFolderName());
	}

	public void deleteLeague(List<League> lLeagues, League league) throws FileNotCreatedException, IOException {
		int iIndex = lLeagues.indexOf(league);
		if (iIndex < 0)
			return;
		lLeagues.remove(iIndex);
		try {
			FileHandling.getFileHandling().saveTToFile(lLeagues, sLeaguesFile);
		} catch (FileNotCreatedException | IOException e) {
			// Keep memory and file in sync
			lLeagues.add(iIndex, league);
			throw e;
		}
		deleteFolder(new File(fLeaguesFolder, league.getsFolderName()), league.getsFolderName());
	}

	/**
	 * Deletes the folder only if it lies inside the leagues folder. An empty or
	 * missing folder name would otherwise point at the leagues folder itself.
	 */
	private void deleteFolder(File folder, String sFolderName) throws IOException {
		if (sFolderName == null || sFolderName.isBlank())
			return;
		File canonicalFolder = folder.getCanonicalFile();
		File canonicalLeagues = fLeaguesFolder.getCanonicalFile();
		if (!canonicalFolder.toPath().startsWith(canonicalLeagues.toPath())
				|| canonicalFolder.equals(canonicalLeagues))
			return;
		if (canonicalFolder.isDirectory())
			FileUtils.deleteDirectory(canonicalFolder);
	}
}
