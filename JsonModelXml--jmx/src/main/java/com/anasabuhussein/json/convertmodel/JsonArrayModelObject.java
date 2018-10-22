package com.anasabuhussein.json.convertmodel;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.anasabuhussein.json.JsonArrayObject;
import com.anasabuhussein.json.JsonException;
import com.anasabuhussein.json.JsonObject;

/**
 * This method will convert any json array as string to list of obejcts ... <br>
 * String json Array to list of model. <br>
 * JsonArray ----> Model
 * 
 **/

public class JsonArrayModelObject {

	public JsonArrayModelObject() {
		super();
	}

	@SuppressWarnings("unchecked")
	public List<?> createModelOfList(String jsonArray, Class<?> classType) throws JsonException, IllegalAccessException,
			InvocationTargetException, InstantiationException, NoSuchFieldException, ModelConvertorExceptions {

		if (classType == null)
			throw new com.anasabuhussein.json.convertmodel.ModelConvertorExceptions(
					" Must set class type of list to convert it to model or list");

		JsonArrayObject<?> arrayObject = JsonModelUtil.isJsonArrayObject(jsonArray);

		if (arrayObject == null)
			throw new JsonException("The String must be start with array Brackets [] ");

		List<Object> list = new LinkedList<>();

		Iterator<Object> iterator = (Iterator<Object>) arrayObject.getObjectList().iterator();

		int count = 0;

		while (iterator.hasNext()) {
			Object object = iterator.next();
			list.add(appendList(list, object, classType, arrayObject.get(count).toString()));
			count++;
		}

		return list;

	}

	private Object appendList(List<Object> list, Object object, Class<?> class1, String listInList)
			throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException,
			JsonException, ModelConvertorExceptions {

		JsonModelObject jsonModelObject = null;

		if (object instanceof JsonObject) {
			jsonModelObject = new JsonModelObject();
			return jsonModelObject.createModelObject(((JsonObject) object).toString(), class1);
		}

		if (object instanceof JsonArrayObject<?>) {
			return createModelOfList(listInList, class1);
		}

		if (object != JsonArrayObject.class && object != JsonObject.class) {
			return object;
		}

		return null;

	}

}
