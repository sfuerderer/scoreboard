/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import gui.frames.ControlFrame;
import gui.frames.control.ControlFrameStateMatch;
import javafx.event.ActionEvent;
import language.Language;
import system.Main;

public class ButtonStopTime extends ControlButton{
	public ButtonStopTime(ControlFrame controlFrame) {
		super(Language.getText("stop_zeit"), controlFrame);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == this) {
			Main.getMatch().stop();
			controlFrame.changeState(new ControlFrameStateMatch());
		}
	}
}
