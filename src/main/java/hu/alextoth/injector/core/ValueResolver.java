package hu.alextoth.injector.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import hu.alextoth.injector.annotation.Value;
import hu.alextoth.injector.util.ClassUtils;

/**
 * Class for resolving value of elements annotated with {@link Value}.
 * 
 * @author Alex Toth
 */
public class ValueResolver {

	private final AnnotationProcessorHelper annotationProcessorHelper;

	public ValueResolver(AnnotationProcessorHelper annotationProcessorHelper) {
		this.annotationProcessorHelper = annotationProcessorHelper;
	}

	/**
	 * Returns the value of the {@link Value} annotation for the given field, or a
	 * default value if it's not present.
	 * 
	 * @param field Field of which value must be returned.
	 * @return The value of the {@link Value} annotation for the given field, or a
	 *         default value if it's not present.
	 */
	public Object getValueOf(Field field) {
		Annotation valueAnnotation = Arrays.stream(field.getAnnotations())
				.filter(annotation -> annotationProcessorHelper.isValueAnnotation(annotation.annotationType()))
				.findFirst()
				.orElse(null);
		
		if (valueAnnotation == null) {
			return ClassUtils.getDefaultValueForPrimitive(field.getType());
		}

		return extractValueOf(field.getType(), valueAnnotation);
	}

	/**
	 * Extracts the value of the given annotation instance.
	 * 
	 * @param annotation Annotation instance of which value must be extracted.
	 * @return The extracted value of the given annotation instance.
	 */
	private Object extractValueOf(Class<?> clazz, Annotation annotation) {
		Class<?> valueClass = annotation.annotationType();
		Object valueInstance = valueClass.cast(annotation);

		Value valueAnnotation = valueClass.getAnnotation(Value.class);
		String valueAttributeName = valueAnnotation == null ? Value.DEFAULT_VALUE_ATTRIBUTE_NAME
				: valueAnnotation.valueAttributeName();

		try {
			Method valueAttribute = valueClass.getMethod(valueAttributeName);

			String stringValue = String.valueOf(valueAttribute.invoke(valueInstance));

			if (String.class.equals(clazz)) {
				return stringValue;
			} else if (ClassUtils.isPrimitiveOrWrapper(clazz)) {
				if (boolean.class.equals(clazz) || Boolean.class.equals(clazz)) {
					return Boolean.valueOf(stringValue);
				} else if (byte.class.equals(clazz) || Byte.class.equals(clazz)) {
					return Byte.valueOf(stringValue);
				} else if (char.class.equals(clazz) || Character.class.equals(clazz)) {
					return Character.valueOf(stringValue.charAt(0));
				} else if (double.class.equals(clazz) || Double.class.equals(clazz)) {
					return Double.valueOf(stringValue);
				} else if (float.class.equals(clazz) || Float.class.equals(clazz)) {
					return Float.valueOf(stringValue);
				} else if (int.class.equals(clazz) || Integer.class.equals(clazz)) {
					return Integer.valueOf(stringValue);
				} else if (long.class.equals(clazz) || Long.class.equals(clazz)) {
					return Long.valueOf(stringValue);
				} else /* (short.class.equals(clazz) || Short.class.equals(clazz)) */ {
					return Short.valueOf(stringValue);
				}
			} else {
				return null;
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new IllegalArgumentException(
					String.format("Annotation %s must have '%s' attribute to be used as value.", valueClass.getName(),
							valueAttributeName),
					e);
		}
	}

}
