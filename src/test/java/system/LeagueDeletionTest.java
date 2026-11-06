/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.google.gson.reflect.TypeToken;

import football.League;
import football.TeamWrapper;

class LeagueDeletionTest {

	@TempDir
	Path tempDir;

	private Path leaguesFolder;
	private String sLeaguesFile;
	private LeagueDeletion deletion;
	private List<League> lLeagues;
	private League league;
	private TeamWrapper teamA;
	private TeamWrapper teamB;

	@BeforeEach
	void setUp() throws Exception {
		leaguesFolder = tempDir.resolve("leagues");
		sLeaguesFile = leaguesFolder.resolve("leagues.json").toString();
		deletion = new LeagueDeletion(leaguesFolder.toFile(), sLeaguesFile);

		teamA = createTeam("Liga", "TeamA");
		teamB = createTeam("Liga", "TeamB");
		league = new League("Liga", "Liga");
		league.addTeam(teamA);
		league.addTeam(teamB);
		lLeagues = new ArrayList<League>();
		lLeagues.add(league);
		FileHandling.getFileHandling().saveTToFile(lLeagues, sLeaguesFile);
	}

	private TeamWrapper createTeam(String sLeagueFolder, String sTeamFolder) throws Exception {
		Path teamFolder = leaguesFolder.resolve(sLeagueFolder).resolve(sTeamFolder);
		Files.createDirectories(teamFolder.resolve("playerImages"));
		Path config = teamFolder.resolve(sTeamFolder + ".json");
		Files.writeString(config, "{}");
		Files.writeString(teamFolder.resolve("playerImages/1.png"), "x");
		return new TeamWrapper(sTeamFolder, sTeamFolder, config.toString());
	}

	private List<League> loadSaved() throws Exception {
		Type type = new TypeToken<Collection<League>>() {
		}.getType();
		return FileHandling.getFileHandling().loadTFromFile(sLeaguesFile, type);
	}

	@Test
	void deleteTeamRemovesEntryAndFolder() throws Exception {
		deletion.deleteTeam(lLeagues, league, teamA);

		assertFalse(Files.exists(leaguesFolder.resolve("Liga/TeamA")));
		assertTrue(Files.exists(leaguesFolder.resolve("Liga/TeamB")));
		List<League> saved = loadSaved();
		assertEquals(1, saved.get(0).getlTeams().size());
		assertEquals("TeamB", saved.get(0).getlTeams().get(0).getsFolderName());
	}

	@Test
	void deleteLeagueRemovesEntryAndAllTeamFolders() throws Exception {
		deletion.deleteLeague(lLeagues, league);

		assertFalse(Files.exists(leaguesFolder.resolve("Liga")));
		assertTrue(Files.exists(leaguesFolder));
		assertTrue(loadSaved().isEmpty());
	}

	@Test
	void emptyFolderNameDeletesNothing() throws Exception {
		League broken = new League("Broken", "");
		lLeagues.add(broken);

		deletion.deleteLeague(lLeagues, broken);

		assertTrue(Files.exists(leaguesFolder.resolve("Liga/TeamA")));
		assertEquals(1, loadSaved().size());
	}

	@Test
	void folderOutsideLeaguesIsNotDeleted() throws Exception {
		Files.createDirectories(tempDir.resolve("outside"));
		League escaping = new League("Escape", "../outside");
		lLeagues.add(escaping);

		deletion.deleteLeague(lLeagues, escaping);

		assertTrue(Files.exists(tempDir.resolve("outside")));
	}

	@Test
	void detectsTeamInUse() {
		String sOtherPath = new File("somewhere/else.json").getPath();
		assertTrue(deletion.isTeamInUse(teamA, teamA.getsPathConfigFile(), sOtherPath));
		assertTrue(deletion.isTeamInUse(teamA, null,
				leaguesFolder.resolve("Liga/./TeamA/TeamA.json").toString()));
		assertFalse(deletion.isTeamInUse(teamB, teamA.getsPathConfigFile(), null));
	}

	@Test
	void noMatchMeansNothingInUse() {
		assertFalse(deletion.isTeamInUse(teamA, (football.match.Match) null));
		assertFalse(deletion.isLeagueInUse(league, null));
	}
}
