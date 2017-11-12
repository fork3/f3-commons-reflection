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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author n3k0nation
 *
 */
public class MethodUtils {
	private MethodUtils() {
		throw new RuntimeException();
	}
	
	/** return annotated methods */
	public static <T extends Annotation> List<Method> getAnnotatedMethods(Class<?> clazz, Class<T> annotationClass) {
		List<Method> list = new ArrayList<>();
		getAnnotatedMethods(clazz, annotationClass, list);
		return list;
	}
	
	public static <T extends Annotation> void getAnnotatedMethods(Class<?> clazz, Class<T> annotationClass, List<Method> out) {
		Method[] methods = clazz.getDeclaredMethods();
		for(int i = 0; i < methods.length; i++) {
			final Method method = methods[i];
			if(method.isAnnotationPresent(annotationClass)) {
				out.add(method);
			}
		}
		
		final Class<?> superClass = clazz.getSuperclass();
		if(superClass == null || superClass == Object.class) {
			return;
		}
		
		getAnnotatedMethods(superClass, annotationClass, out);
	}
	
	public static <T extends Annotation> List<T> getAnnotationMethod(Class<?> clazz, Class<T> annotationClass) {
		List<T> list = new ArrayList<>();
		getAnnotationMethod(clazz, annotationClass, list);
		return list;
	}
	
	public static <T extends Annotation> void getAnnotationMethod(Class<?> clazz, Class<T> annotationClass, List<T> out) {
		Method[] methods = clazz.getDeclaredMethods();
		for(int i = 0; i < methods.length; i++) {
			final Method method = methods[i];
			final T[] annotations = method.getAnnotationsByType(annotationClass);
			
			for(int j = 0; j < annotations.length; j++) {
				out.add(annotations[j]);
			}
		}
		
		final Class<?> superClass = clazz.getSuperclass();
		if(superClass == null || superClass == Object.class) {
			return;
		}
		
		getAnnotationMethod(superClass, annotationClass, out);
	}
}
