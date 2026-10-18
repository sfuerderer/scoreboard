/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import exceptions.FileNotCreatedException;
import football.Team;
import football.person.Coach;
import football.person.Player;
import javafx.scene.image.Image;

public class Images {
	private static Images images;
	// Images from the JAR never change, so they are decoded only once
	private final Map<String, Image> resourceCache = new ConcurrentHashMap<String, Image>();
	
	private Images() {
		
	}
	
	public static Images getImages() {
		if(images == null)
			images = new Images();
		return images;
	}

	public Image loadImageFromResource(String sImageName, String sFileExtension) {
		return resourceCache.computeIfAbsent(sImageName + "." + sFileExtension, key -> new Image(
				Images.class.getResource("/images/" + sImageName + "." + sFileExtension).toExternalForm(), true));
	}
	
	public Image loadImageFromPath(String sPathToImage) throws MalformedURLException {
		File image = new File(sPathToImage);
		Image img = new Image(image.toURI().toURL().toString(),false);
		image = null;
		return img;
	}

	/**
	 * Loads the image scaled down to fit into the given size (keeping the ratio).
	 * Saves memory and decoding time for large pictures such as advertisements.
	 */
	public Image loadImageFromPath(String sPathToImage, double dMaxWidth, double dMaxHeight) throws MalformedURLException {
		File image = new File(sPathToImage);
		return new Image(image.toURI().toURL().toString(), dMaxWidth, dMaxHeight, true, true, false);
	}
	
	/**
	 * Moves the given file to the team folder.
	 * @param sBasePathTeam The path to the team folder.
	 * @param fImage The image file.
	 * @param currentPlayer The current player.
	 * 
	 * @return The new path, where the file is stored.
	 * @throws FileNotCreatedException
	 * @throws IOException
	 */
	public String movePlayerImage(String sBasePathTeam, File fImage, Player currentPlayer) throws FileNotCreatedException, IOException {
		String sNewPath = sBasePathTeam+"/playerImages/"+fImage.getName();
		moveImage(sNewPath, fImage);
		return sNewPath;
	}
	
	/**
	 * Moves the given file to the coach folder.
	 * @param sBasePathTeam The path to the team folder.
	 * @param fImage The image file.
	 * @param currentPlayer The current player.
	 * 
	 * @return The new path, where the file is stored.
	 * @throws FileNotCreatedException
	 * @throws IOException
	 */
	public String moveCoachImage(String sBasePathTeam, File fImage, Coach currentCoach) throws FileNotCreatedException, IOException {
		String sNewPath = sBasePathTeam+"/coachImages/"+fImage.getName();
		moveImage(sNewPath, fImage);
		return sNewPath;
	}
	
	public String moveLogoImage(Team team, File fImage) throws IOException {
		String sNewPath = team.getPathToFolder()+"/teamImages/logo."+FileHandling.getFileHandling().getExtensionOfFile(fImage);
		moveImage(sNewPath, fImage);
		return sNewPath;
	}
	
	private void moveImage(String sPath, File fImage) throws IOException {
		File newFile = new File(sPath);
		
		newFile.mkdirs();
		
		Files.copy(fImage.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

}
