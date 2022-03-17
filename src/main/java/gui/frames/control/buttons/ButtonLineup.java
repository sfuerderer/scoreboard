/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import gui.frames.ControlFrame;
import gui.frames.lineupFrame.ShowLineupFrame;
import javafx.event.ActionEvent;
import language.Language;

public class ButtonLineup extends ControlButton{
	public ButtonLineup(ControlFrame controlFrame) {
		super(Language.getText("aufstellung"), controlFrame);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == this) {
			new ShowLineupFrame(controlFrame.getSettings(),controlFrame.getGuiControlling(),controlFrame).show();
		}
	}
}
