/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football.person;

import java.util.Date;

import com.google.gson.annotations.Expose;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person{
	private SimpleStringProperty sFirstname;
	private SimpleStringProperty sLastname;
	@Expose
	protected String sFirstnameGson;
	@Expose
	protected String sLastnameGson;
	@Expose
	private Date dBirthday;
	
	public Person(String sFirstname, String sLastname) {
		this(sFirstname, sLastname, null);
	}
	
	public Person(String sFirstname, String sLastname, Date dBirthday) {
		super();
		this.sFirstname = new SimpleStringProperty(sFirstname);
		this.sLastname = new SimpleStringProperty(sLastname);
		this.sFirstnameGson = sFirstname;
		this.sLastnameGson = sLastname;
		this.dBirthday = dBirthday;
	}

	/**
	 * Returns the sFirstname.
	 *
	 * @return the sFirstname
	 */
	public String getsFirstname() {
		if(sFirstname == null)
			this.sFirstname = new SimpleStringProperty(sFirstnameGson);
		return sFirstname.get();
	}

	/**
	 * Sets the sFirstname to the new value.
	 *
	 * @param sFirstname the sFirstname to set
	 */
	public void setsFirstname(String sFirstname) {
		this.sFirstname.set(sFirstname);
		this.sFirstnameGson = sFirstname;
	}

	/**
	 * Returns the sLastname.
	 *
	 * @return the sLastname
	 */
	public String getsLastname() {
		if(sLastname == null)
			this.sLastname = new SimpleStringProperty(sLastnameGson);
		return sLastname.get();
	}

	/**
	 * Sets the sLastname to the new value.
	 *
	 * @param sLastname the sLastname to set
	 */
	public void setsLastname(String sLastname) {
		this.sLastname.set(sLastname);
		this.sLastnameGson = sLastname;
	}

	/**
	 * Returns the sFirstnameGson.
	 *
	 * @return the sFirstnameGson
	 */
	public String getsFirstnameGson() {
		return sFirstnameGson;
	}

	/**
	 * Sets the sFirstnameGson to the new value.
	 *
	 * @param sFirstnameGson the sFirstnameGson to set
	 */
	public void setsFirstnameGson(String sFirstnameGson) {
		this.sFirstnameGson = sFirstnameGson;
	}

	/**
	 * Returns the sLastnameGson.
	 *
	 * @return the sLastnameGson
	 */
	public String getsLastnameGson() {
		return sLastnameGson;
	}

	/**
	 * Sets the sLastnameGson to the new value.
	 *
	 * @param sLastnameGson the sLastnameGson to set
	 */
	public void setsLastnameGson(String sLastnameGson) {
		this.sLastnameGson = sLastnameGson;
	}

	/**
	 * Returns the dBirthday.
	 *
	 * @return the dBirthday
	 */
	public Date getdBirthday() {
		return dBirthday;
	}

	/**
	 * Sets the dBirthday to the new value.
	 *
	 * @param dBirthday the dBirthday to set
	 */
	public void setdBirthday(Date dBirthday) {
		this.dBirthday = dBirthday;
	}
	
	public StringProperty sFirstnameProperty() {
        return sFirstname;
    }
	
	public StringProperty sLastnameProperty() {
        return sLastname;
    }

	public void callAfterDeserialization() {
		this.sFirstname = new SimpleStringProperty(sFirstnameGson);
		this.sLastname = new SimpleStringProperty(sLastnameGson);
	}

}
