/*
 * Copyright (c) 2010-2017 fork3
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author n3k0nation
 *
 */
public class AnnotationUtils {
	private AnnotationUtils() {
		throw new RuntimeException();
	}
	
	public static <T extends Annotation> List<Annotation> getAnnotationsAnnotatedBy(Class<?> clazz, Class<T> annotation) {
		final ArrayList<Annotation> list = new ArrayList<>();
		getAnnotationsAnnotatedBy(clazz, annotation, list);
		return list;
	}
	
	public static <T extends Annotation> void getAnnotationsAnnotatedBy(Class<?> clazz, Class<T> annotation, List<Annotation> list) {
		collectAnnotationsAnnotatedBy(clazz.getAnnotations(), annotation, list);
		
		final Class<?> superClass = clazz.getSuperclass();
		if(superClass == null || superClass == Object.class) {
			return;
		}
		
		getAnnotationsAnnotatedBy(superClass, annotation, list);
	}
	
	public static <T extends Annotation> List<Annotation> getAnnotationsAnnotatedBy(Method method, Class<T> annotation) {
		final ArrayList<Annotation> list = new ArrayList<>();
		collectAnnotationsAnnotatedBy(method.getAnnotations(), annotation, list);
		return list;
	}
	
	public static <T extends Annotation> List<Annotation> getAnnotationsAnnotatedBy(Field field, Class<T> annotation) {
		final ArrayList<Annotation> list = new ArrayList<>();
		collectAnnotationsAnnotatedBy(field.getAnnotations(), annotation, list);
		return list;
	}
	
	private static <T extends Annotation> void collectAnnotationsAnnotatedBy(Annotation[] anns, Class<T> annotation, List<Annotation> list) {
		for(int j = 0; j < anns.length; j++) {
			final Annotation ann = anns[j];
			if(ann.annotationType() == annotation) {
				list.add(ann);
			}
		}
	}
}
