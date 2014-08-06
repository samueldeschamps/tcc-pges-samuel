package com.generator.structure.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.structure.res.input.ValidaCPF;

public class ValidaCPFTest {

    /**
     * Coverage: 96,08%
     */
    @Test
    public void testFormatarCPF_1() {
        String actual = ValidaCPF.formatarCPF("355772253850");
        Assert.assertEquals("355.772.253-85", actual);
    }

    /**
     * Coverage: 96,08%
     */
    @Test
    public void testFormatarCPF_2() {
        String actual = ValidaCPF.formatarCPF("73254451038472032");
        Assert.assertEquals("732.544.510-38", actual);
    }

    /**
     * Coverage: 96,08%
     */
    @Test
    public void testFormatarCPF_3() {
        String actual = ValidaCPF.formatarCPF("37785887188882716");
        Assert.assertEquals("377.858.871-88", actual);
    }

    /**
     * Coverage: 96,08%
     */
    @Test
    public void testFormatarCPF_4() {
        String actual = ValidaCPF.formatarCPF("25073795293113084");
        Assert.assertEquals("250.737.952-93", actual);
    }

    /**
     * Coverage: 7,84%
     */
    @Test
    public void testFormatarCPF_5() {
        String actual = ValidaCPF.formatarCPF(null);
        Assert.assertEquals(null, actual);
    }

    /**
     * Coverage: 89,29%
     */
    @Test
    public void testIsCpfValido_1() {
        boolean actual = ValidaCPF.isCpfValido("97960481169");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 84,82%
     */
    @Test
    public void testIsCpfValido_2() {
        boolean actual = ValidaCPF.isCpfValido("59316373208");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 84,82%
     */
    @Test
    public void testIsCpfValido_3() {
        boolean actual = ValidaCPF.isCpfValido("57249755307");
        Assert.assertEquals(true, actual);
    }

    /**
     * Coverage: 83,04%
     */
    @Test
    public void testIsCpfValido_4() {
        boolean actual = ValidaCPF.isCpfValido("15468165609");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 7,14%
     */
    @Test
    public void testIsCpfValido_5() {
        boolean actual = ValidaCPF.isCpfValido("");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 3,57%
     */
    @Test
    public void testIsCpfValido_6() {
        boolean actual = ValidaCPF.isCpfValido(null);
        Assert.assertEquals(false, actual);
    }
}
