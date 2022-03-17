/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import gui.frames.ControlFrame;
import gui.frames.replacementFrame.ReplacementFrame;
import javafx.event.ActionEvent;
import language.Language;

public class ButtonChange extends ControlButton{
	public ButtonChange(ControlFrame controlFrame) {
		super(Language.getText("wechsel"), controlFrame);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == this) {
			new ReplacementFrame(controlFrame.getSettings(), controlFrame.getGuiControlling(), controlFrame).show();
		}
	}
}
