/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football.match;

public enum Halftime {
	FIRST(0), SECOND(1), THIRD(2), FOURTH(3);
	
	private int iHalftime;
	Halftime(int iHalftime){
		this.iHalftime = iHalftime;
	}
	
	public int getiHalftime() {
		return iHalftime;
	}
}
