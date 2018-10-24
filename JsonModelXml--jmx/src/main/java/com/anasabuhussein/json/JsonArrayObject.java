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
import java.util.List;

import com.anasabuhussein.json.convertmodel.JsonArrayModelObject;
import com.anasabuhussein.json.convertmodel.ModelConvertorExceptions;
import com.anasabuhussein.json.convertmodel.ModelObjectJsonArray;
import com.anasabuhussein.util.ObjectList;

/**
 * <pre>
 * This class for create array json object and for :
 * 	<ul>
 * 		<li>convert from {@link String} to {@link JsonArrayObject} by use {@link #fromString(String)}</li>
 * 		<li>convert from {@link JsonArrayObject} to {@link String} by use {@link JsonArrayObject#toString()}</li>
 * 		<li>convert from {@link JsonArrayObject #toString()} to {@link Class} or model by use {@link #toModel(String, Class)}</li>
 * 		<li>convert from Model or {@link Class} to {@link JsonArrayObject} by use {@link #fromModel(List)}</li>	
 * 	</ul>
 * </pre>
 **/
public class JsonArrayObject<T> extends ObjectList<T> {

	public JsonArrayObject() {
		super();
	}

	/**
	 * This method for convert any string start with array to json array objbect.
	 * 
	 * @param jsonArrayObject json array object as string
	 * @return {@link JsonArrayObject}
	 * 
	 * @see StringJsonBuilder
	 **/
	public static JsonArrayObject<Object> fromString(String jsonArrayObject) throws JsonException {
		StringJsonBuilder builder = new StringJsonBuilder(jsonArrayObject);
		return (JsonArrayObject<Object>) builder.nextValue();
	}

	/**
	 * This method will convert any string or {@link JsonArrayObject#toString()} or
	 * start with array to model or class you want.
	 * 
	 * @param jsonArrayString string of array json
	 * @param classTypeInList class you want convert it.
	 * 
	 * @return Object that want convert to it.
	 * 
	 * @see JsonArrayModelObject
	 **/
	public static Object toModel(String jsonArrayString, Class<?> classTypeInList)
			throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException,
			JsonException, ModelConvertorExceptions {
		JsonArrayModelObject jsonArrayModelObject = new JsonArrayModelObject();
		return jsonArrayModelObject.createModelOfList(jsonArrayString, classTypeInList);
	}

	/**
	 * will convert from Objects list to jso array object.
	 * 
	 * @param objectsList list contains object.
	 * @return {@link JsonArrayObject}
	 * @see ModelObjectJsonArray
	 **/
	@SuppressWarnings("unchecked")
	public static JsonArrayObject<Object> fromModel(List<Object> objectsList)
			throws IllegalAccessException, InvocationTargetException, JsonException, ModelConvertorExceptions {
		ModelObjectJsonArray modelObjectJsonArray = new ModelObjectJsonArray();
		return modelObjectJsonArray.createJsonArray((List<Object>) objectsList);
	}

	/**
	 * Will convert json arrat object to string.
	 **/
	@Override
	public String toString() {

		JsonStringBuilder builder = new JsonStringBuilder();
		try {

			builder.openArray();

			if (super.getObjectList().isEmpty())
				builder.appendValue(null);

			Iterator<T> iterator = super.getObjectList().iterator();
			while (iterator.hasNext()) {
				T t = iterator.next();
				builder.appendValue(t);
			}

			builder.closeArray();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return builder.toString();
	}

}
