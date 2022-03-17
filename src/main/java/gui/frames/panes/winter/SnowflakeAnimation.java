package gui.frames.panes.winter;

import javafx.scene.layout.Pane;
import system.settings.Settings;

public class SnowflakeAnimation {
	private static final int COUNT_OF_SNOWFLAKES = 200;
	private static Snowflake flakes[] = new Snowflake[COUNT_OF_SNOWFLAKES];

	public SnowflakeAnimation(Pane rootPane, Settings settings) {

		for (int i = 0; i < COUNT_OF_SNOWFLAKES; i++) {
			flakes[i] = new Snowflake(settings);
			rootPane.getChildren().add(flakes[i]);
		}
	}

	public void animate() {
		for (int i = 0; i < COUNT_OF_SNOWFLAKES; i++) {
			flakes[i].animate();
		}
	}
}
