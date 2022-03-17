/************************************************
 * Copyright (c) 2021 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football.person;

import java.util.Date;

import com.google.gson.annotations.Expose;

public class Coach extends Person implements Comparable<Coach> {
	@Expose
	private CoachType eCoachType;
	@Expose
	private String sImagePath;

	public Coach(String sFirstname, String sLastname, CoachType eCoachType) {
		this(sFirstname, sLastname, eCoachType, null);
	}

	public Coach(String sFirstname, String sLastname, CoachType eCoachType, Date dBirthday) {
		super(sFirstname, sLastname, dBirthday);
		this.eCoachType = eCoachType;
		sImagePath = null;
	}

	/**
	 * Returns the ePosition.
	 *
	 * @return the ePosition
	 */
	public CoachType geteCoachType() {
		return eCoachType;
	}

	/**
	 * Sets the ePosition to the new value.
	 *
	 * @param ePosition the ePosition to set
	 */
	public void seteCoachType(CoachType eCoachType) {
		this.eCoachType = eCoachType;
	}

	/**
	 * Returns the sImagePath.
	 *
	 * @return the sImagePath
	 */
	public String getsImagePath() {
		return sImagePath;
	}

	/**
	 * Sets the sImagePath to the new value.
	 *
	 * @param sImagePath the sImagePath to set
	 */
	public void setsImagePath(String sImagePath) {
		this.sImagePath = sImagePath;
	}

	@Override
	public String toString() {
		return sFirstnameGson + " " + sLastnameGson;
	}

	@Override
	public int compareTo(Coach otherPerson) {
		if (eCoachType == CoachType.HEADCOACH && otherPerson.geteCoachType() == CoachType.ASSISTENTCOACH
				|| eCoachType == CoachType.HEADCOACH && otherPerson.geteCoachType() == CoachType.GOALKEEPERCOACH
				|| eCoachType == CoachType.ASSISTENTCOACH && otherPerson.geteCoachType() == CoachType.GOALKEEPERCOACH)
			return 1;
		else if (eCoachType == CoachType.ASSISTENTCOACH && otherPerson.geteCoachType() == CoachType.HEADCOACH
				|| eCoachType == CoachType.GOALKEEPERCOACH && otherPerson.geteCoachType() == CoachType.HEADCOACH
				|| eCoachType == CoachType.GOALKEEPERCOACH && otherPerson.geteCoachType() == CoachType.ASSISTENTCOACH)
			return -1;

		return 0;
	}

}
