/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package language;

public enum Languages {
	GERMAN(0), ENGLISH(1);
	
	private int iId;
	
	Languages(int iId) {
		this.iId = iId;
	}
	
	public int getIId() {
		return iId;
	}
}
