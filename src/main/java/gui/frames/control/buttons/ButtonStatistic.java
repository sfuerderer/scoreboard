/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import gui.frames.ControlFrame;
import gui.frames.statisticFrame.StatisticFrame;
import javafx.event.ActionEvent;
import language.Language;

public class ButtonStatistic extends ControlButton{
	public ButtonStatistic(ControlFrame controlFrame) {
		super(Language.getText("statistik"), controlFrame);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == this) {
			new StatisticFrame(controlFrame.getSettings(), controlFrame.getGuiControlling(), controlFrame).show();
		}
	}
}
