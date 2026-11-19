/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package background.threads;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import advertising.Advertising;
import advertising.FullscreenSpot;
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
	private final Advertising advertising;
	private final List<FullscreenSpot> lSpots;
	private ControlFrame controlFrame;
	private Settings sSettings;

	public AdvertismentThread(ControlFrame controlFrame, Settings sSettings) {
		this.controlFrame = controlFrame;
		this.sSettings = sSettings;
		setDaemon(true);
		advertising = Advertising.getAdvertising();
		advertising.addNewFolderFiles();
		lSpots = advertising.getActiveFullscreenSpots();
	}

	/**
	 * @return true if there is at least one image or video to show
	 */
	public boolean hasAdvertisments() {
		return !lSpots.isEmpty();
	}

	public void stopThread() {
		laeuft = false;
		interrupt();
		controlFrame.changeMainScoreboardPane(new PaneMain(controlFrame.getRootPaneMainScoreboard(), controlFrame));
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
		if (lSpots.isEmpty())
			return;
		int i = 0;
		try {
			while (laeuft) {
				FullscreenSpot spot = lSpots.get(i);
				File file = advertising.getSpotFile(spot);
				long lDuration = spot.iDurationInSeconds > 0 ? spot.iDurationInSeconds * 1000L
						: sSettings.iTimeBetweenAdvertisingInMiliseconds;
				if (spot.isVideo()) {
					Media video = Medias.loadMediaFromPath(file.getAbsolutePath());
					PaneVideo videoPane = new PaneVideo(controlFrame.getRootPaneMainScoreboard(), video);
					showPane(() -> controlFrame.changeMainScoreboardPane(videoPane));
					double duration = video.getDuration().toMillis();
					// The duration is unknown if the video could not be loaded
					Thread.sleep(Double.isNaN(duration) || duration <= 0 ? lDuration : (long) duration);
				} else {
					showPane(() -> controlFrame.changeMainScoreboardPane(
							new PaneImage(controlFrame.getRootPaneMainScoreboard(), file.getAbsolutePath())));
					Thread.sleep(lDuration);
				}
				i = ++i % lSpots.size();

			}
		} catch (InterruptedException ie) {
			if (laeuft)
				ErrorMsg.showErrorMessage(116);
		} catch (MalformedURLException e) {
			ErrorMsg.showErrorMessage(114);
		}
	}

}
