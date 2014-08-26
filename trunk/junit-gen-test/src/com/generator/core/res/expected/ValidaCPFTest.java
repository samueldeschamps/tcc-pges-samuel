package com.generator.core.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.core.res.input.ValidaCPF;

public class ValidaCPFTest {

    /**
     * Coverage: 96,08%
     */
    @Test
    public void testFormatarCPF_1() {
        String actual = ValidaCPF.formatarCPF("13172148398");
        Assert.assertEquals("131.721.483-98", actual);
    }

    /**
     * Coverage: 7,84%
     */
    @Test
    public void testFormatarCPF_2() {
        String actual = ValidaCPF.formatarCPF(null);
        Assert.assertEquals(null, actual);
    }

    /**
     * Coverage: 96,08%
     */
    @Test
    public void testFormatarCPF_3() {
        String actual = ValidaCPF.formatarCPF("14056442208");
        Assert.assertEquals("140.564.422-08", actual);
    }

    /**
     * Coverage: 89,29%
     */
    @Test
    public void testIsCpfValido_1() {
        boolean actual = ValidaCPF.isCpfValido("00933070711");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 84,82%
     */
    @Test
    public void testIsCpfValido_2() {
        boolean actual = ValidaCPF.isCpfValido("57249755307");
        Assert.assertEquals(true, actual);
    }

    /**
     * Coverage: 83,04%
     */
    @Test
    public void testIsCpfValido_3() {
        boolean actual = ValidaCPF.isCpfValido("14909973232");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 7,14%
     */
    @Test
    public void testIsCpfValido_4() {
        boolean actual = ValidaCPF.isCpfValido("");
        Assert.assertEquals(false, actual);
    }

    /**
     * Coverage: 3,57%
     */
    @Test
    public void testIsCpfValido_5() {
        boolean actual = ValidaCPF.isCpfValido(null);
        Assert.assertEquals(false, actual);
    }
}
