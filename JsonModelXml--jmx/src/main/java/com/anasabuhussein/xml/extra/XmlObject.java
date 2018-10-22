package com.anasabuhussein.xml.extra;

import java.util.Iterator;
import java.util.Map;

import com.anasabuhussein.json.JsonException;
import com.anasabuhussein.util.ObjectMap;

public class XmlObject extends ObjectMap {

	private String nameXmlObject = "changableName";

	public XmlObject() {
		super();
	}

	public XmlObject(String nameXmlObject) {
		super();

		if (nameXmlObject != null)
			this.nameXmlObject = nameXmlObject.replace(" ", "");
	}

	public void setNameXmlObject(String nameXmlObject) {
		if (nameXmlObject != null)
			this.nameXmlObject = nameXmlObject.replace(" ", "");
	}

	@Override
	public String toString() {

		XmlStringBuilder xmlStringBuilder = new XmlStringBuilder();
		Map<String, Object> xml = super.getMapObject();

		xmlStringBuilder.startRoot(nameXmlObject);

		Iterator<String> tags = xml.keySet().iterator();

		while (tags.hasNext()) {
			String tag = tags.next();
			Object value = xml.get(tag);
			try {
				if (tag.contains(" ")) {
					throw new JsonException("The xml tag must not contains Spaces.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				xmlStringBuilder.appendChildren(tag.replaceAll(" ", ""), value);
			}
		}

		xmlStringBuilder.endRoot(nameXmlObject);

		return xmlStringBuilder.result();
	}

	public String prettyXmlResult() {
		return XmlFormat.prettyXml(toString());
	}

}
