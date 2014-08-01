package com.generator.structure.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.structure.res.input.Calculator;

public class CalculatorTest {

    @Test
    public void testdivide_1() {
        try {
            Calculator.divide(0, 0);
            Assert.fail("An ArithmeticException must have been thrown.");
        } catch (ArithmeticException ex) {
            Assert.assertEquals("/ by zero", ex.getMessage());
        }
    }

    @Test
    public void testdivide_2() {
        try {
            Calculator.divide(1, 0);
            Assert.fail("An ArithmeticException must have been thrown.");
        } catch (ArithmeticException ex) {
            Assert.assertEquals("/ by zero", ex.getMessage());
        }
    }

    @Test
    public void testdivide_3() {
        int actual = Calculator.divide(0, 1);
        Assert.assertEquals(0, actual);
    }

    @Test
    public void testdivide_4() {
        int actual = Calculator.divide(1, 1);
        Assert.assertEquals(1, actual);
    }

    @Test
    public void testdivide_5() {
        try {
            Calculator.divide(-1, 0);
            Assert.fail("An ArithmeticException must have been thrown.");
        } catch (ArithmeticException ex) {
            Assert.assertEquals("/ by zero", ex.getMessage());
        }
    }

    @Test
    public void testisPrime_1() {
        boolean actual = Calculator.isPrime(0);
        Assert.assertEquals(true, actual);
    }

    @Test
    public void testisPrime_2() {
        boolean actual = Calculator.isPrime(1);
        Assert.assertEquals(true, actual);
    }

    @Test
    public void testisPrime_3() {
        boolean actual = Calculator.isPrime(-1);
        Assert.assertEquals(true, actual);
    }

    @Test
    public void testisPrime_4() {
        boolean actual = Calculator.isPrime(2);
        Assert.assertEquals(true, actual);
    }

    @Test
    public void testisPrime_5() {
        boolean actual = Calculator.isPrime(-2);
        Assert.assertEquals(true, actual);
    }

    @Test
    public void testmultiply_1() {
        int actual = Calculator.multiply(0, 0);
        Assert.assertEquals(0, actual);
    }

    @Test
    public void testmultiply_2() {
        int actual = Calculator.multiply(1, 0);
        Assert.assertEquals(0, actual);
    }

    @Test
    public void testmultiply_3() {
        int actual = Calculator.multiply(0, 1);
        Assert.assertEquals(0, actual);
    }

    @Test
    public void testmultiply_4() {
        int actual = Calculator.multiply(1, 1);
        Assert.assertEquals(1, actual);
    }

    @Test
    public void testmultiply_5() {
        int actual = Calculator.multiply(-1, 0);
        Assert.assertEquals(0, actual);
    }

    @Test
    public void testsubtract_1() {
        int actual = Calculator.subtract(0, 0);
        Assert.assertEquals(0, actual);
    }

    @Test
    public void testsubtract_2() {
        int actual = Calculator.subtract(1, 0);
        Assert.assertEquals(1, actual);
    }

    @Test
    public void testsubtract_3() {
        int actual = Calculator.subtract(0, 1);
        Assert.assertEquals(-1, actual);
    }

    @Test
    public void testsubtract_4() {
        int actual = Calculator.subtract(1, 1);
        Assert.assertEquals(0, actual);
    }

    @Test
    public void testsubtract_5() {
        int actual = Calculator.subtract(-1, 0);
        Assert.assertEquals(-1, actual);
    }

    @Test
    public void testsum_1() {
        int actual = Calculator.sum(0, 0);
        Assert.assertEquals(0, actual);
    }

    @Test
    public void testsum_2() {
        int actual = Calculator.sum(1, 0);
        Assert.assertEquals(1, actual);
    }

    @Test
    public void testsum_3() {
        int actual = Calculator.sum(0, 1);
        Assert.assertEquals(1, actual);
    }

    @Test
    public void testsum_4() {
        int actual = Calculator.sum(1, 1);
        Assert.assertEquals(2, actual);
    }

    @Test
    public void testsum_5() {
        try {
            Calculator.sum(-1, 0);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("A cannot be negative!", ex.getMessage());
        }
    }
}
