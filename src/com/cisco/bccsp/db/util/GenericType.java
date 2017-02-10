package com.cisco.bccsp.db.util;

public class GenericType<T> {

	private T value;

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	public GenericType(T value)
	{
		this.value=value;
	}
	
}
