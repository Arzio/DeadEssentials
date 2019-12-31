package com.arzio.deadessentials.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;

import com.arzio.deadessentials.DeadEssentials;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraftforge.event.Event;

public class ReflectionHelper {
	
	public static interface FieldChecker<T> {
		public boolean isCorrect(T found, Field field) throws Exception;
	}
	
	public static void setFinalStatic(Field field, Object newValue) throws Exception {
		setFinal(field, null, newValue);
	}

	public static void setFinal(Field field, Object instance, Object newValue) throws Exception {
		field.setAccessible(true);
		
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(instance, newValue);
	}
	
	public static ModContainer getCraftingDeadModContainer() {
		Loader loader = Loader.instance();
		
		loader.getModList();
		ModContainer modContainer = null;
		
		for (ModContainer mod : loader.getModList() ) {
			String modNameLowerCaseWithoutSpaces = mod.getName().replace(" ", "").toLowerCase();
			if (modNameLowerCaseWithoutSpaces.contains("craftingdead") || modNameLowerCaseWithoutSpaces.startsWith("cd")) {
				modContainer = mod;
				break;
			}
		}
		return modContainer;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getValueFromEvent(Event event, Class<T> result, Class<?>... otherTypes) {
		
		Class<?> currentLoop = event.getClass();
		
		while (currentLoop != Object.class) {
			for (Field f : currentLoop.getDeclaredFields()) {
				try {
					if (f.getType().equals(result)) {
						return (T) f.get(event);
					}
					
					if (otherTypes != null) {
						for (Class<?> type : otherTypes) {
							f.setAccessible(true);
							if (f.getType().equals(type)) {
								return (T) f.get(event);
							}
						}
					}
				} catch (Exception e) {
					DeadEssentials.getInstance().getLogger().log(Level.SEVERE, "Field "+f+" could not be used by the value getter.", e);
				}
			}
			currentLoop = currentLoop.getSuperclass();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Field findValueWithTypeAndFilter(Object instance, Class<?> clazz, Class<?> type, FieldChecker<T> predicate, String regex) {
		
		for (Field f : clazz.getDeclaredFields()) {
			
			try {
				f.setAccessible(true);
				
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
				
				if (type != null) {
					if (!f.getType().equals(type)) {
						continue;
					}
				}
				
				if (regex != null) {
					if (!f.getName().matches(regex)) {
						continue;
					}
				}
				
				if (predicate != null) {
					
					if (instance == null ? (f.getModifiers() & Modifier.STATIC) != 0 : (f.getModifiers() & Modifier.STATIC) == 0) {
						if (!predicate.isCorrect((T) f.get(instance), f)) {
							continue;
						}
					}
				}
				
				return f;
			} catch (Exception e) {
				DeadEssentials.getInstance().getLogger().log(Level.SEVERE, "Field with value not found in class "+clazz, e);
			}
			
		}
		
		return null;
	}
}
