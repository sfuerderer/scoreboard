/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system;

import java.io.File;
import java.net.MalformedURLException;

import javafx.scene.media.Media;

@SuppressWarnings("restriction")
public class Medias {

	public static Media loadMediaFromResource(String sMediaName, String sFileExtension,String sSubFolder) {
		return new Media(Medias.class.getResource("/"+sSubFolder+"/"+sMediaName+"."+sFileExtension).toExternalForm());
	}
	
	public static Media loadMediaFromPath(String sPathToMedia) throws MalformedURLException {
		File mediaFile = new File(sPathToMedia);
		Media media = new Media(mediaFile.toURI().toURL().toString());
		return media;
	}

}
