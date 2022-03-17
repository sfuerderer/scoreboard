/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system.settings;

import com.google.gson.annotations.Expose;

import gui.frames.frameFunctions.OtherFramesFunctions;

/**
 * A special settings class to represent one frame.
 *
 * @author Sebastian Fürderer
 * @version 1.0.0
 * @date 06.06.2020
 *
 */
public class Frame {
	@Expose
	public boolean bActivated;
	@Expose
	public int iWidth;
	@Expose
	public int iHeight;
	@Expose
	public int iStartXPosition;
	@Expose
	public int iStartYPosition;
	@Expose
	public OtherFramesFunctions otherFrameFunction;
	
	public Frame(boolean bActivated, int iWidth, int iHeight, int iStartXPosition, int iStartYPosition, OtherFramesFunctions otherFrameFunction) {
		this.bActivated = bActivated;
		this.iWidth = iWidth;
		this.iHeight = iHeight;
		this.iStartXPosition = iStartXPosition;
		this.iStartYPosition = iStartYPosition;
		this.otherFrameFunction = otherFrameFunction;
	}
	
	/**
	 * @return the bActivated
	 */
	public boolean isbActivated() {
		return bActivated;
	}
	/**
	 * @param bActivated the bActivated to set
	 */
	public void setbActivated(boolean bActivated) {
		this.bActivated = bActivated;
	}
	/**
	 * @return the iWidth
	 */
	public int getiWidth() {
		return iWidth;
	}
	/**
	 * @param iWidth the iWidth to set
	 */
	public void setiWidth(int iWidth) {
		this.iWidth = iWidth;
	}
	/**
	 * @return the iHeight
	 */
	public int getiHeight() {
		return iHeight;
	}
	/**
	 * @param iHeight the iHeight to set
	 */
	public void setiHeight(int iHeight) {
		this.iHeight = iHeight;
	}
	/**
	 * @return the iStartXPosition
	 */
	public int getiStartXPosition() {
		return iStartXPosition;
	}
	/**
	 * @param iStartXPosition the iStartXPosition to set
	 */
	public void setiStartXPosition(int iStartXPosition) {
		this.iStartXPosition = iStartXPosition;
	}
	/**
	 * @return the iStartYPosition
	 */
	public int getiStartYPosition() {
		return iStartYPosition;
	}
	/**
	 * @param iStartYPosition the iStartYPosition to set
	 */
	public void setiStartYPosition(int iStartYPosition) {
		this.iStartYPosition = iStartYPosition;
	}

	/**
	 * Returns the otherFrameFunction.
	 *
	 * @return the otherFrameFunction
	 */
	public OtherFramesFunctions getOtherFrameFunction() {
		return otherFrameFunction;
	}

	/**
	 * Sets the otherFrameFunction to the new value.
	 *
	 * @param otherFrameFunction the otherFrameFunction to set
	 */
	public void setOtherFrameFunction(OtherFramesFunctions otherFrameFunction) {
		this.otherFrameFunction = otherFrameFunction;
	}
}
