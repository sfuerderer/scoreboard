/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system;

import javafx.scene.text.Font;

public class Fonts {

	public static Font loadFontFromResource(String sImageName,String sFileExtension, int iSize) {
		return Font.loadFont(Fonts.class.getResource("/fonts/"+sImageName+"."+sFileExtension).toExternalForm(), iSize);
	}

}
