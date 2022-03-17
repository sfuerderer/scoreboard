/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package background.threads;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;

import gui.frames.ControlFrame;
import gui.frames.panes.PaneImage;
import gui.frames.panes.PaneMain;
import gui.frames.panes.PaneVideo;
import javafx.application.Platform;
import javafx.scene.media.Media;
import message.ErrorMsg;
import system.Medias;
import system.settings.Settings;

@SuppressWarnings("restriction")
public class AdvertismentThread extends Thread {
	private boolean laeuft = true;
	private ArrayList<String> filenames = new ArrayList<String>();
	private ControlFrame controlFrame;
	private Settings sSettings;

	public AdvertismentThread(ControlFrame controlFrame, Settings sSettings) {
		this.controlFrame = controlFrame;
		this.sSettings = sSettings;
		File dir = new File("advertisment");

		String[] children = dir.list();
		if (children == null) {

		} else {
			for (int i = 0; i < children.length; i++) {
				String name = children[i];
				if (name.charAt(0) != '.')
					filenames.add(name);
			}
		}

		Collections.sort(filenames);
	}

	public void stopThread() {
		laeuft = false;
		controlFrame.changeMainScoreboardPane(new PaneMain(controlFrame.getRootPaneMainScoreboard(), controlFrame));
	}

	private static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}

	public void run() {
		int i = 0;
		try {
			while (laeuft) {
				File file = new File("advertisment/" + filenames.get(i));
				if (getFileExtension(file).equals("mp4")) {
					Media video = Medias.loadMediaFromPath(file.getAbsolutePath());
					PaneVideo videoPane = new PaneVideo(controlFrame.getRootPaneMainScoreboard(), video);
					Platform.runLater(new Runnable() {

						@Override
						public void run() {

							controlFrame.changeMainScoreboardPane(videoPane);

						}
					});
					Thread.sleep((long) video.getDuration().toMillis());
				} else if (getFileExtension(file).equals("jpg") || getFileExtension(file).equals("png")) {
					Platform.runLater(new Runnable() {

						@Override
						public void run() {

							controlFrame.changeMainScoreboardPane(
									new PaneImage(controlFrame.getRootPaneMainScoreboard(), file.getAbsolutePath()));

						}
					});
					Thread.sleep(sSettings.iTimeBetweenAdvertisingInMiliseconds);

				}
				i = ++i % filenames.size();
				
			}
		} catch (InterruptedException ie) {
			ErrorMsg.showErrorMessage(116);
		} catch (MalformedURLException e) {
			ErrorMsg.showErrorMessage(114);
		}
	}

}
