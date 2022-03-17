/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import gui.frames.ControlFrame;
import gui.frames.panes.PaneAbout;
import javafx.event.ActionEvent;
import language.Language;

public class ButtonAbout extends ControlButton{
	public ButtonAbout(ControlFrame controlFrame) {
		super(Language.getText("ueber"), controlFrame);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == this) {
			controlFrame.changeMainScoreboardPane(new PaneAbout(controlFrame.getRootPaneMainScoreboard()));
		}
	}
}
