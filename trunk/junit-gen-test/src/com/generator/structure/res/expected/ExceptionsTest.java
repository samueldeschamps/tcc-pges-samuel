package com.generator.structure.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.structure.res.input.Exceptions;
import java.io.IOException;

public class ExceptionsTest {

    @Test
    public void testalwaysThrowsException_1() {
        try {
            Exceptions.alwaysThrowsException(0, 0);
            Assert.fail("A NullPointerException must have been thrown.");
        } catch (NullPointerException ex) {
        }
    }

    @Test
    public void testalwaysThrowsException_2() {
        try {
            Exceptions.alwaysThrowsException(1, 0);
            Assert.fail("A NullPointerException must have been thrown.");
        } catch (NullPointerException ex) {
        }
    }

    @Test
    public void testalwaysThrowsException_3() {
        try {
            Exceptions.alwaysThrowsException(0, 1);
            Assert.fail("A NullPointerException must have been thrown.");
        } catch (NullPointerException ex) {
        }
    }

    @Test
    public void testalwaysThrowsException_4() {
        try {
            Exceptions.alwaysThrowsException(1, 1);
            Assert.fail("A NullPointerException must have been thrown.");
        } catch (NullPointerException ex) {
        }
    }

    @Test
    public void testalwaysThrowsException_5() {
        try {
            Exceptions.alwaysThrowsException(-1, 0);
            Assert.fail("A NullPointerException must have been thrown.");
        } catch (NullPointerException ex) {
        }
    }

    @Test
    public void testsquareRoot_1() {
        double actual = Exceptions.squareRoot(0);
        Assert.assertEquals(0.0, actual, 0.00000001);
    }

    @Test
    public void testsquareRoot_2() {
        double actual = Exceptions.squareRoot(1);
        Assert.assertEquals(1.0, actual, 0.00000001);
    }

    @Test
    public void testsquareRoot_3() {
        try {
            Exceptions.squareRoot(-1);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("A cannot be negative!", ex.getMessage());
        }
    }

    @Test
    public void testsquareRoot_4() {
        double actual = Exceptions.squareRoot(2);
        Assert.assertEquals(1.4142135623730951, actual, 0.00000001);
    }

    @Test
    public void testsquareRoot_5() {
        try {
            Exceptions.squareRoot(-2);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("A cannot be negative!", ex.getMessage());
        }
    }

    @Test
    public void testthowsCheckedException_1() {
        try {
            Exceptions.thowsCheckedException(0, 0);
            Assert.fail("An IOException must have been thrown.");
        } catch (IOException ex) {
        }
    }

    @Test
    public void testthowsCheckedException_2() {
        try {
            Exceptions.thowsCheckedException(1, 0);
            Assert.fail("An IOException must have been thrown.");
        } catch (IOException ex) {
        }
    }

    @Test
    public void testthowsCheckedException_3() {
        try {
            Exceptions.thowsCheckedException(0, 1);
            Assert.fail("An IOException must have been thrown.");
        } catch (IOException ex) {
        }
    }

    @Test
    public void testthowsCheckedException_4() {
        try {
            Exceptions.thowsCheckedException(1, 1);
            Assert.fail("An IOException must have been thrown.");
        } catch (IOException ex) {
        }
    }

    @Test
    public void testthowsCheckedException_5() {
        try {
            Exceptions.thowsCheckedException(-1, 0);
            Assert.fail("An IOException must have been thrown.");
        } catch (IOException ex) {
        }
    }

    @Test
    public void testthrowsWarnedUncheckedException_1() {
        try {
            Exceptions.throwsWarnedUncheckedException(0, 0);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testthrowsWarnedUncheckedException_2() {
        try {
            Exceptions.throwsWarnedUncheckedException(1, 0);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testthrowsWarnedUncheckedException_3() {
        try {
            Exceptions.throwsWarnedUncheckedException(0, 1);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testthrowsWarnedUncheckedException_4() {
        try {
            Exceptions.throwsWarnedUncheckedException(1, 1);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testthrowsWarnedUncheckedException_5() {
        try {
            Exceptions.throwsWarnedUncheckedException(-1, 0);
            Assert.fail("An IllegalArgumentException must have been thrown.");
        } catch (IllegalArgumentException ex) {
        }
    }
}
