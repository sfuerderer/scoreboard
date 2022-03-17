/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control;

import gui.frames.ControlFrame;
import gui.frames.control.buttons.ButtonStartMatch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public class ControlFrameStateStart implements ControlFrameState{

	@Override
	public GridPane buildButtons(ControlFrame controlFrame) {
		GridPane gpResult = new GridPane();
		gpResult.setPadding(new Insets(10));
		gpResult.setAlignment(Pos.CENTER);
		
		gpResult.add(new ButtonStartMatch(controlFrame), 0, 0);
		return gpResult;
	}


}
