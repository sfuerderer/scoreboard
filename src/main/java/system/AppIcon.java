/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system;

import java.awt.Taskbar;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ListChangeListener;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * The app's own icon instead of the default Java icon. Source: tools/app-icon/app-icon.svg.
 */
public final class AppIcon {
	static final int[] SIZES = {16, 32, 64, 128, 256, 512};
	static final String DOCK_ICON = "/images/app-icon-dock.png";

	private AppIcon() {
	}

	static String getResourceName(int iSize) {
		return "/images/app-icon-" + iSize + ".png";
	}

	/**
	 * Sets the icon of the macOS Dock (Windows: taskbar). JavaFX cannot do this itself, so AWT is used.
	 * Has to be called before the JavaFX application is launched. Fails silently, the icon is only cosmetic.
	 */
	public static void setTaskbarIcon() {
		try (InputStream in = AppIcon.class.getResourceAsStream(DOCK_ICON)) {
			if (in == null || !Taskbar.isTaskbarSupported())
				return;
			Taskbar taskbar = Taskbar.getTaskbar();
			if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE))
				taskbar.setIconImage(javax.imageio.ImageIO.read(in));
		} catch (Exception | Error e) {
			// e.g. headless environment or unsupported platform: keep the default icon
		}
	}

	/**
	 * Gives every window (control frame, dialogs, settings, ...) the app icon in its title bar and the taskbar.
	 * Has to be called on the JavaFX application thread.
	 */
	public static void applyToAllWindows() {
		List<Image> lIcons = new ArrayList<Image>();
		for (int iSize : SIZES)
			lIcons.add(new Image(AppIcon.class.getResource(getResourceName(iSize)).toExternalForm()));

		for (Window window : Window.getWindows())
			addIcons(window, lIcons);
		Window.getWindows().addListener((ListChangeListener<Window>) change -> {
			while (change.next()) {
				for (Window window : change.getAddedSubList())
					addIcons(window, lIcons);
			}
		});
	}

	private static void addIcons(Window window, List<Image> lIcons) {
		if (window instanceof Stage && ((Stage) window).getIcons().isEmpty())
			((Stage) window).getIcons().setAll(lIcons);
	}
}
