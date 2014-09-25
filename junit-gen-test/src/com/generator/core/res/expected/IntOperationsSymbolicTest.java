package com.generator.core.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.core.res.input.IntOperationsSymbolic;

public class IntOperationsSymbolicTest {

    /**
     * Coverage: 47,37%
     */
    @Test
    public void testAvaliaFaixaEtaria_1() {
        String actual = IntOperationsSymbolic.avaliaFaixaEtaria(60);
        Assert.assertEquals("Idoso", actual);
    }

    /**
     * Coverage: 31,58%
     */
    @Test
    public void testAvaliaFaixaEtaria_2() {
        String actual = IntOperationsSymbolic.avaliaFaixaEtaria(0);
        Assert.assertEquals("Recém nascido.", actual);
    }

    /**
     * Coverage: 31,58%
     */
    @Test
    public void testAvaliaFaixaEtaria_3() {
        String actual = IntOperationsSymbolic.avaliaFaixaEtaria(1);
        Assert.assertEquals("Criança", actual);
    }

    /**
     * Coverage: 31,58%
     */
    @Test
    public void testAvaliaFaixaEtaria_4() {
        String actual = IntOperationsSymbolic.avaliaFaixaEtaria(11);
        Assert.assertEquals("Adolescente", actual);
    }

    /**
     * Coverage: 21,05%
     */
    @Test
    public void testAvaliaFaixaEtaria_5() {
        String actual = IntOperationsSymbolic.avaliaFaixaEtaria(-1000);
        Assert.assertEquals(null, actual);
    }

    /**
     * Coverage: 74,47%
     */
    @Test
    public void testAvaliaTriangulo_1() {
        String actual = IntOperationsSymbolic.avaliaTriangulo(2, 1, 2);
        Assert.assertEquals("Isósceles", actual);
    }

    /**
     * Coverage: 74,47%
     */
    @Test
    public void testAvaliaTriangulo_2() {
        String actual = IntOperationsSymbolic.avaliaTriangulo(2, 4, 3);
        Assert.assertEquals("Escaleno", actual);
    }

    /**
     * Coverage: 68,09%
     */
    @Test
    public void testAvaliaTriangulo_3() {
        String actual = IntOperationsSymbolic.avaliaTriangulo(1, 1, 1);
        Assert.assertEquals("Equiláteo", actual);
    }

    /**
     * Coverage: 36,17%
     */
    @Test
    public void testAvaliaTriangulo_4() {
        String actual = IntOperationsSymbolic.avaliaTriangulo(1, 1, 2);
        Assert.assertEquals("Não é triângulo", actual);
    }

    /**
     * Coverage: 77,27%
     */
    @Test
    public void testFactorial_1() throws Exception {
        int actual = IntOperationsSymbolic.factorial(2);
        Assert.assertEquals(2, actual);
    }

    /**
     * Coverage: 77,27%
     */
    @Test
    public void testFactorial_2() throws Exception {
        int actual = IntOperationsSymbolic.factorial(3);
        Assert.assertEquals(6, actual);
    }

    /**
     * Coverage: 31,82%
     */
    @Test
    public void testFactorial_3() {
        try {
            IntOperationsSymbolic.factorial(-1000);
            Assert.fail("An Exception must have been thrown.");
        } catch (Exception ex) {
            Assert.assertEquals("value is smaller than 0", ex.getMessage());
        }
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testGcd_1() {
        int actual = IntOperationsSymbolic.gcd(-500, -1000);
        Assert.assertEquals(-500, actual);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testGcd_2() {
        int actual = IntOperationsSymbolic.gcd(-1000, -1000);
        Assert.assertEquals(-1000, actual);
    }

    /**
     * Coverage: 38,46%
     */
    @Test
    public void testGcd_3() {
        int actual = IntOperationsSymbolic.gcd(-500, 0);
        Assert.assertEquals(-500, actual);
    }
}
