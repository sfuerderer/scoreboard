/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football.match;

public class Statistic {
	
	private int iCornersHome;
	private int iCornersGuest;
	private int iSpectators;
	
	public Statistic() {
		iCornersHome = 0;
		iCornersGuest = 0;
		iSpectators = 0;
	}

	/**
	 * Increases the corner count of the home team.
	 */
	public void increaseCornersHome() {
		iCornersHome++;
	}
	
	/**
	 * Increases the corner count of the guest team.
	 */
	public void increaseCornersGuest() {
		iCornersGuest++;
	}
	
	/**
	 * Decreases the corner count of the home team.
	 * 
	 * @return false, if an error oncured
	 */
	public boolean decreaseCornersHome() {
		if(iCornersHome <= 0)
			return false;
		iCornersHome--;
		return true;
	}
	
	/**
	 * Decreases the corner count of the home team.
	 * 
	 * @return false, if an error oncured
	 */
	public boolean decreaseCornersGuest() {
		if(iCornersGuest <= 0)
			return false;
		iCornersGuest--;
		return true;
	}
	
	/**
	 * Returns the iSpectators.
	 *
	 * @return the iSpectators
	 */
	public int getiSpectators() {
		return iSpectators;
	}

	/**
	 * Sets the iSpectators to the new value.
	 *
	 * @param iSpectators the iSpectators to set
	 */
	public void setiSpectators(int iSpectators) {
		this.iSpectators = iSpectators;
	}

	/**
	 * Returns the iCornerHome.
	 *
	 * @return the iCornerHome
	 */
	public int getiCornersHome() {
		return iCornersHome;
	}

	/**
	 * Returns the iCornerGuest.
	 *
	 * @return the iCornerGuest
	 */
	public int getiCornersGuest() {
		return iCornersGuest;
	}

	
}
