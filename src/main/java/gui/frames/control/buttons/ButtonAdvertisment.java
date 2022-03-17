/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import background.threads.AdvertismentThread;
import gui.frames.ControlFrame;
import gui.frames.control.ControlFrameStateAdvertisment;
import javafx.event.ActionEvent;
import language.Language;
import system.Main;

public class ButtonAdvertisment extends ControlButton{
	public ButtonAdvertisment(ControlFrame controlFrame) {
		super(Language.getText("werbung"), controlFrame);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == this) {
			controlFrame.changeState(new ControlFrameStateAdvertisment());
			AdvertismentThread adThread = new AdvertismentThread(controlFrame,controlFrame.getSettings());
			Main.setAdThread(adThread);
			adThread.start();
		}
	}
}
