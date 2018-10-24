package com.anasabuhussein.json;

/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This class will conver from string to json object or to json array object.
 * 
 * @author Anas Abu-Hussein
 **/
public class StringJsonBuilder {

	private String input;
	private int index = 0;

	public StringJsonBuilder(String input) {
		this.input = input;
	}

	public Object nextValue() throws JsonException {
		int c = getChars();
		Object object = null;
		switch (c) {
		case '{':
			object = readJson();
			break;
		case '[':
			object = readArray();
			break;
		case '\'':
		case '"':
			object = readString(c);
			break;
		default:
			object = readValue();
			break;
		}

		return object;
	}

	private Object readArray() throws JsonException {

		JsonArrayObject<Object> arrayObject = new JsonArrayObject<>();

		int endArray = getChars();
		if (endArray == ']') {
			return arrayObject;
		} else if (endArray == -1) {
			index--;
		} else {
			index--;
		}

		while (index < input.length()) {

			int c = getChars();

			if (c == ']') {
				return arrayObject;
			} else {
				--index;
			}

			if (c == ',')
				getChars();

			arrayObject.add(nextValue());
		}
		return null;
	}

	private Object readJson() throws JsonException {
		JsonObject jsonObject = new JsonObject();

		int endObject = getChars();
		if (endObject == '}') {
			return jsonObject;
		} else if (endObject == -1) {
			index--;
		} else {
			index--;
		}

		while (index < input.length()) {

			String key = (String) nextValue();

			int seperator = getChars();
			if (seperator != ':')
				throw new JsonException("Error seperators ...");

			Object value = nextValue();

			jsonObject.put(key, value);

			int checker = getChars();

			if (checker == ']') {
				checker = getChars();
			}

			if (checker == '}') {
				return jsonObject;
			}

		}

		return jsonObject;
	}

	private String readString(int c) {

		StringBuilder builder = new StringBuilder();
		while (index < input.length()) {
			char getString = input.charAt(index++);
			if (getString == c) {
				return builder.toString();
			}

			builder.append(getString);
		}
		return null;
	}

	public Object readValue() {

		String builder = "";

		int start = index;
		for (; index < input.length(); index++) {
			int c = input.charAt(index);

			switch (c) {
			case ',':
			case '}':
			case ']':
				builder = input.substring(start - 1, index).replace(" ", "");
				return value(builder);
			case -1:
				builder = input.substring(start - 1, index).replace(" ", "");
				return value(builder);
			default:
				continue;
			}
		}

		return null;
	}

	public Object value(Object value) {

		if (value.equals("true")) {
			return Boolean.TRUE;
		} else if (value.equals("false")) {
			return Boolean.FALSE;
		}

		String toStringValue = value.toString();
		if (toStringValue.matches("-?\\d+(\\.\\d+)?")) {
			if (toStringValue.contains(".")) {
				return Double.valueOf(toStringValue);
			}

			return Integer.valueOf(toStringValue);
		}

		return value.toString();
	}

	public int getChars() {
		while (index < input.length()) {
			int c = input.charAt(index++);
			switch (c) {
			case ' ':
			case '\n':
			case '\t':
			case '\r':
				continue;
			default:
				return c;
			}
		}

		return -1;
	}

}