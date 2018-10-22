package com.anasabuhussein.json.convertmodel;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import com.anasabuhussein.json.JsonArrayObject;
import com.anasabuhussein.json.JsonException;

/**
 * This will convert from list or set to json array ..
 **/
public class ModelObjectJsonArray {

	public JsonArrayObject<Object> createJsonArray(List<Object> list)
			throws IllegalAccessException, InvocationTargetException, JsonException, ModelConvertorExceptions {
		JsonArrayObject<Object> arrayObject = new JsonArrayObject<>();

		Iterator<Object> iterator = list.iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();
			ModelJsonObject model_Json = new ModelJsonObject();
			arrayObject.add(model_Json.createJsonObject(object));
		}

		return arrayObject;
	}
}
