/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.animation;

import gui.frames.panes.PaneMainScoreboard;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class AnimationFade implements IAnimation {

	@Override
	public Animation createAnimation(PaneMainScoreboard paneToAdd, PaneMainScoreboard paneToRemove, StackPane rootPane) {
	    paneToAdd.setOpacity(0);
	    FadeTransition fadeInTransition = new FadeTransition(Duration.millis(1000), paneToAdd);
	    fadeInTransition.setFromValue(0);
	    fadeInTransition.setToValue(1);
	    return fadeInTransition;
	}

}
