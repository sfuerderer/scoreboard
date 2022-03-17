package system;

import exceptions.FileNotCreatedException;
import exceptions.LanguageNotLoadedException;
import gui.GUIControlling;
import language.Language;
import message.ErrorMsg;
import system.settings.Settings;

import java.io.IOException;

public class Boot {

    /**
     * Method that start all necessary programs or init them.
     * Also do some checks if all is correct.
     */
    public void boot() {
        ErrorMsg.init();

        try {
            Settings settings = Load.getLoad().loadSettings();
            Language.init(settings);

            Cleanup.getCleanup().cleanFolderStructure();

            GUIControlling gui = new GUIControlling();
            gui.setSettings(settings);
            gui.start();
        } catch (FileNotCreatedException fnce) {
            ErrorMsg.showErrorMessage(115, true);
        } catch (IOException ioe) {
            ErrorMsg.showErrorMessage(115, true);
        } catch (LanguageNotLoadedException e) {
            ErrorMsg.showErrorMessage(103, true);
        }

    }
}
