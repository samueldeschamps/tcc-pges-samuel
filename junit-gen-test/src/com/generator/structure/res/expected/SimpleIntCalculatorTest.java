package com.generator.structure.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.structure.res.input.SimpleIntCalculator;

public class SimpleIntCalculatorTest {

    /**Coverage: 100,00%*/
    @Test
    public void testdivide_1() {
        int actual = SimpleIntCalculator.divide(0, 1);
        Assert.assertEquals(0, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testdivide_2() {
        int actual = SimpleIntCalculator.divide(1, 1);
        Assert.assertEquals(1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testdivide_3() {
        int actual = SimpleIntCalculator.divide(-1, 1);
        Assert.assertEquals(-1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testdivide_4() {
        int actual = SimpleIntCalculator.divide(0, -1);
        Assert.assertEquals(0, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testdivide_5() {
        int actual = SimpleIntCalculator.divide(1, -1);
        Assert.assertEquals(-1, actual);
    }

    /**Coverage: 58,82%*/
    @Test
    public void testisPrime_1() {
        boolean actual = SimpleIntCalculator.isPrime(0);
        Assert.assertEquals(true, actual);
    }

    /**Coverage: 58,82%*/
    @Test
    public void testisPrime_2() {
        boolean actual = SimpleIntCalculator.isPrime(1);
        Assert.assertEquals(true, actual);
    }

    /**Coverage: 58,82%*/
    @Test
    public void testisPrime_3() {
        boolean actual = SimpleIntCalculator.isPrime(-1);
        Assert.assertEquals(true, actual);
    }

    /**Coverage: 58,82%*/
    @Test
    public void testisPrime_4() {
        boolean actual = SimpleIntCalculator.isPrime(2);
        Assert.assertEquals(true, actual);
    }

    /**Coverage: 58,82%*/
    @Test
    public void testisPrime_5() {
        boolean actual = SimpleIntCalculator.isPrime(-2);
        Assert.assertEquals(true, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testmultiply_1() {
        int actual = SimpleIntCalculator.multiply(0, 0);
        Assert.assertEquals(0, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testmultiply_2() {
        int actual = SimpleIntCalculator.multiply(1, 0);
        Assert.assertEquals(0, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testmultiply_3() {
        int actual = SimpleIntCalculator.multiply(0, 1);
        Assert.assertEquals(0, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testmultiply_4() {
        int actual = SimpleIntCalculator.multiply(1, 1);
        Assert.assertEquals(1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testmultiply_5() {
        int actual = SimpleIntCalculator.multiply(-1, 0);
        Assert.assertEquals(0, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testsubtract_1() {
        int actual = SimpleIntCalculator.subtract(0, 0);
        Assert.assertEquals(0, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testsubtract_2() {
        int actual = SimpleIntCalculator.subtract(1, 0);
        Assert.assertEquals(1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testsubtract_3() {
        int actual = SimpleIntCalculator.subtract(0, 1);
        Assert.assertEquals(-1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testsubtract_4() {
        int actual = SimpleIntCalculator.subtract(1, 1);
        Assert.assertEquals(0, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testsubtract_5() {
        int actual = SimpleIntCalculator.subtract(-1, 0);
        Assert.assertEquals(-1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testsum_1() {
        int actual = SimpleIntCalculator.sum(0, 0);
        Assert.assertEquals(0, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testsum_2() {
        int actual = SimpleIntCalculator.sum(1, 0);
        Assert.assertEquals(1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testsum_3() {
        int actual = SimpleIntCalculator.sum(0, 1);
        Assert.assertEquals(1, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testsum_4() {
        int actual = SimpleIntCalculator.sum(1, 1);
        Assert.assertEquals(2, actual);
    }

    /**Coverage: 100,00%*/
    @Test
    public void testsum_5() {
        int actual = SimpleIntCalculator.sum(-1, 0);
        Assert.assertEquals(-1, actual);
    }
}
