/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.animation;

import gui.frames.panes.PaneMainScoreboard;
import javafx.animation.FadeTransition;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class AnimationFade implements IAnimation {

	@Override
	public void doAnimationIn(PaneMainScoreboard paneToAdd, PaneMainScoreboard paneToRemove, StackPane rootPane) {

	    rootPane.getChildren().add(paneToAdd);
	    FadeTransition fadeInTransition = new FadeTransition(Duration.millis(1000));

	    fadeInTransition.setOnFinished(evt -> {
	    	rootPane.getChildren().remove(paneToRemove);
	    	
	    	paneToRemove.paneDidDisappear();
	    	paneToAdd.paneDidAppear();
	    });
	    fadeInTransition.setNode(paneToAdd);
	    fadeInTransition.setFromValue(0);
	    fadeInTransition.setToValue(1);
	    fadeInTransition.play();
	    
	    
	}
	
	@Override
	public void doAnimationOut(PaneMainScoreboard paneToAdd, PaneMainScoreboard paneToRemove, StackPane rootPane) {

	    rootPane.getChildren().add(paneToAdd);
	    FadeTransition fadeInTransition = new FadeTransition(Duration.millis(1000));

	    fadeInTransition.setOnFinished(evt -> {
	    	rootPane.getChildren().remove(paneToRemove);
	    	
	    	paneToRemove.paneDidDisappear();
	    	paneToAdd.paneDidAppear();
	    });
	    fadeInTransition.setNode(paneToAdd);
	    fadeInTransition.setFromValue(1);
	    fadeInTransition.setToValue(0);
	    fadeInTransition.play();
	}

}
