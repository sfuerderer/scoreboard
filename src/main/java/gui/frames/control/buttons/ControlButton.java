/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import gui.frames.ControlFrame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public abstract class ControlButton extends Button implements EventHandler<ActionEvent>{
	protected ControlFrame controlFrame;
	
	public ControlButton(String btnText, ControlFrame controlFrame, boolean bDisabled) {
		super(btnText);
		this.controlFrame = controlFrame;
		this.setMaxWidth(Double.MAX_VALUE);
		setDisabled(bDisabled);
		setOnAction(this);
	}
	public ControlButton(String btnText, ControlFrame controlFrame) {
		this(btnText, controlFrame, false);
	}

}
