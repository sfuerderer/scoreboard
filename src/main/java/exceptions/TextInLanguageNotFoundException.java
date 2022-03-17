/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package exceptions;

public class TextInLanguageNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3250557220984598766L;

	public TextInLanguageNotFoundException() {
		super("Der Text wurde in der angeforderten Sprache nicht gefunden.");
	}
}
