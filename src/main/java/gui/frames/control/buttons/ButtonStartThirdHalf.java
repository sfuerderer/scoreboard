/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import football.match.Halftime;
import football.match.Match;
import gui.frames.ControlFrame;
import gui.frames.control.ControlFrameStateTimeRunning;
import javafx.event.ActionEvent;
import language.Language;
import system.Main;

public class ButtonStartThirdHalf extends ControlButton{
	public ButtonStartThirdHalf(ControlFrame controlFrame) {
		super(Language.getText("start_hz_3"), controlFrame);
		setDisabled(true);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == this) {
			Match match = Main.getMatch();
			match.seteWhichHalftime(Halftime.THIRD);
			match.start();
			controlFrame.changeState(new ControlFrameStateTimeRunning());
		}
	}
}
