/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football.match;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import football.person.Player;
import football.person.Position;

public class Lineup {
	private List<Player> lHomePlayersOnField;
	private List<Player> lHomePlayersOnExchange;
	private List<Player> lGuestPlayersOnField;
	private List<Player> lGuestPlayersOnExchange;

	public Lineup() {
		lHomePlayersOnField = new ArrayList<Player>();
		lHomePlayersOnExchange = new ArrayList<Player>();
		lGuestPlayersOnField = new ArrayList<Player>();
		lGuestPlayersOnExchange = new ArrayList<Player>();
	}

	public void addPlayerOnField(Teams team, Player playerToAdd) {
		switch (team) {
		case HOME:
			lHomePlayersOnField.add(playerToAdd);
			break;
		case GUEST:
			lGuestPlayersOnField.add(playerToAdd);
			break;
		}
	}

	public void addPlayerOnExchange(Teams team, Player playerToAdd) {
		switch (team) {
		case HOME:
			lHomePlayersOnExchange.add(playerToAdd);
			break;
		case GUEST:
			lGuestPlayersOnExchange.add(playerToAdd);
			break;
		}
	}
	
	public void removePlayerOnField(Teams team, Player playerToRemove) {
		switch (team) {
		case HOME:
			lHomePlayersOnField.remove(playerToRemove);
			break;
		case GUEST:
			lGuestPlayersOnField.remove(playerToRemove);
			break;
		}
	}

	public void removePlayerOnExchange(Teams team, Player playerToRemove) {
		switch (team) {
		case HOME:
			lHomePlayersOnExchange.remove(playerToRemove);
			break;
		case GUEST:
			lGuestPlayersOnExchange.remove(playerToRemove);
			break;
		}
	}

	/**
	 * Returns the lHomePlayersOnField.
	 *
	 * @return the lHomePlayersOnField
	 */
	public List<Player> getlHomePlayersOnField() {
		return lHomePlayersOnField;
	}

	/**
	 * Returns the lHomePlayersOnExchange.
	 *
	 * @return the lHomePlayersOnExchange
	 */
	public List<Player> getlHomePlayersOnExchange() {
		return lHomePlayersOnExchange;
	}

	/**
	 * Returns the lGuestPlayersOnField.
	 *
	 * @return the lGuestPlayersOnField
	 */
	public List<Player> getlGuestPlayersOnField() {
		return lGuestPlayersOnField;
	}

	/**
	 * Returns the lGuestPlayersOnExchange.
	 *
	 * @return the lGuestPlayersOnExchange
	 */
	public List<Player> getlGuestPlayersOnExchange() {
		return lGuestPlayersOnExchange;
	}

	public boolean isPlayerOnField(Teams team, Player player) {
		switch (team) {
		case HOME:
			return lHomePlayersOnField.contains(player);
		case GUEST:
			return lGuestPlayersOnField.contains(player);
		}
		return false;
	}
	
	public boolean isPlayerOnChange(Teams team, Player player) {
		switch (team) {
		case HOME:
			return lHomePlayersOnExchange.contains(player);
		case GUEST:
			return lGuestPlayersOnExchange.contains(player);
		}
		return false;
	}
	
	/**
	 * Order for the scoreboard: goalkeepers first, then by shirt number; players without number at the end.
	 */
	public static List<Player> sortedForScoreboard(List<Player> lPlayers) {
		List<Player> lSorted = new ArrayList<Player>(lPlayers);
		lSorted.sort(Comparator
				.comparing((Player player) -> player.getePosition() != Position.GOALKEEPER)
				.thenComparing(player -> player.getiNumber() < 0)
				.thenComparingInt(Player::getiNumber));
		return lSorted;
	}

	/**
	 * Finds the team of a player in the lineup (on the field or on the bench).
	 *
	 * @return the team, null if the player is not in the lineup
	 */
	public Teams teamOf(Player player) {
		if (lHomePlayersOnField.contains(player) || lHomePlayersOnExchange.contains(player))
			return Teams.HOME;
		if (lGuestPlayersOnField.contains(player) || lGuestPlayersOnExchange.contains(player))
			return Teams.GUEST;
		return null;
	}

	public int countOnField(Teams team) {
		switch (team) {
		case HOME:
			return lHomePlayersOnField.size();
		case GUEST:
			return lGuestPlayersOnField.size();
		}
		return -1;
	}
}
