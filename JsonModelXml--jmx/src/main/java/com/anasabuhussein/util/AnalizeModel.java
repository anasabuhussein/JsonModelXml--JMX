package com.anasabuhussein.util;

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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <h3>Description :</h3>
 * 
 * <pre>
 * This class for analysis any model or class pass to here; they need it to 
 * get class fields names and get data getMethod and setMethods and set data by fields and check data type
 * from getMethods
 * </pre>
 * 
 * @author Anas Abu-Hussein
 * 
 **/

public class AnalizeModel {

	/**
	 * <pre>
	 *  This method for get fields of class in list.
	 * </pre>
	 * 
	 * @param model class you want to analysis.
	 * @return list of class field as linked list.
	 **/
	public List<Field> getModelFields(Class<?> model) {
		List<Field> listField = new LinkedList<>();
		for (int i = 0; i < model.getDeclaredFields().length; i++) {
			model.getDeclaredFields()[i].setAccessible(true);
			listField.add(model.getDeclaredFields()[i]);
		}
		return listField;
	}

	/**
	 * <pre>
	 *  This method for get getMethods from class and set it in list.
	 *  by check every methods in class and check if is start with get string or not.
	 * </pre>
	 * 
	 * @param model class you want to analysis.
	 * @return list of class methods start with get String as linked list.
	 **/
	public List<Method> getMethods(Class<?> model) {
		List<Method> list = new LinkedList<>();
		Method[] methods = model.getMethods();
		for (int i = 0; i < methods.length; i++) {
			methods[i].setAccessible(true);
			if (methods[i].getName().startsWith("get")) {
				list.add(methods[i]);
			}
		}
		return list;
	}

	/**
	 * <pre>
	 *  This method for return type of getMethod by set name and object.
	 *  will check if it is name = get method name but after substring name of method
	 *  after -get- chars if equals will return type else will return null.
	 * </pre>
	 * 
	 * @param name   key of json object or attribute name from class.
	 * @param object object must visit to get class and check all getMethods.
	 * @return dataType of getMethod if name = getMethod name
	 **/
	public Object getMethodByName(String name, Object object) {

		Iterator<Method> iterator = getMethods(object.getClass()).iterator();
		while (iterator.hasNext()) {

			Method method = iterator.next();

			String nameMethod = method.getName();
			String afterGet = nameMethod.substring(3, nameMethod.length());

			if (afterGet.equalsIgnoreCase(name)) {
				Object object2 = method.getReturnType();
				return object2;
			}
		}

		return null;
	}

	/**
	 * <pre>
	 *  This method for get type of generic data to check his type, and return it.
	 *  such as List<?> -?- must know type of -?- to set data and store it to new
	 *  object if -?- is not primitive data
	 * </pre>
	 * 
	 * @param name   key of json object or attribute name from class.
	 * @param object object must visit to get class and check all fields of class.
	 * @return generic dataType of getDeclaredField.
	 **/
	public Type getGenericTypeFromFieldName(String name, Object object) throws NoSuchFieldException {
		return object.getClass().getDeclaredField(name).getGenericType();
	}

	/**
	 * <pre>
	 *  This method for get type of field of class.
	 * </pre>
	 * 
	 * @param name   key of json object or attribute name from class.
	 * @param object object must visit to get class and check all fields of class.
	 * @return dataType of getDeclaredField.
	 **/
	public Type getTypeField(String name, Object object) throws NoSuchFieldException {
		return object.getClass().getDeclaredField(name).getType();
	}

	/**
	 * <pre>
	 *  This method for get data from getMethods.
	 *  will check if name equals {@link #afterSub(Method)} will get value of get method 
	 *  and return it as object else will retrun null;F
	 * </pre>
	 * 
	 * @param fieldName key of json object or attribute name from class.
	 * @param object    object must visit to get class and check all getMethods of
	 *                  class.
	 * @return value of get method in class if fieldName = getmethod name will
	 *         return value of method else will return null
	 **/
	public Object getValueFromGetMethod(String fieldName, Object object)
			throws IllegalAccessException, InvocationTargetException {

		Iterator<Method> iterator = getMethods(object.getClass()).iterator();

		while (iterator.hasNext()) {

			Method method = iterator.next();

			if (afterSub(method).equalsIgnoreCase(fieldName)) {
				return getValue(object, method);
			}

		}
		return null;
	}

