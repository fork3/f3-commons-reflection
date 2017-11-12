/*
 * Copyright (c) 2010-2016 fork2
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR 
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package f3.commons.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import f3.commons.reflection.exception.ClassNotFoundUncheckedException;
import f3.commons.reflection.exception.ReflectiveOperationUncheckedException;

/**
 * @author n3k0nation
 *
 */
public class ClassUtils {
	/** Class.forName with runtime exception */
	public static Class<?> getClass(String path) throws ClassNotFoundUncheckedException {
		try {
			return Class.forName(path);
		} catch(ClassNotFoundException e) {
			throw new ClassNotFoundUncheckedException(e);
		}
	}
	
	/** Class.forName with runtime exception */
	public static Class<?> getClass(String path, ClassLoader classLoader) throws ClassNotFoundUncheckedException {
		try {
			return Class.forName(path, true, classLoader);
		} catch(ClassNotFoundException e) {
			throw new ClassNotFoundUncheckedException(e);
		}
	}
	
	/** Class.forName with runtime exception */
	public static Class<?> getClass(String path, boolean initialize, ClassLoader classLoader) throws ClassNotFoundUncheckedException {
		try {
			return Class.forName(path, initialize, classLoader);
		} catch(ClassNotFoundException e) {
			throw new ClassNotFoundUncheckedException(e);
		}
	}
	
	public static boolean isSingleton(Class<?> clazz) {
		Method method;
		try {
			method = clazz.getDeclaredMethod("getInstance");
		} catch(NoSuchMethodException e) {
			method = null;
		}
		
		return method != null;
	}
	
	public static Object createInstance(Class<?> clazz) throws ReflectiveOperationUncheckedException {
		Object object = singletonInstance(clazz);
		if(object == null) {
			try {
				return clazz.newInstance();
			} catch (ReflectiveOperationException e) {
				throw new ReflectiveOperationUncheckedException(
						"Failed to create class '" + clazz.getName() + "', method not found and constructor not found.",
						e
				);
			}
		}
		
		return object;
	}
	
	public static Object singletonInstance(Class<?> clazz) {
		final Method method;
		try {
			method = clazz.getDeclaredMethod("getInstance");
		} catch (NoSuchMethodException e) {
			return null;
		}
		
		try {
			return method.invoke(null);
		} catch (Throwable e) {
			return null;
		}
	}
	
	public static Object createInstance(Class<?> clazz, Object... params) throws RuntimeException {
		if(Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers())) {
			throw new RuntimeException("Class " + clazz.getCanonicalName() + " is interface or abstract!");
		}
		
		Constructor<?>[] constructors = clazz.getConstructors();
		if(constructors.length < 1) {
			throw new RuntimeException("Class " + clazz.getCanonicalName() + " doesnt have public constructors");
		}
		
