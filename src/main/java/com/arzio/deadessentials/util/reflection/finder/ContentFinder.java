package com.arzio.deadessentials.util.reflection.finder;

import java.lang.reflect.Field;

import com.arzio.deadessentials.util.exception.FinderException;
import com.arzio.deadessentials.util.reflection.ReflectionHelper;
import com.arzio.deadessentials.util.reflection.ReflectionHelper.FieldChecker;

public interface ContentFinder<F> {

	public F find(Class<?> from) throws FinderException;
	
	public static class FieldBuilder<T> {
		
		private Class<?> type;
		private FieldChecker<T> checker;
		private Object instance;
		private String regex;
		
		public FieldBuilder() { }
		
		public FieldBuilder<T> withType(Class<?> returnType) {
			this.type = returnType;
			return this;
		}
		
		public FieldBuilder<T> withExactValue(Object instance, Class<?> type, final T value){
			return this.withValue(instance, type, new FieldChecker<T>() {

				@Override
				public boolean isCorrect(T found, Field field) {
					return found.equals(value);
				}
				
			});
		}
		
		public FieldBuilder<T> withValue(Object instance, Class<?> type, FieldChecker<T> checker) {
			this.withType(type);
			this.checker = checker;
			this.instance = instance;
			return this;
		}
		
		public ContentFinder<Field> build() {
			return new ContentFinder<Field>() {

				@Override
				public Field find(Class<?> from) throws FinderException{
					Field found = ReflectionHelper.findValueWithTypeAndFilter(instance, from, type, checker, regex);
					if (found == null) {
						throw new FinderException("The field could not be found on class "+from.getName()+"!");
					}
					return found;
				}
				
			};
		}
	}
}
