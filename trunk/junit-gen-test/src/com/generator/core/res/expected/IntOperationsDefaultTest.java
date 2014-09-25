package com.generator.core.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.core.res.input.IntOperationsDefault;

public class IntOperationsDefaultTest {

    /**
     * Coverage: 47,37%
     */
    @Test
    public void testAvaliaFaixaEtaria_1() {
        String actual = IntOperationsDefault.avaliaFaixaEtaria(100);
        Assert.assertEquals("Idoso", actual);
    }

    /**
     * Coverage: 31,58%
     */
    @Test
    public void testAvaliaFaixaEtaria_2() {
        String actual = IntOperationsDefault.avaliaFaixaEtaria(0);
        Assert.assertEquals("Recém nascido.", actual);
    }

    /**
     * Coverage: 31,58%
     */
    @Test
    public void testAvaliaFaixaEtaria_3() {
        String actual = IntOperationsDefault.avaliaFaixaEtaria(1);
        Assert.assertEquals("Criança", actual);
    }

    /**
     * Coverage: 21,05%
     */
    @Test
    public void testAvaliaFaixaEtaria_4() {
        String actual = IntOperationsDefault.avaliaFaixaEtaria(-1);
        Assert.assertEquals(null, actual);
    }

    /**
     * Coverage: 74,47%
     */
    @Test
    public void testAvaliaTriangulo_1() {
        String actual = IntOperationsDefault.avaliaTriangulo(2, 1, 2);
        Assert.assertEquals("Isósceles", actual);
    }

    /**
     * Coverage: 68,09%
     */
    @Test
    public void testAvaliaTriangulo_2() {
        String actual = IntOperationsDefault.avaliaTriangulo(1, 1, 1);
        Assert.assertEquals("Equiláteo", actual);
    }

    /**
     * Coverage: 44,68%
     */
    @Test
    public void testAvaliaTriangulo_3() {
        String actual = IntOperationsDefault.avaliaTriangulo(1, -1155484576, -1155484576);
        Assert.assertEquals("Não é triângulo", actual);
    }

    /**
     * Coverage: 94,12%
     */
    @Test
    public void testBlah_1() {
        float actual = IntOperationsDefault.blah(1, -1);
        Assert.assertEquals((float) 2.0, actual, 1.0E-8);
    }

    /**
     * Coverage: 94,12%
     */
    @Test
    public void testBlah_2() {
        float actual = IntOperationsDefault.blah(1, -2);
        Assert.assertEquals((float) 2.0, actual, 1.0E-8);
    }

    /**
     * Coverage: 73,53%
     */
    @Test
    public void testBlah_3() {
        float actual = IntOperationsDefault.blah(0, 1);
        Assert.assertEquals((float) 1.0, actual, 1.0E-8);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testGcd_1() {
        int actual = IntOperationsDefault.gcd(0, 1);
        Assert.assertEquals(1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testGcd_2() {
        int actual = IntOperationsDefault.gcd(0, -1);
        Assert.assertEquals(-1, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testGcd_3() {
        int actual = IntOperationsDefault.gcd(0, 2);
        Assert.assertEquals(2, actual);
    }
}
