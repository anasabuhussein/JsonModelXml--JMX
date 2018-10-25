package com.anasabuhussein.xml;

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

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import com.anasabuhussein.json.JsonException;
import com.anasabuhussein.json.JsonObject;
import com.anasabuhussein.json.convertmodel.ModelConvertorExceptions;

/**
 * This class for convert from xml to model or reverse, or from xml to json.
 * 
 * @author Anas Abu-Hussein.
 **/
public class XML {

	private XML() {
		super();
	}

	/**
	 * This method will convert from xml to json.
	 **/
	public static JsonObject toJson(String input, Class<?> class1)
			throws InstantiationException, IllegalAccessException, JAXBException, InvocationTargetException,
			JsonException, NoSuchFieldException, ModelConvertorExceptions {
		// from xml to object
		Object object = toModel(input, class1);

		// from object to json;
		return JsonObject.fromModel(object);
	}

	/**
	 * This will convert from String to model object.
	 **/
	public static Object toModel(String iString, Class<?> class1)
			throws JAXBException, InstantiationException, IllegalAccessException {

		StringReader sr = new StringReader(iString);

		Object objectClass = class1.newInstance();
		JAXBContext jaxbCtxt = JAXBContext.newInstance(class1);

		Unmarshaller jaxbUnmarshaller = jaxbCtxt.createUnmarshaller();
		objectClass = jaxbUnmarshaller.unmarshal(sr);

		return objectClass;
	}

	/**
	 * <h2>Description :</h2>
	 * 
	 * <pre>
	 *  This method will convert the object set to xml string.
	 *  
	 *  <code>
	 *  	Type type = new Type();
	 *  	System.out.println(XML.getStringXmlFromObject(type, "type", Type.class));
	 *  
		String teString = "<type>\r\n" +
	 *  			"    <id>0</id>\r\n" +
	 *  			"</type>\r\n";
	 *   </code>
	 * </pre>
	 * 
	 * @param object  Object must convert it.
	 * @param name    name of object.
	 * @param classes class of object; if you have generic object must set class of
	 *                object and class of generic object.
	 * 
	 **/
	public static <T> String toString(Object object, Class<?>... classes) {
		try {

			JAXBContext jc = JAXBContext.newInstance(classes);
			QName qName = new QName(object.getClass().getSimpleName().toLowerCase());

			@SuppressWarnings("unchecked")
			JAXBElement<T> jaxbElement = new JAXBElement<T>(qName, (Class<T>) classes[0], (T) object);

			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			StringWriter sw = new StringWriter();

			marshaller.marshal(jaxbElement, sw);

			String xmlContent = sw.toString();
			return xmlContent;

		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}

}
