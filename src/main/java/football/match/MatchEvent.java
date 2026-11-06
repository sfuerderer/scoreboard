/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football.match;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import football.Cards;
import football.person.Player;

/**
 * Something that happened in the match and is shown later on the scoreboard (scorers at half-time, cards in
 * the statistic).
 */
public class MatchEvent {
	public enum Type {
		GOAL, CARD
	}

	private final Type eType;
	private final Teams eTeam;
	// Scorer may be set after the goal, when the operator has chosen her; null if not chosen
	private Player player;
	private final Cards eCard;
	// Minute as counted in football, 0 if unknown
	private final int iMinute;

	private MatchEvent(Type eType, Teams eTeam, Player player, Cards eCard, int iMinute) {
		this.eType = eType;
		this.eTeam = eTeam;
		this.player = player;
		this.eCard = eCard;
		this.iMinute = iMinute;
	}

	public static MatchEvent goal(Teams team, int iMinute) {
		return new MatchEvent(Type.GOAL, team, null, null, iMinute);
	}

	public static MatchEvent card(Teams team, Player player, Cards card, int iMinute) {
		return new MatchEvent(Type.CARD, team, player, card, iMinute);
	}

	public Type geteType() {
		return eType;
	}

	public Teams geteTeam() {
		return eTeam;
	}

	public Player getPlayer() {
		return player;
	}

	void setPlayer(Player player) {
		this.player = player;
	}

	public Cards geteCard() {
		return eCard;
	}

	public int getiMinute() {
		return iMinute;
	}

	/**
	 * Scorers for the scoreboard, in the order of their first goal: "Schwarz 23', 41' · Loos 50'". Goals
	 * without a chosen scorer are left out, unknown minutes are not shown.
	 */
	public static String formatScorers(List<MatchEvent> lGoals) {
		Map<Player, List<Integer>> minutesByPlayer = new LinkedHashMap<Player, List<Integer>>();
		for (MatchEvent goal : lGoals) {
			if (goal.eType != Type.GOAL || goal.player == null)
				continue;
			minutesByPlayer.computeIfAbsent(goal.player, player -> new ArrayList<Integer>()).add(goal.iMinute);
		}

		StringBuilder sbScorers = new StringBuilder();
		for (Map.Entry<Player, List<Integer>> entry : minutesByPlayer.entrySet()) {
			if (sbScorers.length() > 0)
				sbScorers.append(" · ");
			sbScorers.append(entry.getKey().getsLastname());
			StringBuilder sbMinutes = new StringBuilder();
			for (int iMinute : entry.getValue()) {
				if (iMinute <= 0)
					continue;
				sbMinutes.append(sbMinutes.length() == 0 ? " " : ", ").append(iMinute).append('\'');
			}
			sbScorers.append(sbMinutes);
		}
		return sbScorers.toString();
	}
}
