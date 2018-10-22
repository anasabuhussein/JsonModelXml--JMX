package com.anasabuhussein.xml.extra;

import java.util.LinkedList;
import java.util.List;

/**
 * This class wil convert from XmlObject to String .
 **/
public class XmlStringBuilder {

	private StringBuilder builder = new StringBuilder();
	final List<xmlState> stack;
	private int stackSize;

	public XmlStringBuilder() {
		super();
		stack = new LinkedList<>();
	}

	enum xmlState {
		BEGIN, VALUE, NEXT, END;
	}

	private int checkSizeStack() {
		if (!stack.isEmpty()) {
			return stackSize = stack.size() - 1;
		}
		return 0;
	}

	private void begin() {
		if (stack.isEmpty() || stack.get(checkSizeStack()) == xmlState.NEXT)
			stack.add(xmlState.BEGIN);
	}

	private void value() {
		if (stack.get(checkSizeStack()) == xmlState.BEGIN)
			stack.add(xmlState.VALUE);
	}

	private void end() {
		if (stack.get(stackSize) == xmlState.VALUE)
			stack.add(xmlState.END);
	}

	private void next() {
		if (stack.get(stackSize) == xmlState.END)
			stack.add(xmlState.NEXT);
	}

	private void startObject(String tag) {
		begin();
		builder.append("<" + tag + ">");
	}

	private void addValue(Object value) {
		value();
		builder.append(value);
	}

	private void endObject(String endTag) {
		end();
		builder.append("</" + endTag + ">");
	}

	public void appendChildren(String tag, Object value) {
		startObject(tag);
		addValue(value);
		endObject(tag);
		next();
	}

	public void startRoot(String tag) {
		startObject(tag);
		value();
	}

	public void endRoot(String endTag) {
		endObject(endTag);
	}

	public String result() {
		return builder.toString();
	}

	public StringBuilder getBuilder() {
		return builder;
	}

}
