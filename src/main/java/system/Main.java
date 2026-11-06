package system;

import background.services.ServicePlaytime;
import background.threads.AdvertismentThread;
import football.match.Match;
import gui.observer.ConcreteSubject;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Main {
    private static Match match;
    private static AdvertismentThread adThread;
    private static ConcreteSubject subject;
    private static ServicePlaytime servicePlaytime;
    public static final String VERSION = "2.0.0-beta.1";

    public static void main(String[] args) {
        new Main(args);
    }

    public Main(String args[]) {
        Boot boot = new Boot();
        subject = new ConcreteSubject();
        boot.boot();

    }


    /**
     * Sets the current match and starts its playtime. The playtime and observer of a
     * previous match are stopped, so only one playtime thread is running at a time.
     * Has to be called on the JavaFX thread.
     *
     * @param onHalftimeEnded called on the JavaFX thread when a halftime ended, may be null
     */
    public static void setMatch(Match newMatch, Runnable onHalftimeEnded) {
        if (servicePlaytime != null)
            servicePlaytime.cancel();
        if (match != null)
            subject.removeObserver(match);

        match = newMatch;
        subject.addObserver(match);

        servicePlaytime = new ServicePlaytime(match, onHalftimeEnded);
        servicePlaytime.start();
    }

    public static Match getMatch() {
        return match;
    }

    public static void teamsChanged() {
        synchronized (subject) {
            subject.notifyAllObserver();
        }
    }

    public static void setAdThread(AdvertismentThread adThread) {
        Main.adThread = adThread;
    }

    public static AdvertismentThread getAdThread() {
        return adThread;
    }

    public static ServicePlaytime getServicePlaytime() {
        return servicePlaytime;
    }

    public static Font getMainFont() {
        return getMainFont(70);
    }

    public static Font getMainFont(int size) {
        return getMainFont(FontWeight.BOLD, size);
    }

    /**
     * Font of the scoreboards (Barlow Condensed, SIL Open Font License). Its digits have different widths,
     * so numbers that change while shown (clock, result) belong in a {@code FixedDigitsLabel}.
     */
    public static Font getMainFont(FontWeight weight, int size) {
        return Fonts.loadFontFromResource(getMainFontFile(weight), "ttf", size);
    }

    static String getMainFontFile(FontWeight weight) {
        return "BarlowCondensed-" + getMainFontStyle(weight);
    }

    private static String getMainFontStyle(FontWeight weight) {
        switch (weight) {
            case THIN:
            case EXTRA_LIGHT:
            case LIGHT:
            case NORMAL:
                return "Regular";
            case MEDIUM:
                return "Medium";
            case SEMI_BOLD:
                return "SemiBold";
            case BOLD:
                return "Bold";
            default:
                return "ExtraBold";
        }
    }


}
