/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package exceptions;

public class FileDidNotExistException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6414310718015264537L;

	public FileDidNotExistException() {
		super("file didn`t exists");
	}

}
