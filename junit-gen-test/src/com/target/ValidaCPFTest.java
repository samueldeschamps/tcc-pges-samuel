package com.target;

import org.junit.*;

import com.generator.structure.res.input.ValidaCPF;

import static org.junit.Assert.*;

/**
 * The class <code>ValidaCPFTest</code> contains tests for the class <code>{@link ValidaCPF}</code>.
 *
 * @generatedBy CodePro at 17/07/14 22:50
 * @author Samuel
 * @version $Revision: 1.0 $
 */
public class ValidaCPFTest {
	/**
	 * Run the String formatarCPF(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 22:50
	 */
	@Test(expected = java.lang.StringIndexOutOfBoundsException.class)
	public void testFormatarCPF_1()
		throws Exception {
		String cpf = "";

		String result = ValidaCPF.formatarCPF(cpf);

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the boolean isCpfValido(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 22:50
	 */
	@Test
	public void testIsCpfValido_1()
		throws Exception {
		String cpf = "";

		boolean result = ValidaCPF.isCpfValido(cpf);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isCpfValido(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 22:50
	 */
	@Test
	public void testIsCpfValido_2()
		throws Exception {
		String cpf = "";

		boolean result = ValidaCPF.isCpfValido(cpf);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isCpfValido(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 22:50
	 */
	@Test
	public void testIsCpfValido_3()
		throws Exception {
		String cpf = "aaaaaaaaaaa";

		boolean result = ValidaCPF.isCpfValido(cpf);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isCpfValido(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 17/07/14 22:50
	 */
	@Test
	public void testIsCpfValido_4()
		throws Exception {
		String cpf = "";

		boolean result = ValidaCPF.isCpfValido(cpf);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 17/07/14 22:50
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
	 * @generatedBy CodePro at 17/07/14 22:50
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
	 * @generatedBy CodePro at 17/07/14 22:50
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(ValidaCPFTest.class);
	}
}