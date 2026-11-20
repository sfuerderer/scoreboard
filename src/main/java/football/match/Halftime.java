/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football.match;

public enum Halftime {
	FIRST(0, "halbzeit_1"), SECOND(1, "halbzeit_2"), THIRD(2, "halbzeit_3"), FOURTH(3, "halbzeit_4");

	private int iHalftime;
	// Short label for the scoreboard, e.g. "2. HZ"
	private String sLanguageKey;

	Halftime(int iHalftime, String sLanguageKey){
		this.iHalftime = iHalftime;
		this.sLanguageKey = sLanguageKey;
	}

	public int getiHalftime() {
		return iHalftime;
	}

	public String getsLanguageKey() {
		return sLanguageKey;
	}
}
