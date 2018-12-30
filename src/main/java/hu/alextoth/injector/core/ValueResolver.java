package hu.alextoth.injector.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import hu.alextoth.injector.annotation.Value;
import hu.alextoth.injector.core.helper.AnnotationProcessorHelper;
import hu.alextoth.injector.util.ArrayUtils;
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
				.findFirst().orElse(null);

		if (valueAnnotation == null) {
			return ClassUtils.getDefaultValueForPrimitive(field.getType());
		}

		return extractValueOf(field.getType(), valueAnnotation);
	}

	/**
	 * Returns the value of the {@link Value} annotation for the given parameter, or
	 * a default value if it's not present.
	 * 
	 * @param parameter Parameter of which value must be returned.
	 * @return The value of the {@link Value} annotation for the given parameter, or
	 *         a default value if it's not present.
	 */
	public Object getValueOf(Parameter parameter) {
		Annotation valueAnnotation = Arrays.stream(parameter.getAnnotations())
				.filter(annotation -> annotationProcessorHelper.isValueAnnotation(annotation.annotationType()))
				.findFirst().orElse(null);

		if (valueAnnotation == null) {
			return ClassUtils.getDefaultValueForPrimitive(parameter.getType());
		}

		return extractValueOf(parameter.getType(), valueAnnotation);
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

			String[] stringValues = (String[]) valueAttribute.invoke(valueInstance);

			if (clazz.isArray()) {
				return ArrayUtils.convertToPrimitiveArray(clazz.getComponentType(), stringValues);
			}
			return ClassUtils.convertToPrimitive(clazz, stringValues[0]);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new IllegalArgumentException(
					String.format("Annotation %s must have '%s' attribute to be used as value.", valueClass.getName(),
							valueAttributeName),
					e);
		}
	}

}
