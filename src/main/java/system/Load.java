package system;

import com.google.gson.reflect.TypeToken;
import exceptions.FileDidNotExistException;
import exceptions.FileNotCreatedException;
import football.League;
import gui.animation.Animations;
import gui.frames.frameFunctions.OtherFramesFunctions;
import language.Languages;
import system.settings.Frame;
import system.settings.Settings;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Load {
    private static Load load;

    private Load() {

    }

    public static Load getLoad() {
        if (load == null)
            load = new Load();
        return load;
    }

    public List<League> loadLeagues() throws IOException, FileNotCreatedException {
        try {
            Type collectionType = new TypeToken<Collection<League>>() {
            }.getType();
            return FileHandling.getFileHandling().loadTFromFile(Defaults.S_LEAGUES_FILE_NAME, collectionType);
        } catch (FileDidNotExistException e1) {

            List<League> newLeague = new ArrayList<League>();

            FileHandling.getFileHandling().saveTToFile(newLeague, Defaults.S_LEAGUES_FILE_NAME);
            return newLeague;
        }
    }

    public Settings loadSettings() throws IOException, FileNotCreatedException {
        try {
            Settings settings = FileHandling.getFileHandling().loadTFromFile(Defaults.S_SETTINGS_FILE_NAME, Settings.class);

            settings.update();
            return settings;
        } catch (FileDidNotExistException e1) {
            Frame mainFrame = new Frame(Defaults.FRAME_ACTIVATED, Defaults.FRAME_WIDTH, Defaults.FRAME_HEIGHT,
                    Defaults.FRAME_START_X, Defaults.FRAME_START_Y, null);

            Frame otherFrames[] = new Frame[2];
            otherFrames[0] = new Frame(Defaults.OTHER_FRAME_1_ACTIVATED, Defaults.OTHER_FRAME_1_WIDTH,
                    Defaults.OTHER_FRAME_1_HEIGHT, Defaults.OTHER_FRAME_1_START_X, Defaults.OTHER_FRAME_1_START_Y,
                    OtherFramesFunctions.DATE);
            otherFrames[1] = new Frame(Defaults.OTHER_FRAME_2_ACTIVATED, Defaults.OTHER_FRAME_2_WIDTH,
                    Defaults.OTHER_FRAME_2_HEIGHT, Defaults.OTHER_FRAME_2_START_X, Defaults.OTHER_FRAME_2_START_Y,
                    OtherFramesFunctions.CURRENT_TIME);

            Settings newSettings = new Settings(Main.VERSION, mainFrame, otherFrames, Languages.GERMAN, Animations.FADE,
                    Defaults.HALFTIME_1_LENGTH, Defaults.HALFTIME_2_LENGTH, Defaults.HALFTIME_3_LENGTH, Defaults.HALFTIME_4_LENGTH, Defaults.TIME_BETWEEN_ADVERTISING_IN_MILISECONDS,
                    Defaults.TIME_BETWEEN_REPLACEMENT_IN_MILISECONDS, Defaults.TIME_UNTIL_GO_TO_MAIN_IN_MILISECONDS, Defaults.IS_SNOWING, Defaults.THEME);

            FileHandling.getFileHandling().saveTToFile(newSettings, Defaults.S_SETTINGS_FILE_NAME);
            return newSettings;
        }

    }
}
