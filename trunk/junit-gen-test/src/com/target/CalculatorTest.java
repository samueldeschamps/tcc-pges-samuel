package com.target;

import org.junit.*;

import com.generator.core.res.input.SimpleIntCalculator;

import static org.junit.Assert.*;

/**
 * The class <code>CalculatorTest</code> contains tests for the class <code>{@link SimpleIntCalculator}</code>.
 *
 * @generatedBy CodePro at 17/07/14 20:51
 * @author Samuel
 * @version $Revision: 1.0 $
 */
public class CalculatorTest {
	/**
	 * Run the int divide(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testDivide_1()
		throws Exception {
		int a = 0;
		int b = 0;

		int result = SimpleIntCalculator.divide(a, b);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    java.lang.ArithmeticException: / by zero
		//       at com.target.Calculator.divide(Calculator.java:21)
		assertEquals(0, result);
	}
	
	/**
	 * Run the int divide(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testDivide_2()
		throws Exception {
		int a = 1;
		int b = 1;

		int result = SimpleIntCalculator.divide(a, b);

		// add additional test code here
		assertEquals(1, result);
	}

	/**
	 * Run the int divide(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testDivide_3()
		throws Exception {
		int a = 7;
		int b = 1;

		int result = SimpleIntCalculator.divide(a, b);

		// add additional test code here
		assertEquals(7, result);
	}

	/**
	 * Run the int divide(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testDivide_4()
		throws Exception {
		int a = 1;
		int b = 0;

		int result = SimpleIntCalculator.divide(a, b);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    java.lang.ArithmeticException: / by zero
		//       at com.target.Calculator.divide(Calculator.java:21)
		assertEquals(0, result);
	}

	/**
	 * Run the int divide(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testDivide_5()
		throws Exception {
		int a = 7;
		int b = 0;

		int result = SimpleIntCalculator.divide(a, b);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    java.lang.ArithmeticException: / by zero
		//       at com.target.Calculator.divide(Calculator.java:21)
		assertEquals(0, result);
	}

	/**
	 * Run the int divide(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testDivide_6()
		throws Exception {
		int a = 0;
		int b = 1;

		int result = SimpleIntCalculator.divide(a, b);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the boolean isPrime(int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testIsPrime_1()
		throws Exception {
		int number = 0;

		boolean result = SimpleIntCalculator.isPrime(number);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isPrime(int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testIsPrime_2()
		throws Exception {
		int number = 1;

		boolean result = SimpleIntCalculator.isPrime(number);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isPrime(int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testIsPrime_3()
		throws Exception {
		int number = 7;

		boolean result = SimpleIntCalculator.isPrime(number);

		// add additional test code here
		assertEquals(true, result);
	}

	@Test
	public void testIsPrime_4()
		throws Exception {
		int number = 10;
		boolean result = SimpleIntCalculator.isPrime(number);
		assertEquals(false, result);
	}

	/**
	 * Run the int multiply(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testMultiply_1()
		throws Exception {
		int a = 0;
		int b = 0;

		int result = SimpleIntCalculator.multiply(a, b);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int multiply(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testMultiply_2()
		throws Exception {
		int a = 1;
		int b = 1;

		int result = SimpleIntCalculator.multiply(a, b);

		// add additional test code here
		assertEquals(1, result);
	}

	/**
	 * Run the int multiply(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testMultiply_3()
		throws Exception {
		int a = 7;
		int b = 7;

		int result = SimpleIntCalculator.multiply(a, b);

		// add additional test code here
		assertEquals(49, result);
	}

	/**
	 * Run the int multiply(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testMultiply_4()
		throws Exception {
		int a = 1;
		int b = 0;

		int result = SimpleIntCalculator.multiply(a, b);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int multiply(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testMultiply_5()
		throws Exception {
		int a = 7;
		int b = 1;

		int result = SimpleIntCalculator.multiply(a, b);

		// add additional test code here
		assertEquals(7, result);
	}

	/**
	 * Run the int multiply(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testMultiply_6()
		throws Exception {
		int a = 0;
		int b = 7;

		int result = SimpleIntCalculator.multiply(a, b);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int multiply(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testMultiply_7()
		throws Exception {
		int a = 7;
		int b = 0;

		int result = SimpleIntCalculator.multiply(a, b);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int multiply(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testMultiply_8()
		throws Exception {
		int a = 0;
		int b = 1;

		int result = SimpleIntCalculator.multiply(a, b);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int multiply(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testMultiply_9()
		throws Exception {
		int a = 1;
		int b = 7;

		int result = SimpleIntCalculator.multiply(a, b);

		// add additional test code here
		assertEquals(7, result);
	}

	/**
	 * Run the int subtract(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testSubtract_1()
		throws Exception {
		int a = 0;
		int b = 0;

		int result = SimpleIntCalculator.subtract(a, b);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int subtract(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testSubtract_2()
		throws Exception {
		int a = 1;
		int b = 1;

		int result = SimpleIntCalculator.subtract(a, b);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int subtract(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testSubtract_3()
		throws Exception {
		int a = 7;
		int b = 7;

		int result = SimpleIntCalculator.subtract(a, b);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int subtract(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testSubtract_4()
		throws Exception {
		int a = 1;
		int b = 0;

		int result = SimpleIntCalculator.subtract(a, b);

		// add additional test code here
		assertEquals(1, result);
	}

	/**
	 * Run the int subtract(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testSubtract_5()
		throws Exception {
		int a = 7;
		int b = 1;

		int result = SimpleIntCalculator.subtract(a, b);

		// add additional test code here
		assertEquals(6, result);
	}

	/**
	 * Run the int subtract(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testSubtract_6()
		throws Exception {
		int a = 0;
		int b = 7;

		int result = SimpleIntCalculator.subtract(a, b);

		// add additional test code here
		assertEquals(-7, result);
	}

	/**
	 * Run the int subtract(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testSubtract_7()
		throws Exception {
		int a = 7;
		int b = 0;

		int result = SimpleIntCalculator.subtract(a, b);

		// add additional test code here
		assertEquals(7, result);
	}

	/**
	 * Run the int subtract(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testSubtract_8()
		throws Exception {
		int a = 0;
		int b = 1;

		int result = SimpleIntCalculator.subtract(a, b);

		// add additional test code here
		assertEquals(-1, result);
	}

	/**
	 * Run the int subtract(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testSubtract_9()
		throws Exception {
		int a = 1;
		int b = 7;

		int result = SimpleIntCalculator.subtract(a, b);

		// add additional test code here
		assertEquals(-6, result);
	}

	/**
	 * Run the int sum(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testSum_1()
		throws Exception {
		int a = 0;
		int b = 1;

		int result = SimpleIntCalculator.sum(a, b);

		// add additional test code here
		assertEquals(1, result);
	}

	/**
	 * Run the int sum(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testSum_2()
		throws Exception {
		int a = 0;
		int b = 7;

		int result = SimpleIntCalculator.sum(a, b);

		// add additional test code here
		assertEquals(7, result);
	}

	/**
	 * Run the int sum(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test
	public void testSum_3()
		throws Exception {
		int a = 0;
		int b = 0;

		int result = SimpleIntCalculator.sum(a, b);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int sum(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testSum_4()
		throws Exception {
		int a = -1;
		int b = 0;

		int result = SimpleIntCalculator.sum(a, b);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int sum(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testSum_5()
		throws Exception {
		int a = -1;
		int b = 7;

		int result = SimpleIntCalculator.sum(a, b);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int sum(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testSum_6()
		throws Exception {
		int a = -1;
		int b = 1;

		int result = SimpleIntCalculator.sum(a, b);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int sum(int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testSum_7()
		throws Exception {
		int a = -1;
		int b = 10;

		int result = SimpleIntCalculator.sum(a, b);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@Before
	public void setUp()
		throws Exception {
		// add additional set up code here
	}

	/**
	 * Perform post-test clean-up.
	 *
	 * @throws Exception
	 *         if the clean-up fails for some reason
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	@After
	public void tearDown()
		throws Exception {
		// Add additional tear down code here
	}

	/**
	 * Launch the test.
	 *
	 * @param args the command line arguments
	 *
	 * @generatedBy CodePro at 17/07/14 20:51
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(CalculatorTest.class);
	}
}