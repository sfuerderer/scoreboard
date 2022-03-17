/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import gui.frames.ControlFrame;
import gui.frames.mediaFrame.MediaFrame;
import gui.frames.mediaFrame.MediaType;
import javafx.event.ActionEvent;
import language.Language;

public class ButtonPicture extends ControlButton{
	public ButtonPicture(ControlFrame controlFrame) {
		super(Language.getText("bild_anzeigen"), controlFrame);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == this) {
			new MediaFrame(controlFrame.getSettings(),controlFrame.getGuiControlling(),controlFrame, MediaType.PICTURE).show();
		}
	}
}
