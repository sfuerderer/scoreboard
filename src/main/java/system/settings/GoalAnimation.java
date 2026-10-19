package system.settings;

public enum GoalAnimation {
    ANIMATION_1(0, "tor_1"), ANIMATION_2(1, "tor_2");

    private String sFilename;
    private int iId;

    GoalAnimation(int iId, String sFilename) {
        this.sFilename = sFilename;
        this.iId = iId;
    }

    public String getFilename() {
        return sFilename;
    }

    public int getiId() {
        return iId;
    }
}
