/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import football.match.Halftime;
import football.person.Player;
import language.Languages;
import language.Texts;

class LeagueTest {

	private static final List<League> LEAGUES = Arrays.asList(
			new League("Flyeralarm Frauen-Bundesliga", "FlyeralarmFrauenBundesliga"),
			new League("Testspiele", "Testspiele"));

	private static Team teamAt(String sPath) {
		return new Team("Team", new ArrayList<Player>(), null, null, sPath);
	}

	@Test
	void leagueIsFoundByTheFolderOfTheTeamFile() {
		assertEquals("Testspiele",
				League.findLeagueNameOf(teamAt("leagues/Testspiele/FCVendenheim/FCVendenheim.json"), LEAGUES));
		assertEquals("Flyeralarm Frauen-Bundesliga",
				League.findLeagueNameOf(teamAt("leagues/FlyeralarmFrauenBundesliga/SGSEssen/SGSEssen.json"), LEAGUES));
	}

	@Test
	void windowsPathsAreFound() {
		assertEquals("Testspiele",
				League.findLeagueNameOf(teamAt("leagues\\Testspiele\\FCVendenheim\\FCVendenheim.json"), LEAGUES));
	}

	@Test
	void unknownLeagueOrMissingPathGivesNull() {
		assertNull(League.findLeagueNameOf(teamAt("leagues/Pokal/FCX/FCX.json"), LEAGUES));
		assertNull(League.findLeagueNameOf(teamAt(null), LEAGUES));
		assertNull(League.findLeagueNameOf(teamAt("FCX.json"), LEAGUES));
		assertNull(League.findLeagueNameOf(null, LEAGUES));
	}

	@Test
	void everyHalftimeHasTextInAllLanguages() throws Exception {
		try (Reader reader = new InputStreamReader(LeagueTest.class.getResourceAsStream("/config/language.json"),
				StandardCharsets.UTF_8)) {
			Texts texts = new Gson().fromJson(reader, Texts.class);
			for (Halftime halftime : Halftime.values())
				for (Languages language : Languages.values())
					assertEquals(true, texts.getTextInLanguage(language, halftime.getsLanguageKey()) != null,
							halftime + " " + language);
		}
	}
}
