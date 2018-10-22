package com.anasabuhussein.xml.extra;

import java.util.Iterator;
import java.util.List;

import com.anasabuhussein.util.ObjectList;

public class XmlArrayObject<T> extends ObjectList<T> {

	private String nameOfList = "list";
	private String nameOfElement = "value";

	public XmlArrayObject() {
		super();
	}

	public XmlArrayObject(String nameOfList, String nameOfElement) {
		super();

		if (nameOfList != null)
			this.nameOfList = nameOfList.replace(" ", "");

		if (nameOfElement != null)
			this.nameOfElement = nameOfElement.replace(" ", "");
	}

	public void setNameOfList(String nameOfList) {
		if (nameOfList != null)
			this.nameOfList = nameOfList.replace(" ", "");
	}

	public void setNameOfValue(String nameOfValue) {
		if (nameOfValue != null)
			this.nameOfElement = nameOfValue.replace(" ", "");
	}

	@Override
	public String toString() {

		int count = 0;

		XmlStringBuilder xmlStringBuilder = new XmlStringBuilder();
		List<?> xmlList = super.getObjectList();
		Iterator<?> iterator = xmlList.iterator();

		if (nameOfList == null && nameOfList.isEmpty()) {
			nameOfList = "list";
		}

		if (nameOfElement == null && nameOfElement.isEmpty()) {
			nameOfElement = "element";
		}

		xmlStringBuilder.startRoot(nameOfList);

		while (iterator.hasNext()) {
			Object value = iterator.next();

			if (nameOfElement == "element")
				xmlStringBuilder.appendChildren(nameOfElement + count, value);

			if (nameOfElement != "element")
				xmlStringBuilder.appendChildren(nameOfElement, value);

			count++;
		}

		xmlStringBuilder.endRoot(nameOfList);
		return xmlStringBuilder.result();
	}

	public String prettyXmlResult() {
		return XmlFormat.prettyXml(toString());
	}
}
