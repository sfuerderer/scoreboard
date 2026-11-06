/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football.match;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import football.Cards;
import football.person.Player;
import football.person.Position;
import language.Languages;
import language.Texts;

class MatchEventTest {
	private final Player schwarz = new Player("Daniela", "Schwarz", 10, Position.MIDFIELD);
	private final Player loos = new Player("Chiara", "Loos", 7, Position.STRIKER);
	private final Player weber = new Player("Anna", "Weber", 14, Position.DEFENDER);

	private static Match newMatch() {
		return new Match(null, null, 45, 45, 15, 15);
	}

	@Test
	void scorersAreListedWithMinutesInTheOrderOfTheirFirstGoal() {
		Match match = newMatch();
		match.addGoal(Teams.HOME, 23);
		match.setScorerOfLastGoal(Teams.HOME, schwarz);
		match.addGoal(Teams.GUEST, 30);
		match.setScorerOfLastGoal(Teams.GUEST, weber);
		match.addGoal(Teams.HOME, 41);
		match.setScorerOfLastGoal(Teams.HOME, loos);
		match.addGoal(Teams.HOME, 50);
		match.setScorerOfLastGoal(Teams.HOME, schwarz);

		assertEquals("Schwarz 23', 50' · Loos 41'", MatchEvent.formatScorers(match.getGoals(Teams.HOME)));
		assertEquals("Weber 30'", MatchEvent.formatScorers(match.getGoals(Teams.GUEST)));
		assertEquals(3, match.getiGoalsHome());
	}

	@Test
	void goalsWithoutScorerOrMinuteAreShownAsFarAsKnown() {
		Match match = newMatch();
		match.addGoal(Teams.HOME);
		match.addGoal(Teams.HOME, 0);
		match.setScorerOfLastGoal(Teams.HOME, schwarz);

		assertEquals("Schwarz", MatchEvent.formatScorers(match.getGoals(Teams.HOME)));
		assertEquals(2, match.getiGoalsHome());
	}

	@Test
	void takingBackAGoalRemovesTheLatestGoalOfThatTeam() {
		Match match = newMatch();
		match.addGoal(Teams.HOME, 23);
		match.setScorerOfLastGoal(Teams.HOME, schwarz);
		match.addGoal(Teams.GUEST, 30);
		match.setScorerOfLastGoal(Teams.GUEST, weber);
		match.addGoal(Teams.HOME, 41);
		match.setScorerOfLastGoal(Teams.HOME, loos);

		match.removeGoal(Teams.HOME);

		assertEquals("Schwarz 23'", MatchEvent.formatScorers(match.getGoals(Teams.HOME)));
		assertEquals("Weber 30'", MatchEvent.formatScorers(match.getGoals(Teams.GUEST)));
		assertEquals(1, match.getiGoalsHome());
	}

	@Test
	void takingBackAGoalAtZeroChangesNothing() {
		Match match = newMatch();
		match.addGoal(Teams.GUEST, 30);

		match.removeGoal(Teams.HOME);

		assertEquals(0, match.getiGoalsHome());
		assertEquals(1, match.getGoals(Teams.GUEST).size());
	}

	@Test
	void yellowRedCountsAsYellowAndAsSendingOff() {
		Match match = newMatch();
		match.addCard(Teams.GUEST, weber, Cards.YELLOW, 20);
		match.addCard(Teams.GUEST, weber, Cards.YELLOW_RED, 70);
		match.addCard(Teams.HOME, schwarz, Cards.YELLOW, 55);
		match.addCard(Teams.HOME, loos, Cards.RED, 88);

		assertEquals(2, match.countYellowCards(Teams.GUEST));
		assertEquals(1, match.countRedCards(Teams.GUEST));
		assertEquals(1, match.countYellowCards(Teams.HOME));
		assertEquals(1, match.countRedCards(Teams.HOME));
	}

	@Test
	void resultTitleIsHalfTimeOnlyInTheBreakAfterTheFirstHalf() {
		Match match = newMatch();
		assertEquals("spielstand", match.getResultTitleKey());

		match.nextHalf();
		assertEquals("halbzeit_anzeige", match.getResultTitleKey());

		match.start();
		assertEquals("spielstand", match.getResultTitleKey());
	}

	@Test
	void textsForResultAndStatisticExistInAllLanguages() throws Exception {
		try (Reader reader = new InputStreamReader(MatchEventTest.class.getResourceAsStream("/config/language.json"),
				StandardCharsets.UTF_8)) {
			Texts texts = new Gson().fromJson(reader, Texts.class);
			for (String sKey : new String[] { "halbzeit_anzeige", "spielstand", "gelbe_karten", "rote_karten",
					"spielstand_anzeigen", "statistik_anzeigen", "statistik", "ecken", "zuschauer" })
				for (Languages language : Languages.values())
					assertNotNull(texts.getTextInLanguage(language, sKey), sKey + " " + language);
		}
	}
}
