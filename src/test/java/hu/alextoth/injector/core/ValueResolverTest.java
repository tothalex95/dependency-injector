package hu.alextoth.injector.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import hu.alextoth.injector.annotation.Value;
import hu.alextoth.injector.core.helper.AnnotationProcessorHelper;
import hu.alextoth.injector.demo.ConfigClass;
import hu.alextoth.injector.demo.DemoInjectableOne;
import hu.alextoth.injector.demo.DemoValue;
import hu.alextoth.injector.demo.DemoWrongValue;

@ExtendWith(MockitoExtension.class)
public class ValueResolverTest {

	@Mock
	private AnnotationProcessorHelper annotationProcessorHelper;

	@InjectMocks
	private ValueResolver valueResolver;

	@Value("true")
	private boolean demoBoolean;

	@Value("true")
	private Boolean demoBoolean2;

	@Value("111")
	private byte demoByte;

	@Value("111")
	private Byte demoByte2;

	@Value("C")
	private char demoChar;

	@Value("t")
	private Character demoCharacter;

	@Value("3.14")
	private double demoDouble;

	@Value("3.14")
	private Double demoDouble2;

	@Value("2.71f")
	private float demoFloat;

	@Value("2.71f")
	private Float demoFloat2;

	@Value("2018")
	private int demoInt;

	@DemoValue(valueAttribute = "2018")
	private Integer demoInteger;

	@SuppressWarnings("unused")
	private Integer demoInteger2;

	@Value("20181022")
	private long demoLong;

	@Value("20181022")
	private Long demoLong2;

	@Value("1022")
	private short demoShort;

	@Value("1022")
	private Short demoShort2;

	@Value("demoString")
	private String demoString;

	@DemoWrongValue
	private String demoString2;

	@Value("demoObject")
	private Object demoObject;

	@Value({ "2018", "12", "26" })
	private int[] demoIntArray;

	@Value({ "2018", "12", "26" })
	private Integer[] demoIntegerArray;

	@BeforeEach
	public void setUp() {
		Mockito.when(annotationProcessorHelper.isValueAnnotation(Value.class)).thenReturn(true);
		Mockito.when(annotationProcessorHelper.isValueAnnotation(DemoValue.class)).thenReturn(true);
		Mockito.when(annotationProcessorHelper.isValueAnnotation(DemoWrongValue.class)).thenReturn(true);
	}

	@AfterEach
	public void tearDown() {
		Mockito.reset(annotationProcessorHelper);
	}

	@Test
	public void testGetValueOf() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
		assertEquals(true, valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoBoolean")));
		assertEquals(true, valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoBoolean2")));

		assertEquals((byte) 111, valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoByte")));
		assertEquals((byte) 111, valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoByte2")));

		assertEquals('C', valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoChar")));
		assertEquals('t', valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoCharacter")));

		assertEquals(3.14, valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoDouble")));
		assertEquals(3.14, valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoDouble2")));

		assertEquals(2.71f, valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoFloat")));
		assertEquals(2.71f, valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoFloat2")));

		assertEquals(2018, valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoInt")));
		assertEquals(2018, valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoInteger")));
		assertEquals(0, valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoInteger2")));

		assertEquals(20181022L, valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoLong")));
		assertEquals(20181022L, valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoLong2")));

		assertEquals((short) 1022, valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoShort")));
		assertEquals((short) 1022, valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoShort2")));

		assertEquals("demoString", valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoString")));
		assertThrows(IllegalArgumentException.class,
				() -> valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoString2")));

		assertThrows(IllegalArgumentException.class,
				() -> valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoObject")));

		demoIntArray = (int[]) valueResolver.getValueOf(ValueResolverTest.class.getDeclaredField("demoIntArray"));
		assertNotNull(demoIntArray);
		assertEquals(3, demoIntArray.length);
		assertEquals(2018, demoIntArray[0]);
		assertEquals(12, demoIntArray[1]);
		assertEquals(26, demoIntArray[2]);

		demoIntegerArray = (Integer[]) valueResolver
				.getValueOf(ValueResolverTest.class.getDeclaredField("demoIntegerArray"));
		assertNotNull(demoIntegerArray);
		assertEquals(3, demoIntegerArray.length);
		assertEquals(Integer.valueOf(2018), demoIntegerArray[0]);
		assertEquals(Integer.valueOf(12), demoIntegerArray[1]);
		assertEquals(Integer.valueOf(26), demoIntegerArray[2]);

		assertEquals(Integer.valueOf(20181230),
				valueResolver.getValueOf(ConfigClass.class.getDeclaredMethod("getValueInjectedDemoInjectableOne",
						Integer.class, String.class, float[].class, String[].class).getParameters()[0]));
		assertEquals("Happy New Year!",
				valueResolver.getValueOf(ConfigClass.class.getDeclaredMethod("getValueInjectedDemoInjectableOne",
						Integer.class, String.class, float[].class, String[].class).getParameters()[1]));
		float[] floats = (float[]) valueResolver
				.getValueOf(ConfigClass.class.getDeclaredMethod("getValueInjectedDemoInjectableOne", Integer.class,
						String.class, float[].class, String[].class).getParameters()[2]);
		assertEquals(2018f, floats[0]);
		assertEquals(12.30f, floats[1]);
		String[] strings = (String[]) valueResolver
				.getValueOf(ConfigClass.class.getDeclaredMethod("getValueInjectedDemoInjectableOne", Integer.class,
						String.class, float[].class, String[].class).getParameters()[3]);
		assertEquals("2018.12.30.", strings[0]);
		assertEquals(0, valueResolver.getValueOf(
				DemoInjectableOne.class.getDeclaredMethod("setDemoInteger", Integer.class).getParameters()[0]));
	}

}
