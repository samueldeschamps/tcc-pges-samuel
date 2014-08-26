package com.generator.core.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.core.res.input.PrimitiveOperations;

public class PrimitiveOperationsTest {

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testByteSum_1() {
        byte actual = PrimitiveOperations.byteSum((byte) 0, (byte) 0);
        Assert.assertEquals((byte) 0, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testByteSum_2() {
        byte actual = PrimitiveOperations.byteSum((byte) 0, (byte) 1);
        Assert.assertEquals((byte) 1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testByteSum_3() {
        byte actual = PrimitiveOperations.byteSum((byte) 0, (byte) -1);
        Assert.assertEquals((byte) -1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testDoubleSum_1() {
        double actual = PrimitiveOperations.doubleSum(0.0, 0.0);
        Assert.assertEquals(0.0, actual, 1.0E-8);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testDoubleSum_2() {
        double actual = PrimitiveOperations.doubleSum(0.0, 1.0);
        Assert.assertEquals(1.0, actual, 1.0E-8);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testDoubleSum_3() {
        double actual = PrimitiveOperations.doubleSum(0.0, -1.0);
        Assert.assertEquals(-1.0, actual, 1.0E-8);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testFloatSum_1() {
        float actual = PrimitiveOperations.floatSum((float) 0.0, (float) 0.0);
        Assert.assertEquals((float) 0.0, actual, 1.0E-8);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testFloatSum_2() {
        float actual = PrimitiveOperations.floatSum((float) 0.0, (float) 1.0);
        Assert.assertEquals((float) 1.0, actual, 1.0E-8);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testFloatSum_3() {
        float actual = PrimitiveOperations.floatSum((float) 0.0, (float) -1.0);
        Assert.assertEquals((float) -1.0, actual, 1.0E-8);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testHashCode_1() {
        int actual = PrimitiveOperations.hashCode(false, (byte) 0, (short) 0, '0', 0, 0, (float) 0.0, 0.0);
        Assert.assertEquals(1253, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testHashCode_2() {
        int actual = PrimitiveOperations.hashCode(true, (byte) 0, (short) 0, '0', 0, 0, (float) 0.0, 0.0);
        Assert.assertEquals(1279, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testHashCode_3() {
        int actual = PrimitiveOperations.hashCode(false, (byte) 0, (short) 0, '1', 0, 0, (float) 0.0, 0.0);
        Assert.assertEquals(1252, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testIntSum_1() {
        int actual = PrimitiveOperations.intSum(0, 0);
        Assert.assertEquals(0, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testIntSum_2() {
        int actual = PrimitiveOperations.intSum(0, 1);
        Assert.assertEquals(1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testIntSum_3() {
        int actual = PrimitiveOperations.intSum(0, -1);
        Assert.assertEquals(-1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testLongSum_1() {
        long actual = PrimitiveOperations.longSum(0, 0);
        Assert.assertEquals(0, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testLongSum_2() {
        long actual = PrimitiveOperations.longSum(0, 1);
        Assert.assertEquals(1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testLongSum_3() {
        long actual = PrimitiveOperations.longSum(0, -1);
        Assert.assertEquals(-1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testShortSum_1() {
        short actual = PrimitiveOperations.shortSum((short) 0, (short) 0);
        Assert.assertEquals((short) 0, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testShortSum_2() {
        short actual = PrimitiveOperations.shortSum((short) 0, (short) 1);
        Assert.assertEquals((short) 1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testShortSum_3() {
        short actual = PrimitiveOperations.shortSum((short) 0, (short) -1);
        Assert.assertEquals((short) -1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testUpCase_1() {
        char actual = PrimitiveOperations.upCase('0');
        Assert.assertEquals('0', actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testUpCase_2() {
        char actual = PrimitiveOperations.upCase('1');
        Assert.assertEquals('1', actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testUpCase_3() {
        char actual = PrimitiveOperations.upCase('2');
        Assert.assertEquals('2', actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testXor_1() {
        boolean actual = PrimitiveOperations.xor(false, false);
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testXor_2() {
        boolean actual = PrimitiveOperations.xor(true, false);
        Assert.assertEquals(true, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testXor_3() {
        boolean actual = PrimitiveOperations.xor(false, true);
        Assert.assertEquals(true, actual);
    }
}
