/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.animation;

public enum Animations {
	FADE(new AnimationFade());
	
	private IAnimation animation;
	Animations(IAnimation animation){
		this.animation = animation;
	}
	
	public IAnimation getAnimation() {
		return animation;
	}
}
