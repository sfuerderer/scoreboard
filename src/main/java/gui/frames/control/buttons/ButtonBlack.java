/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import gui.frames.ControlFrame;
import gui.frames.panes.PaneBlack;
import javafx.event.ActionEvent;
import language.Language;

public class ButtonBlack extends ControlButton{
	public ButtonBlack(ControlFrame controlFrame) {
		super(Language.getText("schwarz"), controlFrame);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == this) {
			controlFrame.changeMainScoreboardPane(new PaneBlack(controlFrame.getRootPaneMainScoreboard()));
		}
	}
}
