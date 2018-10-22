package com.anasabuhussein.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.anasabuhussein.json.JsonException;

/**
 * This class commen class for any class work with map and Its components are
 * mainly.
 * 
 * @author Anas Abu-Hussein
 **/
public class ObjectMap {

	private Map<String, Object> objectMap;

	public ObjectMap() {
		objectMap = new LinkedHashMap<>();
	}

	/**
	 * put String key with boolean value.
	 **/
	public ObjectMap put(String key, boolean value) throws JsonException {
		objectMap.put(check_Key(key), value);
		return this;
	}

	/**
	 * put String key with string value.
	 **/
	public ObjectMap put(String key, String value) throws JsonException {
		objectMap.put(check_Key(key), value);
		return this;
	}

	/**
	 * put String key with int value.
	 **/
	public ObjectMap put(String key, int value) throws JsonException {
		objectMap.put(check_Key(key), value);
		return this;
	}

	/**
	 * put String key with float value.
	 **/
	public ObjectMap put(String key, float value) throws JsonException {
		objectMap.put(check_Key(key), value);
		return this;
	}

	/**
	 * put String key with double value.
	 **/
	public ObjectMap put(String key, double value) throws JsonException {
		objectMap.put(check_Key(key), value);
		return this;
	}

	/**
	 * put String key with long value.
	 **/
	public ObjectMap put(String key, long value) throws JsonException {
		objectMap.put(check_Key(key), value);
		return this;
	}

	/**
	 * put String key with object value.
	 **/
	public ObjectMap put(String key, Object value) throws JsonException {

		if (value == null) {
			objectMap.remove(key);
			return this;
		}

		if (value instanceof Number) {
			PrimitiveDataTypeConvertor.checkDouble(((Number) value).doubleValue());
		}

		objectMap.put(check_Key(key), value);
		return this;
	}

	/**
	 * get object dependent on key.
	 **/
	public Object get(String key) {
		return objectMap.get(key);
	}

	/**
	 * get boolean dependent on key.
	 **/
	public Boolean getBoolean(String key) throws JsonException {
		Object object = get(key);
		Boolean result = PrimitiveDataTypeConvertor.toBoolean(object);
		if (result == null) {
			throw PrimitiveDataTypeConvertor.exceptionType(key, object, "boolean");
		}
		return result;
	}

	/**
	 * get string dependent on key.
	 **/
	public String getString(String key) throws JsonException {
		Object object = get(key);
		String result = PrimitiveDataTypeConvertor.toString(object);
		if (result == null) {
			throw PrimitiveDataTypeConvertor.exceptionType(key, object, "String");
		}
		return result;
	}

	/**
	 * get int dependent on key.
	 **/
	public Integer getInt(String key) throws JsonException {
		Object object = get(key);
		Integer result = PrimitiveDataTypeConvertor.toInteger(object);
		if (result == null) {
			throw PrimitiveDataTypeConvertor.exceptionType(key, object, "Integer");
		}
		return result;
	}

	/**
	 * get long dependent on key.
	 **/
	public Long getLong(String key) throws JsonException {
		Object object = get(key);
		Long result = PrimitiveDataTypeConvertor.toLong(object);
		if (result == null) {
			throw PrimitiveDataTypeConvertor.exceptionType(key, object, "Long");
		}
		return result;
	}

	/**
	 * get doube dependent on key.
	 **/
	public Double getDouble(String key) throws JsonException {
		Object object = get(key);
		Double result = PrimitiveDataTypeConvertor.toDouble(object);
		if (result == null) {
			throw PrimitiveDataTypeConvertor.exceptionType(key, object, "Double");
		}
		return result;
	}

	/**
	 * get Keys set
	 **/
	public Set<String> getKeys() {
		return objectMap.keySet();
	}

	/**
	 * check the map has key.
	 **/
	public boolean hasKey(String key) {
		if (objectMap.containsKey(key))
			return true;

		return false;
	}

	/**
	 * check the map has value.
	 **/
	public boolean hasValue(Object value) {
		if (objectMap.containsKey(value))
			return true;

		return false;
	}

	/**
	 * replace
	 **/
	public ObjectMap replace(String key, Object value) {
		objectMap.replace(key, value);
		return this;
	}

	/**
	 * remove dependent on key
	 **/
	public ObjectMap remove(String key) {
		if (hasKey(key))
			objectMap.remove(key);
		return this;
	}

	/**
	 * remove dependent on key and value
	 **/
	public ObjectMap remove(String key, Object value) {
		if (hasKey(key) && hasValue(value))
			objectMap.remove(key, value);
		return this;
	}

	/**
	 * <p>
	 * This method will check if the key is set is null or not
	 * </p>
	 * 
	 * @param key
	 * @return true if is not null
	 * @exception if the key is null
	 **/
	private String check_Key(String key) throws JsonException {
		if (key == null || key.isEmpty()) {
			throw new JsonException("The key must not null, or empty");
		}

		return key;
	}

	/**
	 * get map object
	 **/
	public Map<String, Object> getMapObject() {
		return objectMap;
	}

	/**
	 * set new map object
	 **/
	public void setMapObject(Map<String, Object> mapObject) {
		this.objectMap = mapObject;
	}

}
