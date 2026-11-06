/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes.components;

/**
 * Progress of the running halftime for the bar under the clock.
 */
public final class ClockProgress {

	private ClockProgress() {
	}

	/**
	 * @param sClock          clock as shown, "mm:ss" counted from kick-off (e.g. "67:12" in the second half)
	 * @param iMinutesBefore  minutes of the halftimes before the current one (e.g. 45 in the second half)
	 * @param iHalftimeLength length of the current halftime in minutes
	 * @return share of the current halftime that has passed, between 0 and 1; 0 if the clock cannot be read
	 */
	public static double of(String sClock, int iMinutesBefore, int iHalftimeLength) {
		if (sClock == null || iHalftimeLength <= 0)
			return 0;
		String[] sParts = sClock.trim().split(":");
		if (sParts.length != 2)
			return 0;
		try {
			int iSeconds = Integer.parseInt(sParts[0]) * 60 + Integer.parseInt(sParts[1]);
			double dPassed = iSeconds - iMinutesBefore * 60.0;
			return Math.max(0, Math.min(1, dPassed / (iHalftimeLength * 60.0)));
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
