/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package exceptions;

public class LanguageNotLoadedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -744988320808167746L;

	public LanguageNotLoadedException() {
		super("Die Sprachdatei konnte nicht geladen oder gefunden werden. Bitte wenden Sie sich an die Entwickler.");
	}
}
