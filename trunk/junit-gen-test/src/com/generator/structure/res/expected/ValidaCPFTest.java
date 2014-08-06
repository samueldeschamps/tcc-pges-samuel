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

    /**Coverage: 96,08%*/
    @Test
    public void testformatarCPF_2() {
        String actual = ValidaCPF.formatarCPF("346416106820");
        Assert.assertEquals("346.416.106-82", actual);
    }

    /**Coverage: 96,08%*/
    @Test
    public void testformatarCPF_3() {
        String actual = ValidaCPF.formatarCPF("316511486628249947430");
        Assert.assertEquals("316.511.486-62", actual);
    }

    /**Coverage: 96,08%*/
    @Test
    public void testformatarCPF_4() {
        String actual = ValidaCPF.formatarCPF("6614016322838626");
        Assert.assertEquals("661.401.632-28", actual);
    }

    /**Coverage: 96,08%*/
    @Test
    public void testformatarCPF_5() {
        String actual = ValidaCPF.formatarCPF("429830699185663767655980179");
        Assert.assertEquals("429.830.699-18", actual);
    }

    /**Coverage: 7,14%*/
    @Test
    public void testisCpfValido_1() {
        boolean actual = ValidaCPF.isCpfValido("");
        Assert.assertEquals(false, actual);
    }

    /**Coverage: 7,14%*/
    @Test
    public void testisCpfValido_2() {
        boolean actual = ValidaCPF.isCpfValido("a");
        Assert.assertEquals(false, actual);
    }

    /**Coverage: 7,14%*/
    @Test
    public void testisCpfValido_3() {
        boolean actual = ValidaCPF.isCpfValido("A");
        Assert.assertEquals(false, actual);
    }

    /**Coverage: 7,14%*/
    @Test
    public void testisCpfValido_4() {
        boolean actual = ValidaCPF.isCpfValido("abc");
        Assert.assertEquals(false, actual);
    }

    /**Coverage: 7,14%*/
    @Test
    public void testisCpfValido_5() {
        boolean actual = ValidaCPF.isCpfValido("ABC");
        Assert.assertEquals(false, actual);
    }
}
