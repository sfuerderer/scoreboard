/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.animation;

public enum Animations {
	NONE("animation_none", new AnimationNone()),
	FADE("animation_fade", new AnimationFade()),
	SLIDE("animation_slide", new AnimationSlide(false)),
	PUSH_UP("animation_push_up", new AnimationSlide(true)),
	ZOOM("animation_zoom", new AnimationZoom()),
	WIPE("animation_wipe", new AnimationWipe());
	
	private String sLanguageKey;
	private IAnimation animation;
	Animations(String sLanguageKey, IAnimation animation){
		this.sLanguageKey = sLanguageKey;
		this.animation = animation;
	}
	
	public String getsLanguageKey() {
		return sLanguageKey;
	}
	
	public IAnimation getAnimation() {
		return animation;
	}
}
