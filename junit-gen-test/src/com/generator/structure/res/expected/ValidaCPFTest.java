package com.generator.structure.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.structure.res.input.ValidaCPF;

public class ValidaCPFTest {

    /**Coverage: 7,84%*/
    @Test
    public void testformatarCPF_1() {
        String actual = ValidaCPF.formatarCPF(null);
        Assert.assertEquals(null, actual);
    }

    /**Coverage: 0,00%*/
    @Test
    public void testformatarCPF_2() {
        try {
            ValidaCPF.formatarCPF("");
            Assert.fail("A StringIndexOutOfBoundsException must have been thrown.");
        } catch (StringIndexOutOfBoundsException ex) {
            Assert.assertEquals("String index out of range: 3", ex.getMessage());
        }
    }

    /**Coverage: 0,00%*/
    @Test
    public void testformatarCPF_3() {
        try {
            ValidaCPF.formatarCPF("a");
            Assert.fail("A StringIndexOutOfBoundsException must have been thrown.");
        } catch (StringIndexOutOfBoundsException ex) {
            Assert.assertEquals("String index out of range: 3", ex.getMessage());
        }
    }

    /**Coverage: 0,00%*/
    @Test
    public void testformatarCPF_4() {
        try {
            ValidaCPF.formatarCPF("A");
            Assert.fail("A StringIndexOutOfBoundsException must have been thrown.");
        } catch (StringIndexOutOfBoundsException ex) {
            Assert.assertEquals("String index out of range: 3", ex.getMessage());
        }
    }

    /**Coverage: 0,00%*/
    @Test
    public void testformatarCPF_5() {
        try {
            ValidaCPF.formatarCPF("abc");
            Assert.fail("A StringIndexOutOfBoundsException must have been thrown.");
        } catch (StringIndexOutOfBoundsException ex) {
            Assert.assertEquals("String index out of range: 6", ex.getMessage());
        }
    }

    /**Coverage: 3,57%*/
    @Test
    public void testisCpfValido_1() {
        boolean actual = ValidaCPF.isCpfValido(null);
        Assert.assertEquals(false, actual);
    }

    /**Coverage: 7,14%*/
    @Test
    public void testisCpfValido_2() {
        boolean actual = ValidaCPF.isCpfValido("");
        Assert.assertEquals(false, actual);
    }

    /**Coverage: 7,14%*/
    @Test
    public void testisCpfValido_3() {
        boolean actual = ValidaCPF.isCpfValido("a");
        Assert.assertEquals(false, actual);
    }

    /**Coverage: 7,14%*/
    @Test
    public void testisCpfValido_4() {
        boolean actual = ValidaCPF.isCpfValido("A");
        Assert.assertEquals(false, actual);
    }

    /**Coverage: 7,14%*/
    @Test
    public void testisCpfValido_5() {
        boolean actual = ValidaCPF.isCpfValido("abc");
        Assert.assertEquals(false, actual);
    }
}
