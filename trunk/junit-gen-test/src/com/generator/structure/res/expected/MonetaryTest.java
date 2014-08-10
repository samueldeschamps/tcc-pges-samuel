package com.generator.structure.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.structure.res.input.Monetary;
import java.math.BigDecimal;
import com.generator.structure.res.input.Monetary.RemainderStrategy;

public class MonetaryTest {

    /**
     * Coverage: 55,83%
     */
    @Test
    public void testInstallments_1() {
        BigDecimal[] actual = Monetary.installments(new BigDecimal("1"), 3, RemainderStrategy.LAST_N);
        BigDecimal[] expected = new BigDecimal[] { new BigDecimal("0.33"), new BigDecimal("0.33"), new BigDecimal("0.34") };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 54,17%
     */
    @Test
    public void testInstallments_2() {
        BigDecimal[] actual = Monetary.installments(new BigDecimal("1"), 3, RemainderStrategy.FIRST_N);
        BigDecimal[] expected = new BigDecimal[] { new BigDecimal("0.34"), new BigDecimal("0.33"), new BigDecimal("0.33") };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 49,17%
     */
    @Test
    public void testInstallments_3() {
        BigDecimal[] actual = Monetary.installments(new BigDecimal("0"), 1, RemainderStrategy.LAST_1);
        BigDecimal[] expected = new BigDecimal[] { new BigDecimal("0.00") };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 44,17%
     */
    @Test
    public void testInstallments_4() {
        BigDecimal[] actual = Monetary.installments(new BigDecimal("0"), 1, RemainderStrategy.FIRST_1);
        BigDecimal[] expected = new BigDecimal[] { new BigDecimal("0.00") };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 6,67%
     */
    @Test
    public void testInstallments_5() {
        BigDecimal[] actual = Monetary.installments(new BigDecimal("0"), 0, null);
        BigDecimal[] expected = new BigDecimal[] {  };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 6,67%
     */
    @Test
    public void testInstallments_6() {
        try {
            Monetary.installments(new BigDecimal("0"), 10000, RemainderStrategy.FIRST_1);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Installments cannot be greater than 1200!", ex.getMessage());
        }
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testInstallments__1() {
        BigDecimal[] actual = Monetary.installments_(new BigDecimal("0"), 0);
        BigDecimal[] expected = new BigDecimal[] {  };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testInstallments__2() {
        BigDecimal[] actual = Monetary.installments_(new BigDecimal("0"), 1);
        BigDecimal[] expected = new BigDecimal[] { new BigDecimal("0.00") };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testInstallments__3() {
        BigDecimal[] actual = Monetary.installments_(new BigDecimal("0"), 2);
        BigDecimal[] expected = new BigDecimal[] { new BigDecimal("0.00"), new BigDecimal("0.00") };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testInstallments__4() {
        BigDecimal[] actual = Monetary.installments_(new BigDecimal("0"), 3);
        BigDecimal[] expected = new BigDecimal[] { new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00") };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testInstallments__5() {
        BigDecimal[] actual = Monetary.installments_(new BigDecimal("0"), 10);
        BigDecimal[] expected = new BigDecimal[] { new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00") };
        Assert.assertArrayEquals(expected, actual);
    }
}
