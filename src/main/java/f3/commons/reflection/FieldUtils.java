/*
 * Copyright (c) 2010-2017 fork2
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author n3k0nation
 *
 */
public class FieldUtils {
	private FieldUtils() {
		throw new RuntimeException();
	}
	
	/** return annotated fields */ 
	public static <T extends Annotation> List<Field> getAnnotatedField(Class<?> clazz, Class<T> annotationClass) {
		List<Field> list = new ArrayList<>();
		getAnnotatedField(clazz, annotationClass, list);
		return list;
	}
	
	public static <T extends Annotation> void getAnnotatedField(Class<?> clazz, Class<T> annotationClass, List<Field> out) {
		Field[] fields = clazz.getDeclaredFields();
		for(int i = 0; i < fields.length; i++) {
			final Field field = fields[i];
			if(field.isAnnotationPresent(annotationClass)) {
				out.add(field);
			}
		}
		
		final Class<?> superClass = clazz.getSuperclass();
		if(superClass == null || superClass == Object.class) {
			return;
		}
		
		getAnnotatedField(superClass, annotationClass, out);
	}
	
	public static <T extends Annotation> List<T> getAnnotationField(Class<?> clazz, Class<T> annotationClass) {
		List<T> list = new ArrayList<>();
		getAnnotationField(clazz, annotationClass, list);
		return list;
	}
	
	public static <T extends Annotation> void getAnnotationField(Class<?> clazz, Class<T> annotationClass, List<T> out) {
		Field[] fields = clazz.getDeclaredFields();
		for(int i = 0; i < fields.length; i++) {
			final Field field = fields[i];
			final T[] annotations = field.getAnnotationsByType(annotationClass);
			
			for(int j = 0; j < annotations.length; j++) {
				out.add(annotations[j]);
			}
		}
		
		final Class<?> superClass = clazz.getSuperclass();
		if(superClass == null || superClass == Object.class) {
			return;
		}
		
		getAnnotationField(superClass, annotationClass, out);
	}
}
