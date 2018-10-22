package com.anasabuhussein.json.convertmodel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import com.anasabuhussein.json.JsonArrayObject;
import com.anasabuhussein.json.JsonException;
import com.anasabuhussein.json.JsonObject;
import com.anasabuhussein.util.AnalizeModel;

/**
 * This calss will convert model object that to json object.
 **/
public class ModelJsonObject extends AnalizeModel {

	public JsonObject createJsonObject(Object object)
			throws IllegalAccessException, InvocationTargetException, JsonException, ModelConvertorExceptions {
		JsonObject jsonObject = new JsonObject();

		for (int i = 0; i < super.getModelFields(object.getClass()).size(); i++) {

			Field field = super.getModelFields(object.getClass()).get(i);
			String fieldName = field.getName();

			if (checkIsCustomClass(field.getType()) && !field.getType().isPrimitive()) {
				jsonObject.put(fieldName, getValueObject(fieldName, field.getType(), object, new JsonObject()));

			} else if (field.getType() == List.class) {
				jsonObject.put(fieldName, getVlaueFromArray(fieldName, object));
			} else {
				jsonObject.put(fieldName, super.getValueFromGetMethod(fieldName, object));
			}
		}

		return jsonObject;
	}

	/**
	 *
	 * <h2>Description</h2>
	 * <p>
	 * This method will get any attribute as object in main object class, <br>
	 * That is not in {@link AnalizeModel#checkIsCustomClass(Class)} method.
	 * </p>
	 *
	 * @param fieldName  name of field is Object
	 * @param class1     class of object to get getMethod; attribute object in
	 *                   class.
	 * @param object     that main object is contains objects to get object values
	 *                   from get method..
	 * @param jsonObject to set data to json object ..
	 * @throws ModelConvertorExceptions
	 **/
	public Object getValueObject(String fieldName, Class<?> class1, Object object, JsonObject jsonObject)
			throws IllegalAccessException, InvocationTargetException, JsonException, ModelConvertorExceptions {

		if (object == null)
			throw new ModelConvertorExceptions("The object is contain another Object attribute so must not be null.");

		Object object2 = super.getValueFromGetMethod(fieldName, object);
		
		// read methods for class is in main Object .
		Iterator<Method> iterator = getMethods(class1).iterator();

		while (iterator.hasNext()) {

			Method method = iterator.next();
			String nameMethod = method.getName();
			String afterGet = nameMethod.substring(3, nameMethod.length());

			if (object2 != null) {
				String name = super.getFieldName(afterGet, object2);

				if (checkIsCustomClass(method.getReturnType()) && !method.getReturnType().isPrimitive()) {
					jsonObject.put(name, getValueObject(afterGet, object2));
				} else {
					if (name != null && !name.equals("Class"))
						jsonObject.put(name, getValue(object2, method));
				}
			} else {
				return jsonObject;
			}

		}

		return jsonObject.remove("Class");
	}

	/**
	 * <h3>Description :</h3>
	 * 
	 * <pre>
	 *  This method for do it in list data after check it and the list contains object will do this method
	 *  will check the data type of get getMethods and may do put data to {@link JsonObject#put(String, Object)} 
	 *  if data it is primitive data or make recursion {@link ModelJsonObject#getValueObject(String, Object, JsonObject)} 
	 *  if data is or attribute is object from custome class or make {@link ModelJsonObject#getVlaueFromArray(String, Object, JsonArrayObject)} 
	 *  if attribute is contains array.
	 * </pre>
	 * 
	 * @param fieldName name of attribute in class,
	 * @param object    object that is contains data.
	 * @return {@link JsonObject}
	 * @throws ModelConvertorExceptions
	 * 
	 **/
	public JsonObject getValueObject(String fieldName, Object object)
			throws IllegalAccessException, InvocationTargetException, JsonException, ModelConvertorExceptions {

		if (object == null)
			throw new ModelConvertorExceptions("The Object must not by null");

		// create json object to set read data from get method.
		JsonObject jsonObject = new JsonObject();

		// read methods for class is in main Object .
		Iterator<Method> iterator = getMethods(object.getClass()).iterator();

		while (iterator.hasNext()) {

			Method method = iterator.next();
			String nameMethod = method.getName();
			String afterGet = nameMethod.substring(3, nameMethod.length());

			String name = getFieldName(afterGet, object);

			// if return type f get method it is object from custome class make recursions.
			// after that put it to json object.
			if (checkIsCustomClass(method.getReturnType())) {
				jsonObject.put(name, getValueObject(afterGet, method.getReturnType(), object, new JsonObject()));

			} else {

				// if return typeof get method from list get value from list
				if (method.getReturnType() == List.class) {
					jsonObject.put(name, this.getVlaueFromArray(afterGet, object));
				}

				// if returnt type is primitive type get value from get method adn put it to
				// json object.
				if (method.getReturnType() == String.class
						|| method.getReturnType().getName().startsWith("java.lang")) {
					if (name != null && !name.equals("Class"))
						jsonObject.put(name, getValue(object, method));
				}

			}
		}
		return (JsonObject) jsonObject.remove("Class");
	}

	/**
	 * <h3>Description :</h3>
	 * 
	 * <pre>
	 *  This method will return json array object; and check the list
	 *  contains custom object from {@link #checkIsCustomClass(Class)} or not.
	 * </pre>
	 * 
	 * @param fieldName name of field in class
	 * @param object    Object is contain data to set it into
	 *                  {@link JsonArrayObject}
	 * @param create    new {@link JsonArrayObject} to store data to it.
	 * @throws ModelConvertorExceptions
	 **/
	public JsonArrayObject<?> getVlaueFromArray(String fieldName, Object object)
			throws IllegalAccessException, InvocationTargetException, JsonException, ModelConvertorExceptions {

		JsonArrayObject<Object> jsonArrayObject = new JsonArrayObject<>();

		// get value from attribute in object that is list.
		List<?> list = (List<?>) super.getValueFromGetMethod(fieldName, object);

		// make iterations ...
		Iterator<?> iterator = list.iterator();

		while (iterator.hasNext()) {

			// get every object in list ...
			Object object2 = iterator.next();

			// check every object in list if is custom class or primitive class or string .
			if (checkIsCustomClass(object2.getClass())) {

				// custom class will read object from below method.
				jsonArrayObject.add(getValueObject(fieldName, object2));
			} else {

				// list inside list do recursion .
				if (object2 instanceof List<?>) {
					jsonArrayObject.add(this.getVlaueFromArray(fieldName, object2));
				}

				// list contains string or number add it.
				if (object2 instanceof String || object2.getClass().getName().startsWith("java.lang")) {
					jsonArrayObject.add(object2);
				}

			}

		}

		return jsonArrayObject;
	}

}
