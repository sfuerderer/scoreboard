/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import gui.frames.ControlFrame;
import gui.frames.panes.PaneWelcome;
import javafx.event.ActionEvent;
import language.Language;

public class ButtonWelcome extends ControlButton{
	public ButtonWelcome(ControlFrame controlFrame) {
		super(Language.getText("willkommen"), controlFrame);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == this) {
			controlFrame.changeMainScoreboardPane(new PaneWelcome(controlFrame.getRootPaneMainScoreboard()));
		}
	}
}
