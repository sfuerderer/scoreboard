/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package exceptions;

public class TextNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3250557220984598766L;

	public TextNotFoundException() {
		super("Der gesuchte Text ist nicht vorhanden.");
	}
}
