/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football.match;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import football.person.Player;
import football.person.Position;
import language.Languages;
import language.Texts;

class MatchEventsHelpersTest {

	@Test
	void minuteIsCountedLikeInFootball() {
		assertEquals(1, MatchMinute.of("00:00"));
		assertEquals(1, MatchMinute.of("00:59"));
		assertEquals(68, MatchMinute.of("67:12"));
		assertEquals(46, MatchMinute.of("45:00"));
		assertEquals(113, MatchMinute.of("112:30"));
		assertEquals("68'", MatchMinute.format("67:12"));
	}

	@Test
	void unreadableClockGivesNoMinute() {
		assertEquals(0, MatchMinute.of(null));
		assertEquals(0, MatchMinute.of(""));
		assertEquals(0, MatchMinute.of("ab:cd"));
		assertEquals("", MatchMinute.format(null));
	}

	@Test
	void lineupShowsGoalkeeperFirstThenByNumber() {
		Player striker = new Player("Lena", "Hofmann", 9, Position.STRIKER);
		Player defender = new Player("Sara", "Wolf", 3, Position.DEFENDER);
		Player goalkeeper = new Player("Julia", "Klein", 1, Position.GOALKEEPER);
		Player noNumber = new Player("Eva", "Lang", -1, Position.MIDFIELD);

		List<Player> sorted = Lineup.sortedForScoreboard(Arrays.asList(striker, noNumber, defender, goalkeeper));

		assertEquals(Arrays.asList(goalkeeper, defender, striker, noNumber), sorted);
	}

	@Test
	void eventTextsExistInAllLanguages() throws Exception {
		try (Reader reader = new InputStreamReader(
				MatchEventsHelpersTest.class.getResourceAsStream("/config/language.json"), StandardCharsets.UTF_8)) {
			Texts texts = new Gson().fromJson(reader, Texts.class);
			for (String sKey : new String[] { "tor_anzeige", "wechsel_anzeige", "startelf", "trainer" })
				for (Languages language : Languages.values())
					assertNotNull(texts.getTextInLanguage(language, sKey), sKey + " " + language);
		}
	}

	@Test
	void teamOfPlayerIsFoundOnFieldAndBench() {
		Lineup lineup = new Lineup();
		Player home = new Player("Lena", "Hofmann", 9, Position.STRIKER);
		Player homeBench = new Player("Mia", "Becker", 17, Position.MIDFIELD);
		Player guest = new Player("Anna", "Weber", 14, Position.DEFENDER);
		Player nobody = new Player("Eva", "Klein", 1, Position.GOALKEEPER);
		lineup.addPlayerOnField(Teams.HOME, home);
		lineup.addPlayerOnExchange(Teams.HOME, homeBench);
		lineup.addPlayerOnField(Teams.GUEST, guest);

		assertEquals(Teams.HOME, lineup.teamOf(home));
		assertEquals(Teams.HOME, lineup.teamOf(homeBench));
		assertEquals(Teams.GUEST, lineup.teamOf(guest));
		assertNull(lineup.teamOf(nobody));
	}
}
