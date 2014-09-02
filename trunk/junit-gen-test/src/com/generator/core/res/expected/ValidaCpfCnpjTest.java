package com.generator.core.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.core.res.input.ValidaCpfCnpj;

public class ValidaCpfCnpjTest {

    /**
     * Coverage: 95,92%
     */
    @Test
    public void testIsValidCNPJ_1() {
        boolean actual = ValidaCpfCnpj.isValidCNPJ("02358361428711");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 95,92%
     */
    @Test
    public void testIsValidCNPJ_2() {
        boolean actual = ValidaCpfCnpj.isValidCNPJ("02877910887864");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 16,33%
     */
    @Test
    public void testIsValidCNPJ_3() {
        boolean actual = ValidaCpfCnpj.isValidCNPJ("");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 95,92%
     */
    @Test
    public void testIsValidCPF_1() {
        boolean actual = ValidaCpfCnpj.isValidCPF("00400436388");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 95,92%
     */
    @Test
    public void testIsValidCPF_2() {
        boolean actual = ValidaCpfCnpj.isValidCPF("57249755307");
        Assert.assertEquals(true, actual);
    }

    /**
     * Coverage: 16,33%
     */
    @Test
    public void testIsValidCPF_3() {
        boolean actual = ValidaCpfCnpj.isValidCPF("");
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
        boolean actual = ValidaCpfCnpj.isValidCPForCNPJ("85298800632442", 1);
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 38,46%
     */
    @Test
    public void testIsValidCPForCNPJ_3() {
        boolean actual = ValidaCpfCnpj.isValidCPForCNPJ("13172148398", 0);
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 38,46%
     */
    @Test
    public void testIsValidCPForCNPJ_4() {
        boolean actual = ValidaCpfCnpj.isValidCPForCNPJ("", 0);
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 38,46%
     */
    @Test
    public void testIsValidCPForCNPJ_5() {
        boolean actual = ValidaCpfCnpj.isValidCPForCNPJ("", 1);
        Assert.assertEquals(false, actual);
    }
}