	/**
	 * <pre>
	 * This method will substring method name it is coming from methods {@link Iterator} 
	 * of {@link #getMethods(Class)}
	 * </pre>
	 * 
	 * @param method method nedd to substring get or set.
	 * @return name after substring get or set
	 **/
	public String afterSub(Method method) {
		String name = method.getName();
		String afterSub = name.substring(3, name.length());
		return afterSub;
	}

	/**
	 * <pre>
	 * This method will get data from getMethod.
	 * </pre>
	 * 
	 * @param object object you want to get method from it.
	 * @param method method to get data from it.
	 * @return data from get method.
	 **/
	public Object getValue(Object object, Method method) throws IllegalAccessException, InvocationTargetException {
		return method.invoke(object, null);
	}

	/**
	 * <pre>
	 * This method for set data to fields in class.
	 * if field name = {@link #getModelFields(Class)} will set data to it.
	 * </pre>
	 * 
	 * @param fieldName name in class
	 * @param object    object that contains the field.
	 * @param value     value you want to set it to field.
	 **/
	public void setValueModelFields(String fieldName, Object object, Object value) throws IllegalAccessException {

		Iterator<Field> iterator = getModelFields(object.getClass()).iterator();

		while (iterator.hasNext()) {
			Field field = iterator.next();
			if (field.getName().equalsIgnoreCase(fieldName)) {
				setValue(object, value, field);
				break;
			}
		}
	}

	/**
	 * <pre>
	 * This method for set data to fields in class.
	 * </pre>
	 * 
	 * @param fieldName name in class
	 * @param object    object that contains the field.
	 * @param value     value you want to set it to field.
	 **/
	public void setValue(Object object, Object value, Field field) throws IllegalAccessException {

		field.setAccessible(true);
		field.set(object, value);
	}

	public boolean setValueModelFieldsWithCheck(String fieldName, Object T, Object value) {
		try {
			setValueModelFields(fieldName, T, value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("unused")
	private void setBySetMethods(String fieldName, Object object, Object value)
			throws IllegalAccessException, InvocationTargetException {
		try {

			Iterator<Method> iterator = setMethods(object.getClass()).iterator();

			while (iterator.hasNext()) {
				Method method = iterator.next();
				String name = method.getName();
				String afterGet = name.substring(3, name.length());
				if (fieldName.equalsIgnoreCase(afterGet)) {
					method.setAccessible(true);
					method.invoke(object, value);
					break;
				}
			}
		} catch (Exception e) {
			//
		}
	}

	public List<Method> setMethods(Class<?> model) {
		List<Method> list = new LinkedList<>();
		Method[] methods = model.getMethods();
		for (int i = 0; i < methods.length; i++) {
			methods[i].setAccessible(true);
			if (methods[i].getName().startsWith("set")) {
				list.add(methods[i]);
			}
		}
		return list;
	}

	/**
	 * <pre>
	 * This class for check if class is custom class or class is string or start with java.lang
	 * if custom class will return true else false.
	 * </pre>
	 * 
	 * @param class you want to check.
	 * @return true if class1 != List.class && class1 != String.class && class1 !=
	 *         Map.class && !class1.getName().startsWith("java.lang") flase equals
	 *         it.FS
	 **/
	public static boolean checkIsCustomClass(Class<?> class1) {
		return class1 != List.class && class1 != String.class && class1 != Map.class
				&& !class1.getName().startsWith("java.lang");
	}

	/**
	 * This method will return field name as string. will check all
	 * {@link #getModelFields(Class)} and if name = any fields will return it0.
	 * 
	 * @param name  name of field in class.
	 * @param model object you want visit it.
	 * @return name of fields.
	 **/
	public String getFieldName(String name, Object model) {
		Iterator<Field> iterator = getModelFields(model.getClass()).iterator();
		while (iterator.hasNext()) {
			Field field = iterator.next();
			if (name.equalsIgnoreCase(field.getName()))
				return field.getName();
		}
		return null;
	}

}
