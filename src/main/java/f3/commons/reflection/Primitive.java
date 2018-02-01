/*
 * Copyright (c) 2010-2018 fork3
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author n3k0nation
 *
 */
public class Primitive {

	private final static Map<Class<?>, Class<?>> primitiveToWrap = new HashMap<>();
	static {
		primitiveToWrap.put(byte.class, Byte.class);
		primitiveToWrap.put(short.class, Short.class);
		primitiveToWrap.put(char.class, Character.class);
		primitiveToWrap.put(int.class, Integer.class);
		primitiveToWrap.put(long.class, Long.class);
		primitiveToWrap.put(float.class, Float.class);
		primitiveToWrap.put(double.class, Double.class);
		primitiveToWrap.put(void.class, Void.class);
	}
	
	public static Class<?> getWrap(Class<?> clazz) {
		if(!clazz.isPrimitive()) {
			return clazz;
		}
		
		return primitiveToWrap.get(clazz);
	}
	
	private final static Map<Class<?>, Class<?>> wrapToPrimitive = new HashMap<>();
	static {
		wrapToPrimitive.put(Byte.class, byte.class);
		wrapToPrimitive.put(Short.class, short.class);
		wrapToPrimitive.put(Character.class, char.class);
		wrapToPrimitive.put(Integer.class, int.class);
		wrapToPrimitive.put(Long.class, long.class);
		wrapToPrimitive.put(Float.class, float.class);
		wrapToPrimitive.put(Double.class, double.class);
		wrapToPrimitive.put(Void.class, void.class);
	}
	
	public static Class<?> getPrimitive(Class<?> clazz) {
		if(clazz.isPrimitive()) {
			return clazz;
		}
		
		return wrapToPrimitive.get(clazz);
	}
}
