/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package exceptions;

public class LeagueNotCreatedException extends Exception {
	private static final long serialVersionUID = -6759227449617454504L;

	public LeagueNotCreatedException() {
		super("The league could't be created");
	}

}