		constructorLabel:
		for(int i = 0; i < constructors.length; i++) {
			final Constructor<?> constructor = constructors[i];
			
			Class<?>[] parameters = constructor.getParameterTypes();
			if(parameters.length != params.length) {
				continue;
			}
			
			if(parameters.length < 1) {
				try {
					return constructor.newInstance();
				} catch (ReflectiveOperationException e) {
					throw new ReflectiveOperationUncheckedException(e);
				}
			}
			
			for(int j = 0; j < params.length; j++) {
				final Object param = params[j];
				if(param == null) {
					continue;
				}
				
				final Class<?> parameter = parameters[j];
				final Class<?> paramClass = param.getClass();
				if(parameter.isPrimitive() && !paramClass.isPrimitive() && isPrimitiveWrap(paramClass, parameter)) {
					params[j] = transformStringToType(param.toString(), parameter);
					continue;
				}
				
				try {
					param.getClass().asSubclass(parameter);
				} catch(ClassCastException e) {
					continue constructorLabel;
				}
			}
			
			try {
				return constructor.newInstance(params);
			} catch(ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("Not found constructor with specified params. ");
		sb.append("Class: ").append(clazz.getCanonicalName()).append("; ");
		sb.append("Params: ");
		for(int i = 0; i < params.length; i++) {
			final Object param = params[i];
			sb.append(param == null ? "null" : param.getClass().getSimpleName());
			if(i != params.length - 1) {
				sb.append(", ");
			}
		}
		
		throw new RuntimeException(sb.toString());
	}
	
	public static boolean isPrimitiveWrap(Class<?> clazz, Class<?> primitiveClass) {
		try {
			Field field = clazz.getDeclaredField("TYPE");
			field.setAccessible(true);
			Class<?> type = (Class<?>) field.get(null);
			return type == primitiveClass || primitiveClass.isAssignableFrom(clazz);
		} catch (ReflectiveOperationException e) {
			return false;
		}
	}
	
	/** primitives, primitives arrays, enums. exclude string arrays */
	public static Object transformStringToType(String val, Class<?> type) {
		if(type.isAssignableFrom(String.class)) {
			return val;
		} else if(type.isAssignableFrom(Byte.TYPE) || type.isAssignableFrom(Byte.class) || type.isAssignableFrom(byte.class)) {
			return Byte.parseByte(val);
		} else if(type.isAssignableFrom(Character.TYPE) || type.isAssignableFrom(Character.class) || type.isAssignableFrom(char.class)) {
			return val.toCharArray()[0];
		} else if(type.isAssignableFrom(Short.TYPE) || type.isAssignableFrom(Short.class) || type.isAssignableFrom(short.class)) {
			return Short.parseShort(val);
		} else if(type.isAssignableFrom(Integer.TYPE) || type.isAssignableFrom(Integer.class) || type.isAssignableFrom(int.class)) {
			return Integer.parseInt(val);
		} else if(type.isAssignableFrom(Long.TYPE) || type.isAssignableFrom(Long.class) || type.isAssignableFrom(long.class)) {
			return Long.parseLong(val);
		} else if(type.isAssignableFrom(Float.TYPE) || type.isAssignableFrom(Float.class) || type.isAssignableFrom(float.class)) {
			return Float.parseFloat(val);
		} else if(type.isAssignableFrom(Double.TYPE) || type.isAssignableFrom(Double.class) || type.isAssignableFrom(double.class)) {
			return Double.parseDouble(val);
		} else if(type.isAssignableFrom(Boolean.TYPE) || type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class)) {
			return Boolean.parseBoolean(val);
		} else if(type.isAssignableFrom(Byte[].class) || type.isAssignableFrom(byte[].class)) {
			String[] values = val.split("\\s|,|;");
			byte[] array = new byte[values.length];
			for(int i = 0; i < values.length; i++) {
				array[i] = Byte.parseByte(values[i]);
			}
			return array;
		} else if(type.isAssignableFrom(Short[].class) || type.isAssignableFrom(short[].class)) {
			String[] values = val.split("\\s|,|;");
			short[] array = new short[values.length];
			for(int i = 0; i < values.length; i++) {
				array[i] = Short.parseShort(values[i]);
			}
			return array;
		} else if(type.isAssignableFrom(Integer[].class) || type.isAssignableFrom(int[].class)) {
			String[] values = val.split("\\s|,|;");
			int[] array = new int[values.length];
			for(int i = 0; i < values.length; i++) {
				array[i] = Integer.parseInt(values[i]);
			}
			return array;
		} else if(type.isAssignableFrom(Long[].class) || type.isAssignableFrom(long[].class)) {
			String[] values = val.split("\\s|,|;");
			long[] array = new long[values.length];
			for(int i = 0; i < values.length; i++) {
				array[i] = Long.parseLong(values[i]);
			}
			return array;
		} else if(type.isAssignableFrom(Float[].class) || type.isAssignableFrom(float[].class)) {
			String[] values = val.split("\\s|,|;");
			float[] array = new float[values.length];
			for(int i = 0; i < values.length; i++) {
				array[i] = Float.parseFloat(values[i]);
			}
			return array;
		} else if(type.isAssignableFrom(Double[].class) || type.isAssignableFrom(double[].class)) {
			String[] values = val.split("\\s|,|;");
			double[] array = new double[values.length];
			for(int i = 0; i < values.length; i++) {
				array[i] = Double.parseDouble(values[i]);
			}
			return array;
		} else if(type.isAssignableFrom(Boolean[].class) || type.isAssignableFrom(boolean[].class)) {
			String[] values = val.split("\\s|,|;");
			boolean[] array = new boolean[values.length];
			for(int i = 0; i < values.length; i++) {
				array[i] = Boolean.parseBoolean(values[i]);
			}
			return array;
		} else if(type.isEnum()) {
			Class<? extends Enum> clazz = type.asSubclass(Enum.class);
			Enum[] constants = clazz.getEnumConstants();
			for(Enum e : constants) {
				if(e.name().toLowerCase().equals(val.toLowerCase())) {
					return e;
				}
			}
			throw new RuntimeException("Enum constant not found");
		} else throw new RuntimeException("Unknown default type");
	}
	
