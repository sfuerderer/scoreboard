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
	private volatile boolean laeuft = true;
	private ArrayList<String> filenames = new ArrayList<String>();
	private ControlFrame controlFrame;
	private Settings sSettings;

	public AdvertismentThread(ControlFrame controlFrame, Settings sSettings) {
		this.controlFrame = controlFrame;
		this.sSettings = sSettings;
		setDaemon(true);
		File dir = new File("advertisment");

		String[] children = dir.list();
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				String name = children[i];
				// Only supported files, otherwise the loop below would run without any pause
				if (name.charAt(0) != '.' && (isVideo(name) || isImage(name)))
					filenames.add(name);
			}
		}

		Collections.sort(filenames);
	}

	/**
	 * @return true if there is at least one image or video to show
	 */
	public boolean hasAdvertisments() {
		return !filenames.isEmpty();
	}

	public void stopThread() {
		laeuft = false;
		interrupt();
		controlFrame.changeMainScoreboardPane(new PaneMain(controlFrame.getRootPaneMainScoreboard(), controlFrame));
	}

	private static String getFileExtension(String fileName) {
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		else
			return "";
	}

	private static boolean isVideo(String fileName) {
		return getFileExtension(fileName).equals("mp4");
	}

	private static boolean isImage(String fileName) {
		String extension = getFileExtension(fileName);
		return extension.equals("jpg") || extension.equals("png");
	}

	/**
	 * Shows the pane, unless the advertisement was stopped in the meantime.
	 */
	private void showPane(Runnable showPane) {
		Platform.runLater(() -> {
			if (laeuft)
				showPane.run();
		});
	}

	public void run() {
		if (filenames.isEmpty())
			return;
		int i = 0;
		try {
			while (laeuft) {
				File file = new File("advertisment/" + filenames.get(i));
				if (isVideo(file.getName())) {
					Media video = Medias.loadMediaFromPath(file.getAbsolutePath());
					PaneVideo videoPane = new PaneVideo(controlFrame.getRootPaneMainScoreboard(), video);
					showPane(() -> controlFrame.changeMainScoreboardPane(videoPane));
					double duration = video.getDuration().toMillis();
					// The duration is unknown if the video could not be loaded
					Thread.sleep(Double.isNaN(duration) || duration <= 0 ? sSettings.iTimeBetweenAdvertisingInMiliseconds
							: (long) duration);
				} else {
					showPane(() -> controlFrame.changeMainScoreboardPane(
							new PaneImage(controlFrame.getRootPaneMainScoreboard(), file.getAbsolutePath())));
					Thread.sleep(sSettings.iTimeBetweenAdvertisingInMiliseconds);
				}
				i = ++i % filenames.size();

			}
		} catch (InterruptedException ie) {
			if (laeuft)
				ErrorMsg.showErrorMessage(116);
		} catch (MalformedURLException e) {
			ErrorMsg.showErrorMessage(114);
		}
	}

}
