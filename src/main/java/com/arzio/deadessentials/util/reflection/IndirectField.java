package com.arzio.deadessentials.util.reflection;

import java.lang.reflect.Field;

import com.arzio.deadessentials.util.exception.CDAReflectionException;
import com.arzio.deadessentials.util.exception.FinderException;
import com.arzio.deadessentials.util.reflection.finder.ContentFinder;

public class IndirectField<T> {

	private Field field;
	
	public IndirectField(Class<?> clazz, ContentFinder<Field> finder) {
		try {
			this.field = finder.find(clazz);
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public T getValue(Object instance) {
		try {
			return (T) field.get(instance);
		} catch (Exception e) {
			throw new CDAReflectionException("There was an error when trying to get a value from a field", e);
		}
	}
	
	public void setValue(Object instance, T value) {
		try {
			field.set(instance, value);
		} catch (Exception e) {
			throw new CDAReflectionException("There was an error when trying to set a value in a field", e);
		}
	}
}