/************************************************
 * Copyright (c) 2021 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package language;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;

import exceptions.LanguageNotLoadedException;
import exceptions.TextInLanguageNotFoundException;
import exceptions.TextNotFoundException;
import message.ErrorMsg;
import system.settings.Settings;

/**
 * A class to manage all languages supported in the software.
 *
 * @author Sebastian Fürderer
 * @version 1.1.0
 * @date 02.07.2021
 *
 */
public class Language {
	private static Texts tTexts;
	private static Settings sSettings;

	public static void init(Settings sSettings) throws LanguageNotLoadedException {
		Language.sSettings = sSettings;
		tTexts = loadTexts();
	}

	/**
	 * Gets the text to the given key.
	 * 
	 * @param sKey The text key.
	 * @return The translated text.
	 */
	public static String getText(String sKey) {
		try {
			return tTexts.getTextInLanguage(sSettings.sSystemLanguage, sKey);
		} catch (TextNotFoundException e) {
			ErrorMsg.showErrorMessage(104);
		} catch (TextInLanguageNotFoundException e) {
			ErrorMsg.showErrorMessage(105);
		}
		return "###";
	}

	/**
	 * Loads the language file and map it to the Texts class.
	 * 
	 * @return The Texts class.
	 * @throws IOException
	 */
	private static Texts loadTexts() throws LanguageNotLoadedException{
		try {
			Gson gson = new Gson();

			InputStream inStream = Language.class.getResourceAsStream("/config/language.json");
			Reader reade = new InputStreamReader(inStream, "UTF-8");
			return gson.fromJson(reade, Texts.class);
		} catch (Exception e) {
			throw new LanguageNotLoadedException();
		}
	}

}
