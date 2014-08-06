package com.generator.structure.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.structure.res.input.Exceptions;
import java.io.IOException;

public class ExceptionsTest {

    /**
     * Coverage: 63,64%
     */
    @Test
    public void testSquareRoot_1() {
        Exceptions.squareRoot(-1);
    }

    /**
     * Coverage: 54,55%
     */
    @Test
    public void testSquareRoot_2() {
        double actual = Exceptions.squareRoot(0);
        Assert.assertEquals(0.0, actual, 1.0E-8);
    }

    /**
     * Coverage: 54,55%
     */
    @Test
    public void testSquareRoot_3() {
        double actual = Exceptions.squareRoot(1);
        Assert.assertEquals(1.0, actual, 1.0E-8);
    }

    /**
     * Coverage: 54,55%
     */
    @Test
    public void testSquareRoot_4() {
        double actual = Exceptions.squareRoot(2);
        Assert.assertEquals(1.4142135623730951, actual, 1.0E-8);
    }

    /**
     * Coverage: 54,55%
     */
    @Test
    public void testSquareRoot_5() {
        double actual = Exceptions.squareRoot(3);
        Assert.assertEquals(1.7320508075688772, actual, 1.0E-8);
    }

    /**
     * Coverage: 63,64%
     */
    @Test
    public void testSquareRootDeclared_1() {
        try {
            Exceptions.squareRootDeclared(-1);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("A cannot be negative!", ex.getMessage());
        }
    }

    /**
     * Coverage: 54,55%
     */
    @Test
    public void testSquareRootDeclared_2() {
        double actual = Exceptions.squareRootDeclared(0);
        Assert.assertEquals(0.0, actual, 1.0E-8);
    }

    /**
     * Coverage: 54,55%
     */
    @Test
    public void testSquareRootDeclared_3() {
        double actual = Exceptions.squareRootDeclared(1);
        Assert.assertEquals(1.0, actual, 1.0E-8);
    }

    /**
     * Coverage: 54,55%
     */
    @Test
    public void testSquareRootDeclared_4() {
        double actual = Exceptions.squareRootDeclared(2);
        Assert.assertEquals(1.4142135623730951, actual, 1.0E-8);
    }

    /**
     * Coverage: 54,55%
     */
    @Test
    public void testSquareRootDeclared_5() {
        double actual = Exceptions.squareRootDeclared(3);
        Assert.assertEquals(1.7320508075688772, actual, 1.0E-8);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testThrowDeclaredCheckedException_1() {
        try {
            Exceptions.throwDeclaredCheckedException(0, 0);
            Assert.fail("An IOException must have been thrown.");
        } catch (IOException ex) {
        }
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testThrowDeclaredCheckedException_2() {
        try {
            Exceptions.throwDeclaredCheckedException(0, 1);
            Assert.fail("An IOException must have been thrown.");
        } catch (IOException ex) {
        }
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testThrowDeclaredCheckedException_3() {
        try {
            Exceptions.throwDeclaredCheckedException(1, 1);
            Assert.fail("An IOException must have been thrown.");
        } catch (IOException ex) {
        }
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testThrowDeclaredCheckedException_4() {
        try {
            Exceptions.throwDeclaredCheckedException(-1, 0);
            Assert.fail("An IOException must have been thrown.");
        } catch (IOException ex) {
        }
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testThrowDeclaredCheckedException_5() {
        try {
            Exceptions.throwDeclaredCheckedException(-1, 1);
            Assert.fail("An IOException must have been thrown.");
        } catch (IOException ex) {
        }
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testThrowDeclaredUncheckedException_1() {
        try {
            Exceptions.throwDeclaredUncheckedException(0, 0);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testThrowDeclaredUncheckedException_2() {
        try {
            Exceptions.throwDeclaredUncheckedException(0, 1);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testThrowDeclaredUncheckedException_3() {
        try {
            Exceptions.throwDeclaredUncheckedException(1, 1);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testThrowDeclaredUncheckedException_4() {
        try {
            Exceptions.throwDeclaredUncheckedException(-1, 0);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testThrowDeclaredUncheckedException_5() {
        try {
            Exceptions.throwDeclaredUncheckedException(-1, 1);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testThrowException_1() {
        Exceptions.throwException(0, 0);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testThrowException_2() {
        Exceptions.throwException(0, 1);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testThrowException_3() {
        Exceptions.throwException(1, 1);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testThrowException_4() {
        Exceptions.throwException(-1, 0);
    }

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testThrowException_5() {
        Exceptions.throwException(-1, 1);
    }
}
