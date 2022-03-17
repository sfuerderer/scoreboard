/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football.match;

import exceptions.FileDidNotExistException;
import football.Team;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import message.ErrorMsg;
import system.FileHandling;
import system.observer.Observer;

import java.io.IOException;

public class Match implements Observer {
    private Team tTeamHome;
    private Team tTeamGuest;
    private int iGoalsHome;
    private int iGoalsGuest;
    private int iHalftime1Length;
    private int iHalftime2Length;
    private int iHalftime3Length;
    private int iHalftime4Length;
    private int iVisitors;
    private int iCornersHome;
    private int iCornersGuest;
    private long lStartTimeMillis;
    private Statistic sStatistic;
    private Halftime eWhichHalftime;
    private Lineup lLineup;
    public StringProperty sResult;
    public StringProperty sCorners;
    private boolean bPlaytimeRunning;

    public Match(Team tTeamHome, Team tTeamGuest, int iHalftime1Length, int iHalftime2Length, int iHalftime3Length,
                 int iHalftime4Length) {
        this.tTeamHome = tTeamHome;
        this.tTeamGuest = tTeamGuest;
        this.iHalftime1Length = iHalftime1Length;
        this.iHalftime2Length = iHalftime2Length;
        this.iHalftime3Length = iHalftime3Length;
        this.iHalftime4Length = iHalftime4Length;
        this.iGoalsHome = 0;
        this.iGoalsGuest = 0;
        this.iVisitors = 0;
        this.iCornersGuest = 0;
        this.iCornersHome = 0;
        sStatistic = new Statistic();
        eWhichHalftime = Halftime.FIRST;
        lLineup = new Lineup();
        sResult = new SimpleStringProperty(iGoalsHome + " : " + iGoalsGuest);
        sCorners = new SimpleStringProperty(iCornersHome + " : " + iCornersGuest);
        bPlaytimeRunning = false;
    }

    public void addGoal(Teams team) {
        switch (team) {
            case HOME:
                iGoalsHome++;
                break;
            case GUEST:
                iGoalsGuest++;
                break;
        }
        sResult.set(iGoalsHome + " : " + iGoalsGuest);
    }

    public void removeGoal(Teams team) {
        switch (team) {
            case HOME:
                if (iGoalsHome > 0)
                    iGoalsHome--;
                break;
            case GUEST:
                if (iGoalsGuest > 0)
                    iGoalsGuest--;
                break;
        }
        sResult.set(iGoalsHome + " : " + iGoalsGuest);
    }

    public void addCorner(Teams team) {
        switch (team) {
            case HOME:
                iCornersHome++;
                break;
            case GUEST:
                iCornersGuest++;
                break;
        }
        sCorners.set(iCornersHome + " : " + iCornersGuest);
    }

    public void removeCorner(Teams team) {
        switch (team) {
            case HOME:
                if (iCornersHome > 0)
                    iCornersHome--;
                break;
            case GUEST:
                if (iCornersGuest > 0)
                    iCornersGuest--;
                break;
        }
        sCorners.set(iCornersHome + " : " + iCornersGuest);
    }

    /**
     * Returns the tTeamHome.
     *
     * @return the tTeamHome
     */
    public Team gettTeamHome() {
        return tTeamHome;
    }

    /**
     * Sets the tTeamHome to the new value.
     *
     * @param tTeamHome the tTeamHome to set
     */
    public void settTeamHome(Team tTeamHome) {
        this.tTeamHome = tTeamHome;
    }

    /**
     * Returns the tTeamGuest.
     *
     * @return the tTeamGuest
     */
    public Team gettTeamGuest() {
        return tTeamGuest;
    }

    /**
     * Sets the tTeamGuest to the new value.
     *
     * @param tTeamGuest the tTeamGuest to set
     */
    public void settTeamGuest(Team tTeamGuest) {
        this.tTeamGuest = tTeamGuest;
    }

    /**
     * Returns the iGoalsHome.
     *
     * @return the iGoalsHome
     */
    public int getiGoalsHome() {
        return iGoalsHome;
    }

    /**
     * Sets the iGoalsHome to the new value.
     *
     * @param iGoalsHome the iGoalsHome to set
     */
    public void setiGoalsHome(int iGoalsHome) {
        this.iGoalsHome = iGoalsHome;
    }

    /**
     * Returns the iGoalsGuest.
     *
     * @return the iGoalsGuest
     */
    public int getiGoalsGuest() {
        return iGoalsGuest;
    }

    /**
     * Sets the iGoalsGuest to the new value.
     *
     * @param iGoalsGuest the iGoalsGuest to set
     */
    public void setiGoalsGuest(int iGoalsGuest) {
        this.iGoalsGuest = iGoalsGuest;
    }

    /**
     * Returns the sStatistic.
     *
     * @return the sStatistic
     */
    public Statistic getsStatistic() {
        return sStatistic;
    }

    /**
     * Sets the sStatistic to the new value.
     *
     * @param sStatistic the sStatistic to set
     */
    public void setsStatistic(Statistic sStatistic) {
        this.sStatistic = sStatistic;
    }

    /**
     * Returns the eWhichHalftime.
     *
     * @return the eWhichHalftime
     */
    public Halftime geteWhichHalftime() {
        return eWhichHalftime;
    }

    /**
     * Sets the eWhichHalftime to the new value.
     *
     * @param eWhichHalftime the eWhichHalftime to set
     */
    public void seteWhichHalftime(Halftime eWhichHalftime) {
        this.eWhichHalftime = eWhichHalftime;
    }

