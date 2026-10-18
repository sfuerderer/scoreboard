/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;

@SuppressWarnings("restriction")
public class PaneVideo extends PaneMainScoreboard {
	private static final long WAIT_TIMEOUT_SECONDS = 10;
	private MediaPlayer mediaPlayer;
	private final CountDownLatch ready = new CountDownLatch(1);

	public PaneVideo(Pane rootPane, Media video) {
		this(rootPane, video, true);
	}

	public PaneVideo(Pane rootPane, Media video, boolean waitForLoad) {
		super(rootPane, false);

		BorderPane wrapperPane = new BorderPane();
		wrapperPane.prefWidthProperty().bind(this.widthProperty());
		wrapperPane.prefHeightProperty().bind(this.heightProperty());

		mediaPlayer = new MediaPlayer(video);
		mediaPlayer.setOnReady(new Runnable() {

			@Override
			public void run() {
				ready.countDown();
			}

		});
		mediaPlayer.setAutoPlay(true);
		MediaView mediaView = new MediaView(mediaPlayer);
		mediaView.fitWidthProperty().bind(widthProperty());
		mediaView.fitHeightProperty().bind(heightProperty());

		wrapperPane.setCenter(mediaView);

		setStyle("-fx-background-color: #000000;");
		getChildren().add(mediaView);

		if (waitForLoad) {
			// Wait until the duration of the video is known. Must not be called on the JavaFX
			// thread, because onReady is called there. Gives up if the video cannot be loaded.
			mediaPlayer.setOnError(ready::countDown);
			try {
				ready.await(WAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	public void paneWillDisappear() {
		super.paneWillDisappear();
		if (mediaPlayer != null) {

			mediaPlayer.stop();
			mediaPlayer = null;
		}
	}

}
