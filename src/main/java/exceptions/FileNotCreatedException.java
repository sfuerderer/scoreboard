/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package exceptions;

public class FileNotCreatedException extends Exception {
	private static final long serialVersionUID = -6759227449617454504L;

	public FileNotCreatedException() {
		super("file couldn`t be created");
	}

}
