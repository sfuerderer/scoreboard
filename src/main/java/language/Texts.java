/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package language;

import java.util.HashMap;

import exceptions.TextInLanguageNotFoundException;
import exceptions.TextNotFoundException;

/**
 * A class which represent all texts used in the software.
 *
 * @author Sebastian Fürderer
 * @version 1.0.0
 * @date 19.06.2020
 *
 */
public class Texts {
	private String version;
	private HashMap<String, Text> hmTexts;
	
	public Texts() {
		this("1.0.0");
	}
	
	public Texts(String version) {
		this.version = version;
		hmTexts = new HashMap<String, Text>();
	}
	
	/**
	 * Adds a new text to the list.
	 * 
	 * @param sKey The text key.
	 * @param hmText The text in all languages.
	 */
	public void addNewText(String sKey, HashMap<Languages,String> hmText) {
		Text tText = new Text(hmText);
		hmTexts.put(sKey, tText);
	}

	/**
	 * Loads the text with the corresponding id and returns it in the specified language.
	 * 
	 * @param eLanguage The language.
	 * @param sKey The text key.
	 * @return The translated text.
	 * @throws TextNotFoundException
	 * @throws TextInLanguageNotFoundException
	 */
	public String getTextInLanguage(Languages eLanguage, String sKey) throws TextNotFoundException, TextInLanguageNotFoundException {
		Text tLoadText = hmTexts.get(sKey);
		if(tLoadText == null)
			throw new TextNotFoundException();
		return tLoadText.getTextInLanguage(eLanguage);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
