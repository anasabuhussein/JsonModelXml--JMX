package com.anasabuhussein.json.convertmodel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.anasabuhussein.json.JsonArrayObject;
import com.anasabuhussein.json.JsonException;
import com.anasabuhussein.json.JsonObject;
import com.anasabuhussein.util.AnalizeModel;

/**
 * This class will convert Json object that comaing as string to model.
 **/
public class JsonModelObject extends AnalizeModel {

	// for start char is object .. 
	public Object createModelObject(String jsonObjectString, Class<?> class1) throws JsonException,
			IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {

		JsonObject jsonObject = JsonModelUtil.isJsonObject(jsonObjectString);

		if (jsonObject == null)
			throw new JsonException(" The String is not start with json object ");

		try {
			return setJsonObjectValuesToClass(null, jsonObject, class1);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}

		return null;

	}

	// this method for set json object value to class fields ...
	private Object setJsonObjectValuesToClass(String jsonKey, JsonObject jsonObject, Class<?> class1)
			throws InstantiationException, IllegalAccessException, JsonException, InvocationTargetException,
			NoSuchMethodException, NoSuchFieldException {

		Object object = class1.newInstance();

		Iterator<String> keys = jsonObject.getKeys().iterator();

		while (keys.hasNext()) {

			String key = keys.next();
			Object jsonValue = jsonObject.get(key);

			// this for check if json value is object or array ..
			Object value = checkJsonValue(key, jsonValue, object);

			if (value != null) {

				super.setValueModelFields(key, object, value);

			} else {

				if (jsonValue instanceof JsonObject) {
					finalBackDownOfObjects(key, object, (JsonObject) jsonValue);
				} else {
					super.setValueModelFields(key, object, jsonValue);
				}
			}
		}

		return object;
	}

	private void finalBackDownOfObjects(String key, Object object, JsonObject jsonObject)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {

		Iterator<String> iterator = jsonObject.getKeys().iterator();
		while (iterator.hasNext()) {
			String keys = iterator.next();
			Object object2 = jsonObject.get(keys);
			super.setValueModelFields(keys, object, object2);
		}
	}

	// this method for set json array value to list or class fields . ..
	private Object setJsonArrayObjectValuesToClass(String key, JsonArrayObject<?> jsonArrayObject, Object object)
			throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,
			JsonException, NoSuchFieldException {

		// create list ...
		List<Object> list = new ArrayList<>();

		// show type of list.
		Class<?> paraOfList = checkParaOfList(key, object);

		Iterator<?> iterator = jsonArrayObject.getObjectList().iterator();

		while (iterator.hasNext()) {

			Object value = iterator.next();

			Object valueObject = paraOfList.newInstance();
			Object resultCheck = checkJsonValue(null, value, valueObject);

			if (resultCheck != null) {
				list.add(resultCheck);
			} else {
				list.add(value);
			}
		}

		super.setValueModelFields(key, object, list);

		return list;
	}

	private Class<?> checkParaOfList(String key, Object object) throws NoSuchFieldException {
		ParameterizedType type = (ParameterizedType) getGenericTypeFromFieldName(key, object);
		Class<?> class1 = (Class<?>) type.getActualTypeArguments()[0];
		return class1;
	}

	private Object checkJsonValue(String jsonKey, Object jsonValue, Object object)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException,
			JsonException, NoSuchFieldException {

		if (jsonValue instanceof JsonObject) {
			if (jsonKey != null) {

				Class<?> getClass = (Class<?>) super.getMethodByName(jsonKey, object);

				if (getClass != null) {
					return setJsonObjectValuesToClass(jsonKey, (JsonObject) jsonValue, getClass);
				}

			} else {
				return setJsonObjectValuesToClass(jsonKey, (JsonObject) jsonValue, object.getClass());
			}
		}

		if (jsonValue instanceof JsonArrayObject<?>) {
			return setJsonArrayObjectValuesToClass(jsonKey, (JsonArrayObject<?>) jsonValue, object);
		}

		return null;
	}

}
