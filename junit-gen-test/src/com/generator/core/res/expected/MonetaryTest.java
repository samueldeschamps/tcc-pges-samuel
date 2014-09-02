package com.generator.core.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.core.res.input.Monetary;
import java.math.BigDecimal;
import com.generator.core.res.input.Monetary.RemainderStrategy;

public class MonetaryTest {

    /**
     * Coverage: 58,27%
     */
    @Test
    public void testInstallments_1() {
        BigDecimal[] actual = Monetary.installments(new BigDecimal("1"), 3, RemainderStrategy.LAST_N);
        BigDecimal[] expected = new BigDecimal[] { new BigDecimal("0.33"), new BigDecimal("0.33"), new BigDecimal("0.34") };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 54,33%
     */
    @Test
    public void testInstallments_2() {
        BigDecimal[] actual = Monetary.installments(new BigDecimal("1"), 3, RemainderStrategy.FIRST_N);
        BigDecimal[] expected = new BigDecimal[] { new BigDecimal("0.34"), new BigDecimal("0.33"), new BigDecimal("0.33") };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 47,24%
     */
    @Test
    public void testInstallments_3() {
        BigDecimal[] actual = Monetary.installments(new BigDecimal("0"), 1, RemainderStrategy.LAST_1);
        BigDecimal[] expected = new BigDecimal[] { new BigDecimal("0.00") };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 40,16%
     */
    @Test
    public void testInstallments_4() {
        BigDecimal[] actual = Monetary.installments(new BigDecimal("0"), 1, RemainderStrategy.FIRST_1);
        BigDecimal[] expected = new BigDecimal[] { new BigDecimal("0.00") };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 6,30%
     */
    @Test
    public void testInstallments_5() {
        BigDecimal[] actual = Monetary.installments(new BigDecimal("0"), 0, null);
        BigDecimal[] expected = new BigDecimal[] {  };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 6,30%
     */
    @Test
    public void testInstallments_6() {
        try {
            Monetary.installments(new BigDecimal("0"), 1000, RemainderStrategy.FIRST_1);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Installments cannot be greater than 360!", ex.getMessage());
        }
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testInstallments2_1() {
        BigDecimal[] actual = Monetary.installments(new BigDecimal("1"), 3);
        BigDecimal[] expected = new BigDecimal[] { new BigDecimal("0.34"), new BigDecimal("0.33"), new BigDecimal("0.33") };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testInstallments2_2() {
        BigDecimal[] actual = Monetary.installments(null, 0);
        BigDecimal[] expected = new BigDecimal[] {  };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 0,00%
     */
    @Test
    public void testInstallments2_3() {
        try {
            Monetary.installments(null, 1000);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Installments cannot be greater than 360!", ex.getMessage());
        }
    }
}