	/** return first class from hierarchy annotated specified annotation. if classes not annotated, return null */
	public static <T extends Annotation> Class<?> getAnnotatedClass(Class<?> clazz, Class<T> annotationClass) {
		if(clazz.isAnnotationPresent(annotationClass)) {
			return clazz;
		}
		
		final Class<?> superClass = clazz.getSuperclass();
		if(superClass == null || superClass == Object.class) {
			return null;
		}
		
		return getAnnotatedClass(superClass, annotationClass);
	}
	
	/** return specified annotation from first class annotated in class hierarchy. if classes not annotated, return null */
	public static <T extends Annotation> T[] getAnnotationClass(Class<?> clazz, Class<T> annotationClass) {
		T[] annotations = clazz.getAnnotationsByType(annotationClass);
		if(annotations.length > 0) {
			return annotations;
		}
		
		final Class<?> superClass = clazz.getSuperclass();
		if(superClass == null || superClass == Object.class) {
			return null;
		}
		
		return getAnnotationClass(superClass, annotationClass);
	}
	
	public static List<Class<?>> getAllParents(Class<?> clazz) {
		List<Class<?>> list = new ArrayList<>();
		getAllParents(clazz, list);
		return list;
	}
	
	public static void getAllParents(Class<?> clazz, List<Class<?>> out) {
		out.add(clazz);
		
		final Class<?> superClass = clazz.getSuperclass();
		if(superClass == null || superClass == Object.class) {
			return;
		}
		
		getAllParents(superClass, out);
	}
	
	/** return implementation of parent (if parent is abstract or interface). if childs more than one throw exception.
	 * if child not found throw exception */
	public static Class<?> getChildOf(Class<?> parent, List<Class<?>> classes, boolean withSynthetic) throws ClassNotFoundUncheckedException {
		int modifiers = parent.getModifiers();
		if(!Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
			return parent;
		}
		
		Class<?> child = null;
		for(int i = 0; i < classes.size(); i++) {
			final Class<?> clazz = classes.get(i);
			modifiers = clazz.getModifiers();
			if(Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
				continue;
			}
			
			if(!withSynthetic && clazz.isSynthetic()) {
				continue;
			}
			
			if(parent.isAssignableFrom(clazz)) {
				if(child != null) {
					throw new ClassNotFoundUncheckedException("Childs more than one");
				}
				
				child = clazz;
			}
		}
		
		if(child == null) {
			throw new ClassNotFoundUncheckedException("Child not found");
		}
		
		return child;
	}
	
	public static boolean isAbstractClass(Class<?> clazz) {
		final int modifiers = clazz.getModifiers();
		return Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers);
	}
	
	public static boolean isPublicClass(Class<?> clazz) {
		return Modifier.isPublic(clazz.getModifiers());
	}
}
