/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javafx.scene.text.Font;

public class Fonts {

	// Reading the font file is expensive, so every font and size is loaded only once
	private static final Map<String, Font> FONT_CACHE = new ConcurrentHashMap<String, Font>();

	public static Font loadFontFromResource(String sImageName,String sFileExtension, int iSize) {
		return FONT_CACHE.computeIfAbsent(sImageName + "." + sFileExtension + "@" + iSize, key -> Font
				.loadFont(Fonts.class.getResource("/fonts/" + sImageName + "." + sFileExtension).toExternalForm(), iSize));
	}

}
