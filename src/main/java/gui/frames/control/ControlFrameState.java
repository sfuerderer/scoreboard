/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control;

import gui.frames.ControlFrame;
import javafx.scene.layout.GridPane;

public interface ControlFrameState {
	public GridPane buildButtons(ControlFrame controlFram);
}
