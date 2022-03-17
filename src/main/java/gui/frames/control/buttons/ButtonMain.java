/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import gui.frames.ControlFrame;
import gui.frames.panes.PaneMain;
import javafx.event.ActionEvent;
import language.Language;

public class ButtonMain extends ControlButton{
	public ButtonMain(ControlFrame controlFrame) {
		super(Language.getText("main"), controlFrame);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == this) {
			controlFrame.changeMainScoreboardPane(new PaneMain(controlFrame.getRootPaneMainScoreboard(),controlFrame));
		}
	}
}
