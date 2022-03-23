/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football.person;

import com.google.gson.annotations.Expose;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Date;

public class Player extends Person implements Comparable<Player> {
    private SimpleIntegerProperty siNumber;
    @Expose
    private int iNumber;
    @Expose
    private Position ePosition;
    @Expose
    private String sImagePath;

    public Player(String sFirstname, String sLastname, int iNumber, Position ePosition) {
        this(sFirstname, sLastname, iNumber, ePosition, null);
    }

    public Player(String sFirstname, String sLastname, int iNumber, Position ePosition, Date dBirthday) {
        super(sFirstname, sLastname, dBirthday);
        this.siNumber = new SimpleIntegerProperty(iNumber);
        this.iNumber = iNumber;
        this.ePosition = ePosition;
        sImagePath = null;
    }

    /**
     * Returns the sFirstname.
     *
     * @return the sFirstname
     */
    public int getsiNumber() {
        if (siNumber == null)
            this.siNumber = new SimpleIntegerProperty(iNumber);
        return siNumber.get();
    }

    /**
     * Sets the sFirstname to the new value.
     *
     * @param iNumber the sFirstname to set
     */
    public void setsiNumber(int iNumber) {
        this.siNumber.set(iNumber);
        this.iNumber = iNumber;
    }

    /**
     * Returns the iNumber.
     *
     * @return the iNumber
     */
    public int getiNumber() {
        return iNumber;
    }

    /**
     * Sets the iNumber to the new value.
     *
     * @param iNumber the iNumber to set
     */
    public void setiNumber(int iNumber) {
        this.siNumber.set(iNumber);
        this.iNumber = iNumber;
    }

    /**
     * Returns the ePosition.
     *
     * @return the ePosition
     */
    public Position getePosition() {
        return ePosition;
    }

    /**
     * Sets the ePosition to the new value.
     *
     * @param ePosition the ePosition to set
     */
    public void setePosition(Position ePosition) {
        this.ePosition = ePosition;
    }

    /**
     * Returns the sImagePath.
     *
     * @return the sImagePath
     */
    public String getsImagePath() {
        return sImagePath;
    }

    /**
     * Sets the sImagePath to the new value.
     *
     * @param sImagePath the sImagePath to set
     */
    public void setsImagePath(String sImagePath) {
        this.sImagePath = sImagePath;
    }

    public IntegerProperty siNumberProperty() {
        return siNumber;
    }

    @Override
    public String toString() {
        return iNumber + " - " + sFirstnameGson + " " + sLastnameGson;
    }

    @Override
    public int compareTo(Player otherPerson) {
        if (iNumber == otherPerson.getiNumber())
            return 0;
        return iNumber > otherPerson.getiNumber() ? 1 : -1;
    }

    public void callAfterDeserialization() {
        super.callAfterDeserialization();
        this.siNumber = new SimpleIntegerProperty(iNumber);
    }
}
