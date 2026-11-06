/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.scoreboards;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.transform.Scale;

/**
 * Window content of a scoreboard. The content is laid out in a fixed layout size and stretched to the
 * size of this pane, so the scoreboard shows the same picture regardless of the configured window size.
 */
class ScaledPane extends Pane {
	private final Region content;
	private final Scale scale = new Scale();

	ScaledPane(Region content) {
		super(content);
		this.content = content;
		setStyle("-fx-background-color: #000000;");
		content.getTransforms().add(scale);
	}

	void setLayoutSize(int iLayoutWidth, int iLayoutHeight) {
		content.setMinSize(iLayoutWidth, iLayoutHeight);
		content.setPrefSize(iLayoutWidth, iLayoutHeight);
		content.setMaxSize(iLayoutWidth, iLayoutHeight);
		scale.xProperty().bind(widthProperty().divide(iLayoutWidth));
		scale.yProperty().bind(heightProperty().divide(iLayoutHeight));
	}
}
