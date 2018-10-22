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

import java.util.ArrayList;
import java.util.List;

/**
 * this class for conver from json object to string.
 * 
 * @author Anas Abu-Hussein
 **/
public class JsonStringBuilder {

	private StringBuilder builder = new StringBuilder();

	enum DrawJson {
		ARRAY, VALUE, EMPTY, END_ARRAY, OBJECT, KEY_VALUE, END_OBJECT;
	}

	final List<DrawJson> stack = new ArrayList<>();

	public void openArray() {
		builder.append("[");
		stack.add(DrawJson.ARRAY);
		stack.add(DrawJson.VALUE);
	}

	public void closeArray() throws JsonException {

		if (stack.isEmpty() || stack.get(stack.size() - 2) != DrawJson.ARRAY)
			throw new JsonException("Nesting Error - in Array");

		builder.append("]");
		stack.add(DrawJson.END_ARRAY);
	}

	public void openObject() {
		builder.append("{");
		stack.add(DrawJson.OBJECT);
		stack.add(DrawJson.KEY_VALUE);
	}

	public void closeObject() throws JsonException {
		if (stack.isEmpty() || stack.get(stack.size() - 2) != DrawJson.OBJECT)
			throw new JsonException("Nesting Error - in object");

		if (builder.charAt(builder.toString().length() - 1) == '{') {
			stack.remove(DrawJson.KEY_VALUE);
			stack.add(DrawJson.EMPTY);
		}

		builder.append("}");
		stack.add(DrawJson.END_OBJECT);
	}

	public void appendKey(String key) {
		builder.append(escapeForJava(key, true) + " : ");
	}

	public static String escapeForJava(String value, boolean quote) {
		StringBuilder builder = new StringBuilder();

		if (quote)
			builder.append("\"");

		for (char c : value.toCharArray()) {
			if (c == '\'')
				builder.append("\\'");
			else if (c == '\"')
				builder.append("\\\"");
			else if (c == '\r')
				builder.append("\\r");
			else if (c == '\n')
				builder.append("\\n");
			else if (c == '\t')
				builder.append("\\t");
			else if (c < 32 || c >= 127)
				builder.append(String.format("\\u%04x", (int) c));
			else
				builder.append(c);
		}
		if (quote)
			builder.append("\"");
		return builder.toString();
	}

	public void appendValue(Object value) {
		if (value instanceof String) {
			builder.append("\"" + value.toString() + "\"" + ", ");
		} else if (value != null) {
			builder.append(value + ", ");
		} else {
			builder.append("" + ", ");
		}
	}

	public String toString() {

		if (stack.get(stack.size() - 2) == DrawJson.EMPTY)
			return builder.toString();

		if (stack.get(stack.size() - 2) != DrawJson.EMPTY)
			return builder.deleteCharAt(builder.lastIndexOf(", ")).toString();

		return null;
	}

}
