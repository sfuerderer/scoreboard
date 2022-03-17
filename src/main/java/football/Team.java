/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football;

import com.google.gson.annotations.Expose;
import exceptions.FileNotCreatedException;
import football.person.Coach;
import football.person.CoachType;
import football.person.Player;
import message.ErrorMsg;
import system.Cleanup;
import system.FileHandling;
import system.Resetable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Team implements Resetable {
    @Expose
    private String sTeamName;
    @Expose
    private List<Player> lPlayers;
    @Expose
    private List<Coach> lCoaches;
    @Expose
    private String sPathToLogo;
    @Expose
    private String sOwnPath;
    // private ImageIcon iiLogo;

    public Team(String sTeamName, List<Player> lPlayers, List<Coach> lCoaches, String sPathToLogo, String sOwnPath) {
        this.sTeamName = sTeamName;
        this.lPlayers = lPlayers;
        this.lCoaches = lCoaches;
        this.sPathToLogo = sPathToLogo;
        this.sOwnPath = sOwnPath;
    }

    /**
     * Returns the sTeamName.
     *
     * @return the sTeamName
     */
    public String getsTeamName() {
        return sTeamName;
    }

    /**
     * Sets the sTeamName to the new value.
     *
     * @param sTeamName the sTeamName to set
     */
    public void setsTeamName(String sTeamName) {
        this.sTeamName = sTeamName;
    }

    /**
     * Returns the lPlayers.
     *
     * @return the lPlayers
     */
    public List<Player> getlPlayers() {
        return lPlayers;
    }

    /**
     * Sets the lPlayers to the new value.
     *
     * @param lPlayers the lPlayers to set
     */
    public void setlPlayers(List<Player> lPlayers) {
        this.lPlayers = lPlayers;
    }

    public void addPlayer(Player newPlayer) {
        lPlayers.add(newPlayer);
    }

    public void removePlayer(Player playerToRemove) {
        Cleanup.getCleanup().removeFile(playerToRemove.getsImagePath());
        lPlayers.remove(playerToRemove);
    }

    /**
     * Returns the pCoach.
     *
     * @return the pCoach
     */
    public List<Coach> getlCoaches() {
        fixCoachesNotExist();
        return lCoaches;
    }

    public void setlCoaches(List<Coach> lCoaches) {
        this.lCoaches = lCoaches;
    }

    public void addCoach(Coach newCoach) {
        fixCoachesNotExist();
        lCoaches.add(newCoach);
    }

    public void removeCoach(Coach coachToRemove) {
        fixCoachesNotExist();
        Cleanup.getCleanup().removeFile(coachToRemove.getsImagePath());
        lCoaches.remove(coachToRemove);
    }

    private void fixCoachesNotExist() {
        if (lCoaches == null) {
            this.lCoaches = new ArrayList<Coach>();
            this.save();
        }
    }

    /**
     * Returns the sPathToLogo.
     *
     * @return the sPathToLogo
     */
    public String getsPathToLogo() {
        return sPathToLogo;
    }

    /**
     * Sets the sPathToLogo to the new value.
     *
     * @param sPathToLogo the sPathToLogo to set
     */
    public void setsPathToLogo(String sPathToLogo) {
        this.sPathToLogo = sPathToLogo;
    }

    /**
     * Returns the sOwnPath.
     *
     * @return the sOwnPath
     */
    public String getsOwnPath() {
        return sOwnPath;
    }

    /**
     * Sets the sOwnPath to the new value.
     *
     * @param sOwnPath the sOwnPath to set
     */
    public void setsOwnPath(String sOwnPath) {
        this.sOwnPath = sOwnPath;
    }

    /**
     * Returns the path to the folder of the team.
     *
     * @return The path.
     */
    public String getPathToFolder() {
        String[] elements = sOwnPath.split("/");
        String result = "";
        for (int i = 0; i < elements.length - 1; i++)
            result += elements[i] + (i == elements.length - 2 ? "" : "/");

        return result;
    }

    public Coach getHeadCoach() {
        return lCoaches == null ? null : lCoaches.stream().filter(coach -> coach.geteCoachType() == CoachType.HEADCOACH).findFirst().orElse(null);
    }

    @Override
    public void reset() {
        lPlayers.clear();
        Cleanup.getCleanup().cleanPlayerImagesFolder(this);
        Cleanup.getCleanup().removeFile(sPathToLogo);
        sPathToLogo = null;
        save();
    }

    /**
     * Saves the team to the corresponding file.
     */
    public void save() {
        try {
            FileHandling.getFileHandling().saveTToFile(this, sOwnPath);
        } catch (FileNotCreatedException e) {
            ErrorMsg.showErrorMessage(102);
        } catch (IOException e) {
            ErrorMsg.showErrorMessage(102);
        }
    }

    @Override
    public String toString() {
        return sTeamName;
    }
}
