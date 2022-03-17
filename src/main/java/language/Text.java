/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package language;

import java.util.HashMap;

import exceptions.TextInLanguageNotFoundException;

/**
 * A class which represents only one Text in different languages.
 *
 * @author Sebastian Fürderer
 * @version 1.0.0
 * @date 19.06.2020
 *
 */
public class Text {
	/**
	 * <languageKey, Translation>
	 */
	private HashMap<Languages, String> hmText;
	
	public Text(HashMap<Languages, String> hmText) {
		this.hmText = hmText;
	}
	
	/**
	 * Returns the text in the requested Language.
	 * @param eLanguage The Language which should be returned.
	 * @return The text in the requested Language.
	 * @throws TextInLanguageNotFoundException
	 */
	public String getTextInLanguage(Languages eLanguage) throws TextInLanguageNotFoundException {
		String sResult = hmText.get(eLanguage);
		if(sResult == null)
			throw new TextInLanguageNotFoundException();
		return sResult;
	}
}
