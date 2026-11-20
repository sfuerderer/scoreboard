/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football.match;

/**
 * Minute of the match as counted in football: "67:12" on the clock is the 68th minute.
 */
public final class MatchMinute {

	private MatchMinute() {
	}

	/**
	 * @param sClock clock as shown, "mm:ss" counted from kick-off
	 * @return the minute (at least 1), or 0 if the clock cannot be read
	 */
	public static int of(String sClock) {
		if (sClock == null)
			return 0;
		String[] sParts = sClock.trim().split(":");
		if (sParts.length != 2)
			return 0;
		try {
			int iMinutes = Integer.parseInt(sParts[0]);
			Integer.parseInt(sParts[1]);
			return iMinutes < 0 ? 0 : iMinutes + 1;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Minute for the scoreboard, e.g. "68'", or an empty text if the clock cannot be read.
	 */
	public static String format(String sClock) {
		int iMinute = of(sClock);
		return iMinute == 0 ? "" : iMinute + "'";
	}
}
