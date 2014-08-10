package com.generator.structure.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.structure.res.input.ValidaCpfCnpj;

public class ValidaCpfCnpjTest {

    /**
     * Coverage: 95,92%
     */
    @Test
    public void testIsValidCNPJ_1() {
        boolean actual = ValidaCpfCnpj.isValidCNPJ("31093565820733");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 16,33%
     */
    @Test
    public void testIsValidCNPJ_2() {
        boolean actual = ValidaCpfCnpj.isValidCNPJ("");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 95,92%
     */
    @Test
    public void testIsValidCNPJ_3() {
        boolean actual = ValidaCpfCnpj.isValidCNPJ("85298800632442");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 95,92%
     */
    @Test
    public void testIsValidCNPJ_4() {
        boolean actual = ValidaCpfCnpj.isValidCNPJ("92085210689162");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 95,92%
     */
    @Test
    public void testIsValidCNPJ_5() {
        boolean actual = ValidaCpfCnpj.isValidCNPJ("98726896646036");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 95,92%
     */
    @Test
    public void testIsValidCPF_1() {
        boolean actual = ValidaCpfCnpj.isValidCPF("13172148398");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 16,33%
     */
    @Test
    public void testIsValidCPF_2() {
        boolean actual = ValidaCpfCnpj.isValidCPF("");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 95,92%
     */
    @Test
    public void testIsValidCPF_3() {
        boolean actual = ValidaCpfCnpj.isValidCPF("14056442208");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 95,92%
     */
    @Test
    public void testIsValidCPF_4() {
        boolean actual = ValidaCpfCnpj.isValidCPF("50104703776");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 95,92%
     */
    @Test
    public void testIsValidCPF_5() {
        boolean actual = ValidaCpfCnpj.isValidCPF("59436834751");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 53,85%
     */
    @Test
    public void testIsValidCPForCNPJ_1() {
        try {
            ValidaCpfCnpj.isValidCPForCNPJ(null, -1);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Tipo precisa ser 0 ou 1!", ex.getMessage());
        }
    }

    /**
     * Coverage: 38,46%
     */
    @Test
    public void testIsValidCPForCNPJ_2() {
        boolean actual = ValidaCpfCnpj.isValidCPForCNPJ(null, 0);
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 38,46%
     */
    @Test
    public void testIsValidCPForCNPJ_3() {
        boolean actual = ValidaCpfCnpj.isValidCPForCNPJ(null, 1);
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 53,85%
     */
    @Test
    public void testIsValidCPForCNPJ_4() {
        try {
            ValidaCpfCnpj.isValidCPForCNPJ(null, 2);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Tipo precisa ser 0 ou 1!", ex.getMessage());
        }
    }

    /**
     * Coverage: 53,85%
     */
    @Test
    public void testIsValidCPForCNPJ_5() {
        try {
            ValidaCpfCnpj.isValidCPForCNPJ(null, -2);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Tipo precisa ser 0 ou 1!", ex.getMessage());
        }
    }
}
