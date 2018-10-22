package com.anasabuhussein.json.convertmodel;

import com.anasabuhussein.json.JsonArrayObject;
import com.anasabuhussein.json.JsonException;
import com.anasabuhussein.json.JsonObject;

public class JsonModelUtil {

	private JsonModelUtil() {
		super();
	}

	/**
	 * This method will chek if first char in mainString is object or not.
	 * 
	 * @throws JsonException
	 **/
	public static JsonObject isJsonObject(String mainString) throws JsonException {
		if (getChars(mainString) == '{')
			return JsonObject.fromString(mainString);
		return null;
	}

	/**
	 * This method will chek if first char in mainString is Array or not.
	 * 
	 * @throws JsonException
	 **/
	public static JsonArrayObject<?> isJsonArrayObject(String mainString) throws JsonException {
		if (getChars(mainString) == '[')
			return JsonArrayObject.fromString(mainString);
		return null;
	}

	/**
	 * This method will get first chars
	 **/
	private static int getChars(String mainString) {
		int index = 0;
		while (index < mainString.length()) {
			int c = mainString.charAt(index++);

			switch (c) {
			case ' ':
			case '\t':
			case '\r':
			case '\n':
				continue;
			default:
				return c;
			}
		}
		return -1;
	}

}
