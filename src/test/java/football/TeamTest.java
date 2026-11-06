/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import football.person.Player;
import system.Defaults;
import system.FileHandling;

class TeamTest {

	@TempDir
	Path tempDir;

	private static Team newTeam() {
		return new Team("1. FFC Turbine Potsdam", new ArrayList<Player>(), null, null, "leagues/x/Potsdam/Potsdam.json");
	}

	@Test
	void teamFileFromVersion1UsesFullNameAndDefaultColor() throws Exception {
		Path file = tempDir.resolve("SCSand.json");
		Files.write(file, ("{\"sTeamName\": \"SC Sand\", \"lPlayers\": [], \"lCoaches\": [], "
				+ "\"sPathToLogo\": null, \"sOwnPath\": \"leagues/x/SCSand/SCSand.json\"}").getBytes(StandardCharsets.UTF_8));

		Team team = FileHandling.getFileHandling().loadTFromFile(file.toString(), Team.class);

		assertNull(team.getsShortName());
		assertNull(team.getsColor());
		assertEquals("SC Sand", team.getDisplayName());
		assertEquals(Defaults.TEAM_COLOR, team.getDisplayColor());
	}

	@Test
	void shortNameIsTrimmedAndBlankRemovesIt() {
		Team team = newTeam();

		team.setsShortName("  Potsdam ");
		assertEquals("Potsdam", team.getDisplayName());

		team.setsShortName("   ");
		assertNull(team.getsShortName());
		assertEquals("1. FFC Turbine Potsdam", team.getDisplayName());
	}

	@Test
	void onlyHexColorsAreAccepted() {
		Team team = newTeam();

		team.setsColor("#0C1D63");
		assertEquals("#0c1d63", team.getDisplayColor());

		team.setsColor("blue");
		assertNull(team.getsColor());
		assertEquals(Defaults.TEAM_COLOR, team.getDisplayColor());
	}

	@Test
	void shortNameAndColorAreSavedAndLoaded() throws Exception {
		Team team = newTeam();
		team.setsShortName("Potsdam");
		team.setsColor("#c42032");
		String file = tempDir.resolve("Potsdam.json").toString();

		FileHandling.getFileHandling().saveTToFile(team, file);
		Team loaded = FileHandling.getFileHandling().loadTFromFile(file, Team.class);

		assertEquals("Potsdam", loaded.getDisplayName());
		assertEquals("#c42032", loaded.getDisplayColor());
	}
}
