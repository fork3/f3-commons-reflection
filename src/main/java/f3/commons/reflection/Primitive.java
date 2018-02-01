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

/**
 * @author n3k0nation
 *
 */
public class Primitive {
	
	private Primitive() {
	}

	/** Return wrap class type for primitive class type. 
	 * If incoming class is not primitive type return null. */
	public static Class<?> getWrap(Class<?> clazz) {
		if(!clazz.isPrimitive()) {
			return null;
		} else if(clazz == byte.class) {
			return Byte.class;
		} else if(clazz == short.class) {
			return Short.class;
		} else if(clazz == char.class) {
			return Character.class;
		} else if(clazz == int.class) {
			return Integer.class;
		} else if(clazz == long.class) {
			return Long.class;
		} else if(clazz == float.class) {
			return Float.class;
		} else if(clazz == double.class) {
			return Double.class;
		} else {
			return Void.class;
		}
	}
	
	/** Return primitive class type for wrap class type.
	 * If incoming class is primitive type return null. */
	public static Class<?> getPrimitive(Class<?> clazz) {
		if(clazz.isPrimitive()) {
			return null;
		} else if(clazz == Byte.class) {
			return byte.class;
		} else if(clazz == Short.class) {
			return short.class;
		} else if(clazz == Character.class) {
			return char.class;
		} else if(clazz == Integer.class) {
			return int.class;
		} else if(clazz == Long.class) {
			return long.class;
		} else if(clazz == Float.class) {
			return float.class;
		} else if(clazz == Double.class) {
			return double.class;
		} else {
			return void.class;
		}
	}
}
