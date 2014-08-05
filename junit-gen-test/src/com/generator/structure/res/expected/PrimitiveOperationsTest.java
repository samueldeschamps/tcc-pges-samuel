package com.generator.structure.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.structure.res.input.PrimitiveOperations;

public class PrimitiveOperationsTest {

    /**Coverage: 100,00%*/
    @Test
    public void testbyteSum_1() {
        byte actual = PrimitiveOperations.byteSum((byte) -128, (byte) -128);
        Assert.assertEquals((byte) 0, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testbyteSum_2() {
        byte actual = PrimitiveOperations.byteSum((byte) -127, (byte) -128);
        Assert.assertEquals((byte) 1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testbyteSum_3() {
        byte actual = PrimitiveOperations.byteSum((byte) -128, (byte) -127);
        Assert.assertEquals((byte) 1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testbyteSum_4() {
        byte actual = PrimitiveOperations.byteSum((byte) -127, (byte) -127);
        Assert.assertEquals((byte) 2, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testbyteSum_5() {
        byte actual = PrimitiveOperations.byteSum((byte) -126, (byte) -128);
        Assert.assertEquals((byte) 2, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testdoubleSum_1() {
        double actual = PrimitiveOperations.doubleSum(0.0, 0.0);
        Assert.assertEquals(0.0, actual, 1.0E-8);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testdoubleSum_2() {
        double actual = PrimitiveOperations.doubleSum(1.0, 0.0);
        Assert.assertEquals(1.0, actual, 1.0E-8);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testdoubleSum_3() {
        double actual = PrimitiveOperations.doubleSum(0.0, 1.0);
        Assert.assertEquals(1.0, actual, 1.0E-8);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testdoubleSum_4() {
        double actual = PrimitiveOperations.doubleSum(1.0, 1.0);
        Assert.assertEquals(2.0, actual, 1.0E-8);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testdoubleSum_5() {
        double actual = PrimitiveOperations.doubleSum(-1.0, 0.0);
        Assert.assertEquals(-1.0, actual, 1.0E-8);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testfloatSum_1() {
        float actual = PrimitiveOperations.floatSum((float) 0.0, (float) 0.0);
        Assert.assertEquals((float) 0.0, actual, 1.0E-8);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testfloatSum_2() {
        float actual = PrimitiveOperations.floatSum((float) 1.0, (float) 0.0);
        Assert.assertEquals((float) 1.0, actual, 1.0E-8);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testfloatSum_3() {
        float actual = PrimitiveOperations.floatSum((float) 0.0, (float) 1.0);
        Assert.assertEquals((float) 1.0, actual, 1.0E-8);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testfloatSum_4() {
        float actual = PrimitiveOperations.floatSum((float) 1.0, (float) 1.0);
        Assert.assertEquals((float) 2.0, actual, 1.0E-8);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testfloatSum_5() {
        float actual = PrimitiveOperations.floatSum((float) -1.0, (float) 0.0);
        Assert.assertEquals((float) -1.0, actual, 1.0E-8);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testhashCode_1() {
        int actual = PrimitiveOperations.hashCode(false, (byte) -128, (short) 0, '0', 0, 0, (float) 0.0, 0.0);
        Assert.assertEquals(-1179, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testhashCode_2() {
        int actual = PrimitiveOperations.hashCode(true, (byte) -128, (short) 0, '0', 0, 0, (float) 0.0, 0.0);
        Assert.assertEquals(-1153, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testhashCode_3() {
        int actual = PrimitiveOperations.hashCode(false, (byte) -127, (short) 0, '0', 0, 0, (float) 0.0, 0.0);
        Assert.assertEquals(-1180, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testhashCode_4() {
        int actual = PrimitiveOperations.hashCode(true, (byte) -127, (short) 0, '0', 0, 0, (float) 0.0, 0.0);
        Assert.assertEquals(-1154, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testhashCode_5() {
        int actual = PrimitiveOperations.hashCode(false, (byte) -128, (short) 1, '0', 0, 0, (float) 0.0, 0.0);
        Assert.assertEquals(-1180, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testintSum_1() {
        int actual = PrimitiveOperations.intSum(0, 0);
        Assert.assertEquals(0, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testintSum_2() {
        int actual = PrimitiveOperations.intSum(1, 0);
        Assert.assertEquals(1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testintSum_3() {
        int actual = PrimitiveOperations.intSum(0, 1);
        Assert.assertEquals(1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testintSum_4() {
        int actual = PrimitiveOperations.intSum(1, 1);
        Assert.assertEquals(2, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testintSum_5() {
        int actual = PrimitiveOperations.intSum(-1, 0);
        Assert.assertEquals(-1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testlongSum_1() {
        long actual = PrimitiveOperations.longSum(0, 0);
        Assert.assertEquals(0, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testlongSum_2() {
        long actual = PrimitiveOperations.longSum(1, 0);
        Assert.assertEquals(1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testlongSum_3() {
        long actual = PrimitiveOperations.longSum(0, 1);
        Assert.assertEquals(1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testlongSum_4() {
        long actual = PrimitiveOperations.longSum(1, 1);
        Assert.assertEquals(2, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testlongSum_5() {
        long actual = PrimitiveOperations.longSum(-1, 0);
        Assert.assertEquals(-1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testshortSum_1() {
        short actual = PrimitiveOperations.shortSum((short) 0, (short) 0);
        Assert.assertEquals((short) 0, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testshortSum_2() {
        short actual = PrimitiveOperations.shortSum((short) 1, (short) 0);
        Assert.assertEquals((short) 1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testshortSum_3() {
        short actual = PrimitiveOperations.shortSum((short) 0, (short) 1);
        Assert.assertEquals((short) 1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testshortSum_4() {
        short actual = PrimitiveOperations.shortSum((short) 1, (short) 1);
        Assert.assertEquals((short) 2, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testshortSum_5() {
        short actual = PrimitiveOperations.shortSum((short) -1, (short) 0);
        Assert.assertEquals((short) -1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testupCase_1() {
        char actual = PrimitiveOperations.upCase('0');
        Assert.assertEquals('0', actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testupCase_2() {
        char actual = PrimitiveOperations.upCase('1');
        Assert.assertEquals('1', actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testupCase_3() {
        char actual = PrimitiveOperations.upCase('2');
        Assert.assertEquals('2', actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testupCase_4() {
        char actual = PrimitiveOperations.upCase('3');
        Assert.assertEquals('3', actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testupCase_5() {
        char actual = PrimitiveOperations.upCase('4');
        Assert.assertEquals('4', actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testxor_1() {
        boolean actual = PrimitiveOperations.xor(false, false);
        Assert.assertEquals(false, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testxor_2() {
        boolean actual = PrimitiveOperations.xor(true, false);
        Assert.assertEquals(true, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testxor_3() {
        boolean actual = PrimitiveOperations.xor(false, true);
        Assert.assertEquals(true, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testxor_4() {
        boolean actual = PrimitiveOperations.xor(true, true);
        Assert.assertEquals(false, actual);
    }
}
