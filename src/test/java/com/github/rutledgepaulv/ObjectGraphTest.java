package com.github.rutledgepaulv;

import com.github.rutledgepaulv.graph.ObjectGraph;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("unchecked")
public class ObjectGraphTest {

    private ObjectGraph graph;
    private Object instance;
    private String prefix;

    private class TestMe {
        private Boolean booleanValue = true;
        private Integer integerValue = 0;
        private Long longValue = 2L;
        private String stringValue = "hello";
        private Character characterValue = 'a';
        private Short shortValue = 0;
        private Float floatValue = 33f;
        private Double doubleValue = 5.0;
        private Byte byteValue = 0;
    }

    private class TestMe2 {
        private Boolean booleanValue = false;
        private Integer integerValue = 1;
        private Long longValue = 3L;
        private String stringValue = "hello world";
        private Character characterValue = 'b';
        private Short shortValue = 1;
        private Float floatValue = 34f;
        private Double doubleValue = 6.0;
        private Byte byteValue = 1;
    }


    private class Composite {
        TestMe testMe1 = new TestMe();
        TestMe2 testMe2 = new TestMe2();
    }

    private class DoubleComposite {
        Composite composite = new Composite();
    }

    private class ReflexiveWithSuperClass extends TestMe {
        TestMe testMe = new TestMe();
    }

    private class ReflexiveWithSelfClass extends TestMe {
        ReflexiveWithSelfClass testMe = new ReflexiveWithSelfClass();
    }

    private class ReflexiveWithSubClass extends TestMe {
        ChildWithAdditionalPrimitives child = new ChildWithAdditionalPrimitives();
    }

    private class ChildWithAdditionalPrimitives extends TestMe {
        private Integer subField = 44;
    }

    private class ChildWithSameNamePrimitivesAsParent extends TestMe {
        private Boolean booleanValue = false;
        private Integer integerValue = 1;
        private Long longValue = 3L;
        private String stringValue = "hello world";
        private Character characterValue = 'b';
        private Short shortValue = 1;
        private Float floatValue = 34f;
        private Double doubleValue = 6.0;
        private Byte byteValue = 1;
    }


    @Before
    public void setUp() {
        prefix = "";
    }

    @Test
    public void testResolutionOfPrimitivesOnRootObject() {
        instance = new TestMe();
        graph = new ObjectGraph(TestMe.class);
        fieldsSet1();
    }

    @Test
    public void testResolutionOfPrimitivesForSubclass() {
        instance = new ChildWithAdditionalPrimitives();
        graph = new ObjectGraph(ChildWithAdditionalPrimitives.class);
        fieldsSet1();
        fieldSet2();
    }

    @Test
    public void testResolutionOfOverriddenPrimitivesOnSubclass() {
        instance = new ChildWithSameNamePrimitivesAsParent();
        graph = new ObjectGraph(ChildWithSameNamePrimitivesAsParent.class);
        fieldSet1AlternativeValues();
    }

    @Test
    public void testSingleDotDelimitedResolutionOfPrimitives() {
        instance = new Composite();
        graph = new ObjectGraph(Composite.class);
        prefix = "testMe1.";
        fieldsSet1();
        prefix = "testMe2.";
        fieldSet1AlternativeValues();
    }


    @Test
    public void testDoubleDotDelimitedResolutionOfPrimitives() {
        instance = new DoubleComposite();
        graph = new ObjectGraph(DoubleComposite.class);
        prefix = "composite.testMe1.";
        fieldsSet1();
        prefix = "composite.testMe2.";
        fieldSet1AlternativeValues();
    }


    private void fieldsSet1() {
        assertEquals(true, resolve("booleanValue"));
        assertEquals((Integer) 0, resolve("integerValue"));
        assertEquals((Long) 2L, resolve("longValue"));
        assertEquals("hello", resolve("stringValue"));
        assertEquals((Character) 'a', resolve("characterValue"));
        assertEquals((short) 0, (short) resolve("shortValue"));
        assertEquals((Float) 33f, resolve("floatValue"));
        assertEquals((Double) 5.0, resolve("doubleValue"));
        assertEquals((byte) 0, (byte) resolve("byteValue"));
    }

    private void fieldSet2() {
        assertEquals((Integer) 44, resolve("subField"));
    }

    private void fieldSet1AlternativeValues() {
        assertEquals(false, resolve("booleanValue"));
        assertEquals((Integer) 1, resolve("integerValue"));
        assertEquals((Long) 3L, resolve("longValue"));
        assertEquals("hello world", resolve("stringValue"));
        assertEquals((Character) 'b', resolve("characterValue"));
        assertEquals((short) 1, (short) resolve("shortValue"));
        assertEquals((Float) 34f, resolve("floatValue"));
        assertEquals((Double) 6.0, resolve("doubleValue"));
        assertEquals((byte) 1, (byte) resolve("byteValue"));
    }

    private <T> T resolve(String property) {
        return (T) graph.resolveValueNode(prefix + property).value(instance);
    }

}