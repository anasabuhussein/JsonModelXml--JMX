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

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;

/**
 * This class commen class for any class work with list and Its components are
 * mainly.
 * 
 * @author Anas Abu-Hussein
 **/
public class ObjectList<T> {

	private List<T> list;

	public ObjectList() {
		list = new LinkedList<>();
	}

	/**
	 * add data to list.
	 **/
	public ObjectList<T> add(T t) {
		list.add(t);
		return this;
	}

	/**
	 * add data wiht index.
	 **/
	private ObjectList<T> add(int index, T t) {
		list.add(index, t);
		return this;
	}

	/**
	 * replace data with check is index not null.
	 **/
	public ObjectList<T> replace(int index, T t) {
		if (list.get(index) != null) {
			add(index, t);
		}
		return this;
	}

	/**
	 * remove data from this index.
	 **/
	public ObjectList<T> remove(int index) {
		list.remove(index);
		return this;
	}

	/**
	 * remove data depend on object.
	 **/
	public ObjectList<T> remove(T t) {
		list.remove(t);
		return this;
	}

	/**
	 * check if list has value.
	 **/
	public boolean has(T t) {
		if (list.contains(t))
			return true;

		return false;
	}

	/**
	 * get data depned on index from list.
	 **/
	public T get(int index) {
		return list.get(index);
	}

	/**
	 * get size of list.
	 **/
	public int sizeList() {
		return list.size();
	}

	/**
	 * get list.
	 **/
	@XmlAnyElement
	public List<T> getObjectList() {
		return list;
	}

	/**
	 * set new list.
	 **/
	public void setObjectList(List<T> list) {
		this.list = list;
	}
}
