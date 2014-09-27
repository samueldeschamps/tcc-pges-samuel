package com.generator.core.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.core.res.input.SimpleIntCalculator;

public class SimpleIntCalculatorTest {

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testDivide_1() {
        int actual = SimpleIntCalculator.divide(0, 1);
        Assert.assertEquals(0, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testDivide_2() {
        int actual = SimpleIntCalculator.divide(0, -1);
        Assert.assertEquals(0, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testDivide_3() {
        int actual = SimpleIntCalculator.divide(0, 2);
        Assert.assertEquals(0, actual);
    }

    /**
     * Coverage: 88,24%
     */
    @Test
    public void testIsPrime_1() {
        boolean actual = SimpleIntCalculator.isPrime(32767);
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 88,24%
     */
    @Test
    public void testIsPrime_2() {
        boolean actual = SimpleIntCalculator.isPrime(37011083);
        Assert.assertEquals(true, actual);
    }

    /**
     * FIXME: Infinite loop detected! (Took more than 3 seconds)
     */
    @Test
    public void testIsPrime_3() {
        SimpleIntCalculator.isPrime(2147483647);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testMultiply_1() {
        int actual = SimpleIntCalculator.multiply(0, 0);
        Assert.assertEquals(0, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testMultiply_2() {
        int actual = SimpleIntCalculator.multiply(0, 1);
        Assert.assertEquals(0, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testMultiply_3() {
        int actual = SimpleIntCalculator.multiply(0, -1);
        Assert.assertEquals(0, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testSubtract_1() {
        int actual = SimpleIntCalculator.subtract(0, 0);
        Assert.assertEquals(0, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testSubtract_2() {
        int actual = SimpleIntCalculator.subtract(0, 1);
        Assert.assertEquals(-1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testSubtract_3() {
        int actual = SimpleIntCalculator.subtract(0, -1);
        Assert.assertEquals(1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testSum_1() {
        int actual = SimpleIntCalculator.sum(0, 0);
        Assert.assertEquals(0, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testSum_2() {
        int actual = SimpleIntCalculator.sum(0, 1);
        Assert.assertEquals(1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testSum_3() {
        int actual = SimpleIntCalculator.sum(0, -1);
        Assert.assertEquals(-1, actual);
    }
}
