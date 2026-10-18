/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;

@SuppressWarnings("restriction")
public class PaneVideo extends PaneMainScoreboard {
	// TODO besser Lösung finden
	private static final long WAIT_TIMEOUT = 200;
	private MediaPlayer mediaPlayer;
	private boolean ready = false;

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
				ready = true;
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
			Thread dummy = new Thread(new Runnable() {

				@Override
				public void run() {
					while (!ready) {
						try {
							Thread.sleep(WAIT_TIMEOUT);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

			});

			try {
				dummy.start();
				dummy.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void paneWillDisappear() {
		super.paneWillDisappear();
		if (mediaPlayer != null) {

			mediaPlayer.stop();
		}
	}

	@Override
	public void paneDidDisappear() {
		super.paneDidDisappear();
		// Frees the native decoder and buffers, stop() alone keeps them
		if (mediaPlayer != null) {
			mediaPlayer.dispose();
			mediaPlayer = null;
		}
	}

}
