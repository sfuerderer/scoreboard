package system.settings;

public enum Themes {
    DEFAULT(0), MODERN(1);

    private int iId;

    Themes(int iId) {
        this.iId = iId;
    }

    public int getIId() {
        return iId;
    }
}
