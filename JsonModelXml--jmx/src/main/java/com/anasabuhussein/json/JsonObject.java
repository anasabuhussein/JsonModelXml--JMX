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

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import javax.management.modelmbean.XMLParseException;

import com.anasabuhussein.json.convertmodel.JsonModelObject;
import com.anasabuhussein.json.convertmodel.ModelConvertorExceptions;
import com.anasabuhussein.json.convertmodel.ModelJsonObject;
import com.anasabuhussein.util.AnalizeModel;
import com.anasabuhussein.util.ObjectMap;
import com.anasabuhussein.xml.XML;

/**
 * <pre>
 * This class for create json object and for :
 * 	<ul>
 * 		<li>convert from {@link String} to {@link JsonObject} by use {@link #fromString(String)}</li>
 * 		<li>convert from {@link JsonObject} to {@link String} by use {@link JsonObject#toString()}</li>
 * 		<li>convert from {@link JsonObject #toString()} to {@link Class} or model by use {@link #toModel(String, Class)}</li>
 * 		<li>convert from Model or {@link Class} to {@link JsonObject} by use {@link #fromModel(Object)}</li>
 * 		<li>convert from {@link String} object to XML string by use {@link #toXml(String, Class...)}</li>
 * 		<li>convert from {@link JsonObject#toString()} to XML string by use {@link #toXml(String, Class...)}</li>	
 * 	</ul>
 * </pre>
 * 
 * @author Anas Abu-Hussein
 **/

/**
 * @author anasa
 *
 */
public class JsonObject extends ObjectMap {

	public JsonObject() {
		super();
	}

	/**
	 * This method will conver string start with { to Json object <br>
	 * will use {@link StringJsonBuilder} to convert from string to
	 * {@link JsonObject}
	 * 
	 * @param object String jsonObject you need convert it ti {@link JsonObject}
	 * @return {@link JsonObject}
	 **/
	public static JsonObject fromString(String object) throws JsonException {
		StringJsonBuilder jsonStringBuilder = new StringJsonBuilder(object);
		return (JsonObject) jsonStringBuilder.nextValue();
	}

	/**
	 * This method will convert from model or class to json object.
	 * 
	 * @param object object you want to convert it to {@link JsonObject}
	 * @return {@link JsonObject}
	 * @see ModelJsonObject
	 **/
	public static JsonObject fromModel(Object object)
			throws IllegalAccessException, InvocationTargetException, JsonException, ModelConvertorExceptions {
		ModelJsonObject modelJsonObject = new ModelJsonObject();
		return modelJsonObject.createJsonObject(object);
	}

	/**
	 * this method will convert from string start with json object or json object
	 * converted to string by using {@link #toString()} to any class or object you
	 * want.<br>
	 * 
	 * 
	 * @param jsonObject String the present {@link JsonObject}
	 * @param class1     class you want convert to it for read fields and method and
	 *                   use {@link AnalizeModel}
	 * @return object taht converted from json object to model class or object
	 *         casting.
	 * @see JsonModelObject
	 **/
	public static Object toModel(String jsonObject, Class<?> class1) throws IllegalAccessException,
			InvocationTargetException, JsonException, InstantiationException, NoSuchFieldException {
		JsonModelObject jsonModelObject = new JsonModelObject();
		return jsonModelObject.createModelObject(jsonObject, class1);
	}

	public int jsonSize() {
		return super.getMapObject().size();
	}

	/**
	 * The order of class is verey important from hieh class to deep class.<br>
	 * theis method will convert from {@link JsonObject#toString()} or seting json
	 * object to Model or class then will convert model to string using
	 * {@link XML#toString(Object, Class...)}
	 * 
	 * @param JsonStringObject json object as string.
	 * @param class1           classes must be converted and following bind xml
	 *                         notations.
	 * @throws XMLParseException
	 * 
	 * @see {@link JsonObject#toModel(String, Class)}
	 * @see XML#toString()
	 **/
	public static String toXml(String JsonStringObject, Class<?>... class1) throws IllegalAccessException,
			InvocationTargetException, InstantiationException, JsonException, NoSuchFieldException, XMLParseException {
		Object object = null;
		try {
			object = JsonObject.toModel(JsonStringObject, class1[0]);
		} catch (Exception e) {
			throw new XMLParseException("Must " + e.getMessage() + " first element of class array parameters.");
		}
		return XML.toString(object, class1);
	}

	/**
	 * Convert map json object to string.
	 **/
	@Override
	public String toString() {

		JsonStringBuilder builder = new JsonStringBuilder();

		try {

			builder.openObject();
			Iterator<String> iterator = super.getKeys().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				builder.appendKey(key);

				Object value = super.get(key);
				builder.appendValue(value);
			}
			builder.closeObject();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return builder.toString();
	}

}
