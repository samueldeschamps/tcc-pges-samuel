package com.generator.structure.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.structure.res.input.SimpleIntCalculator;

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
        int actual = SimpleIntCalculator.divide(-1, 1);
        Assert.assertEquals(-1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testDivide_3() {
        int actual = SimpleIntCalculator.divide(0, -1);
        Assert.assertEquals(0, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testDivide_4() {
        int actual = SimpleIntCalculator.divide(1, -1);
        Assert.assertEquals(-1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testDivide_5() {
        int actual = SimpleIntCalculator.divide(-1, -1);
        Assert.assertEquals(1, actual);
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
     * Coverage: 58,82%
     */
    @Test
    public void testIsPrime_2() {
        boolean actual = SimpleIntCalculator.isPrime(0);
        Assert.assertEquals(true, actual);
    }

    /**
     * Coverage: 58,82%
     */
    @Test
    public void testIsPrime_3() {
        boolean actual = SimpleIntCalculator.isPrime(-100);
        Assert.assertEquals(true, actual);
    }

    /**
     * Coverage: 58,82%
     */
    @Test
    public void testIsPrime_4() {
        boolean actual = SimpleIntCalculator.isPrime(-1000);
        Assert.assertEquals(true, actual);
    }

    /**
     * Coverage: 58,82%
     */
    @Test
    public void testIsPrime_5() {
        boolean actual = SimpleIntCalculator.isPrime(-10000);
        Assert.assertEquals(true, actual);
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
        int actual = SimpleIntCalculator.multiply(1, 1);
        Assert.assertEquals(1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testMultiply_4() {
        int actual = SimpleIntCalculator.multiply(-1, 0);
        Assert.assertEquals(0, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testMultiply_5() {
        int actual = SimpleIntCalculator.multiply(-1, 1);
        Assert.assertEquals(-1, actual);
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
        int actual = SimpleIntCalculator.subtract(1, 1);
        Assert.assertEquals(0, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testSubtract_4() {
        int actual = SimpleIntCalculator.subtract(-1, 0);
        Assert.assertEquals(-1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testSubtract_5() {
        int actual = SimpleIntCalculator.subtract(-1, 1);
        Assert.assertEquals(-2, actual);
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
        int actual = SimpleIntCalculator.sum(1, 1);
        Assert.assertEquals(2, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testSum_4() {
        int actual = SimpleIntCalculator.sum(-1, 0);
        Assert.assertEquals(-1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testSum_5() {
        int actual = SimpleIntCalculator.sum(-1, 1);
        Assert.assertEquals(0, actual);
    }
}
