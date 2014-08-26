package com.generator.core.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.core.res.input.ArrayOperations;

public class ArrayOperationsTest {

    /**
     * Coverage: 72,22%
     */
    @Test
    public void testConcat_1() {
        String actual = ArrayOperations.concat("a", new String[] { "a", "a" });
        Assert.assertEquals("aaa", actual);
    }

    /**
     * Coverage: 40,00%
     */
    @Test
    public void testConcat_2() {
        try {
            ArrayOperations.concat("0", new String[] { "a", "" });
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("There cannot be empty strings in array!", ex.getMessage());
        }
    }

    /**
     * Coverage: 16,67%
     */
    @Test
    public void testConcat_3() {
        try {
            ArrayOperations.concat("0", new String[] {  });
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Values cannot be empty!", ex.getMessage());
        }
    }

    /**
     * Coverage: 13,33%
     */
    @Test
    public void testConcat_4() {
        try {
            ArrayOperations.concat("", new String[] {  });
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Separator cannot be empty!", ex.getMessage());
        }
    }

    /**
     * Coverage: 10,00%
     */
    @Test
    public void testConcat_5() {
        try {
            ArrayOperations.concat("", null);
            Assert.fail("A NullPointerException must have been thrown.");
        } catch (NullPointerException ex) {
            Assert.assertEquals("Values cannot be null!", ex.getMessage());
        }
    }

    /**
     * Coverage: 7,78%
     */
    @Test
    public void testConcat_6() {
        try {
            ArrayOperations.concat(null, null);
            Assert.fail("A NullPointerException must have been thrown.");
        } catch (NullPointerException ex) {
            Assert.assertEquals("Separator cannot be null!", ex.getMessage());
        }
    }

    /**
     * Coverage: 82,14%
     */
    @Test
    public void testMax_1() {
        int actual = ArrayOperations.max(new int[] { 0 });
        Assert.assertEquals(0, actual);
    }

    /**
     * Coverage: 25,00%
     */
    @Test
    public void testMax_2() {
        try {
            ArrayOperations.max(null);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Values cannot be null!", ex.getMessage());
        }
    }

    /**
     * Coverage: 82,14%
     */
    @Test
    public void testMax_3() {
        int actual = ArrayOperations.max(new int[] { 1 });
        Assert.assertEquals(1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testSum_1() {
        double actual = ArrayOperations.sum(new double[] { 0.0 });
        Assert.assertEquals(0.0, actual, 1.0E-8);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testSum_2() {
        double actual = ArrayOperations.sum(new double[] { 1.0 });
        Assert.assertEquals(1.0, actual, 1.0E-8);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testSum_3() {
        double actual = ArrayOperations.sum(new double[] { -1.0 });
        Assert.assertEquals(-1.0, actual, 1.0E-8);
    }
}