    /**
     * Returns the lLineup.
     *
     * @return the lLineup
     */
    public Lineup getlLineup() {
        return lLineup;
    }

    /**
     * Sets the lLineup to the new value.
     *
     * @param lLineup the lLineup to set
     */
    public void setlLineup(Lineup lLineup) {
        this.lLineup = lLineup;
    }

    /**
     * Returns the lStartTimeMillis.
     *
     * @return the lStartTimeMillis
     */
    public long getlStartTimeMillis() {
        return lStartTimeMillis;
    }

    /**
     * Sets the lStartTimeMillis to the new value.
     *
     * @param lStartTimeMillis the lStartTimeMillis to set
     */
    public void setlStartTimeMillis(long lStartTimeMillis) {
        this.lStartTimeMillis = lStartTimeMillis;
    }

    /**
     * Returns the iHalftimeLength.
     *
     * @return the iHalftimeLength
     */
    public int getiHalftimeLength() {
        switch (eWhichHalftime) {
            case FIRST:
                return iHalftime1Length;
            case SECOND:
                return iHalftime2Length;
            case THIRD:
                return iHalftime3Length;
            case FOURTH:
                return iHalftime4Length;
        }

        return 0;
    }

    public int getTimePassedToHalftime() {
        switch (eWhichHalftime) {
            case FIRST:
                return 0;
            case SECOND:
                return iHalftime1Length;
            case THIRD:
                return iHalftime1Length + iHalftime2Length;
            case FOURTH:
                return iHalftime1Length + iHalftime2Length + iHalftime3Length;
        }

        return 0;
    }

    /**
     * Returns the iHalftimeLength.
     *
     * @return the iHalftimeLength
     */
    public int getiHalftime1Length() {
        return iHalftime1Length;
    }

    /**
     * Sets the iHalftimeLength to the new value.
     *
     * @param iHalftime1Length the iHalftimeLength to set
     */
    public void setiHalftime1Length(int iHalftime1Length) {
        this.iHalftime1Length = iHalftime1Length;
    }

    /**
     * Returns the iHalftimeLength.
     *
     * @return the iHalftimeLength
     */
    public int getiHalftime2Length() {
        return iHalftime2Length;
    }

    /**
     * Sets the iHalftimeLength to the new value.
     *
     * @param iHalftime2Length the iHalftimeLength to set
     */
    public void setiHalftime2Length(int iHalftime2Length) {
        this.iHalftime2Length = iHalftime2Length;
    }

    /**
     * Returns the iHalftimeLength.
     *
     * @return the iHalftimeLength
     */
    public int getiHalftime3Length() {
        return iHalftime3Length;
    }

    /**
     * Sets the iHalftimeLength to the new value.
     *
     * @param iHalftime3Length the iHalftimeLength to set
     */
    public void setiHalftime3Length(int iHalftime3Length) {
        this.iHalftime3Length = iHalftime3Length;
    }

    /**
     * Returns the iHalftimeLength.
     *
     * @return the iHalftimeLength
     */
    public int getiHalftime4Length() {
        return iHalftime4Length;
    }

    /**
     * Sets the iHalftimeLength to the new value.
     *
     * @param iHalftime4Length the iHalftimeLength to set
     */
    public void setiHalftime4Length(int iHalftime4Length) {
        this.iHalftime4Length = iHalftime4Length;
    }

    /**
     * Returns the iVisitors.
     *
     * @return the iVisitors
     */
    public int getiVisitors() {
        return iVisitors;
    }

    /**
     * Sets the iVisitors to the new value.
     *
     * @param iVisitors the iVisitors to set
     */
    public void setiVisitors(int iVisitors) {
        this.iVisitors = iVisitors;
    }

    /**
     * Returns the iCornersHome.
     *
     * @return the iCornersHome
     */
    public int getiCornersHome() {
        return iCornersHome;
    }

    /**
     * Returns the iCornersGuest.
     *
     * @return the iCornersGuest
     */
    public int getiCornersGuest() {
        return iCornersGuest;
    }

    public boolean isbPlaytimeRunning() {
        return bPlaytimeRunning;
    }

    public void setbPlaytimeRunning(boolean bPlaytimeRunning) {
        this.bPlaytimeRunning = bPlaytimeRunning;
    }

    public void nextHalf() {
        switch (eWhichHalftime) {
            case FIRST:
                eWhichHalftime = Halftime.SECOND;
                break;
            case SECOND:
                eWhichHalftime = Halftime.THIRD;
                break;
            case THIRD:
                eWhichHalftime = Halftime.FOURTH;
                break;
            default:
                break;
        }
    }

    public void start() {
        bPlaytimeRunning = true;
        lStartTimeMillis = System.currentTimeMillis();
    }

    public void stop() {
        bPlaytimeRunning = false;
    }

    @Override
    public void update() {
        try {
            tTeamHome = loadTeamFromPath(tTeamHome.getsOwnPath());
            tTeamGuest = loadTeamFromPath(tTeamGuest.getsOwnPath());

        } catch (IOException e) {
            ErrorMsg.showErrorMessage(109);
        }

    }

    private Team loadTeamFromPath(String sPath) throws IOException {
        try {
            return FileHandling.getFileHandling().loadTFromFile(sPath, Team.class);
        } catch (FileDidNotExistException e1) {
            ErrorMsg.showErrorMessage(109);
        }

        return null;
    }
}
