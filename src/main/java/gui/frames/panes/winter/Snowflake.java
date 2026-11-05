package gui.frames.panes.winter;

import java.util.Random;

import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import system.settings.Settings;

public class Snowflake extends Circle {
	private static Random random = new Random();
	private final static int OFFSET = 200;
	private Settings settings;
	private TranslateTransition fallAnimation;

	public Snowflake(Settings settings) {
		super(random.nextInt(settings.scalingMode.getLayoutWidth(settings.mainFrame)), random.nextInt(OFFSET), random.nextDouble() * 3);
		this.settings = settings;
		setCenterX(random.nextInt(settings.scalingMode.getLayoutWidth(settings.mainFrame)));

		Color color = Color.rgb(255, 255, 255, random.nextDouble());
		setFill(color);
	}

	public void animate() {
		int duration = (10 + random.nextInt(50)) * 1000;
		fallAnimation = new TranslateTransition(Duration.millis(duration), this);
        fallAnimation.setFromY(-OFFSET);
		fallAnimation.setByY(50);
		fallAnimation.setToY(settings.scalingMode.getLayoutHeight(settings.mainFrame));
		fallAnimation.setByX(random.nextDouble() * getCenterX());
		fallAnimation.setCycleCount(Timeline.INDEFINITE);
		fallAnimation.play();
	}

	public void stop() {
		if (fallAnimation != null)
			fallAnimation.stop();
	}
}
