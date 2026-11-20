/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package advertising;

/**
 * Places on the scoreboard that show exactly one sponsor ("presented by").
 */
public enum AdPlacement {
	GOAL("werbeplatz_tor"), CARD("werbeplatz_karte"), SUBSTITUTION("werbeplatz_wechsel"),
	LINEUP("werbeplatz_aufstellung"), HALFTIME("werbeplatz_halbzeit"), STATISTIC("werbeplatz_statistik"),
	CORNERS("werbeplatz_ecken"), VISITORS("werbeplatz_zuschauer");

	private final String sLanguageKey;

	AdPlacement(String sLanguageKey) {
		this.sLanguageKey = sLanguageKey;
	}

	public String getLanguageKey() {
		return sLanguageKey;
	}
}
