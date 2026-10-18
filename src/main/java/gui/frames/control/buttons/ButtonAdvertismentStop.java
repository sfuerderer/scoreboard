/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import gui.frames.ControlFrame;
import javafx.event.ActionEvent;
import language.Language;
import system.Main;

public class ButtonAdvertismentStop extends ControlButton{
	public ButtonAdvertismentStop(ControlFrame controlFrame) {
		super(Language.getText("werbung_stop"), controlFrame);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == this) {
			Main.getAdThread().stopThread();
			controlFrame.showMatchState();
		}
	}
}
