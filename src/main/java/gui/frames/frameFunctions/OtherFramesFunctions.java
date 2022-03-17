/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.frameFunctions;

public enum OtherFramesFunctions {
	DATE(new FramesFunctionDate(),0), CURRENT_TIME(new FramesFunctionTime(),1);
	
	private FramesFunction otherFrameFunction;
	private int iIndex;
	
	OtherFramesFunctions(FramesFunction otherFrameFunction, int iIndex) {
		this.otherFrameFunction = otherFrameFunction;
		this.iIndex = iIndex;
	}
	
	public FramesFunction getOtherFrameFunction() {
		return this.otherFrameFunction;
	}
	
	public int getIndex() {
		return iIndex;
	}
}
