package gui.frames.settingsFrame;

import javafx.scene.layout.StackPane;
import system.settings.Settings;

abstract public class SettingsComponent extends StackPane{
	abstract public void saveToSettings();
	abstract public void reset();
	abstract public void restoreDefaultValues();
	abstract public String getTitleKey();
	
	protected Settings sSettings;
	
	public SettingsComponent(Settings sSettings) {
		this.sSettings = sSettings;
	}
}
