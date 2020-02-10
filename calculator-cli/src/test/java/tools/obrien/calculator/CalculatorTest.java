package tools.obrien.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author michaelobrien
 *
 */
public class CalculatorTest {

	private final Calculator calculator = new Calculator();
	
	@Test
	public void testResursionLevel0() {
		String input = "add(1, 2 ) ";
		Double expected = 3D;
		Double result = calculator.parse(input);
		assertNotNull(result);
		assertEquals(expected, result);
	}

	@Test
	public void testResursionLevel1() {
		String input = "add(4, add(1, 2 )) ";
		Double expected = 7D;
		Double result = calculator.parse(input);
		assertNotNull(result);
		assertEquals(expected, result);
	}

	@Test
	public void testResursionLevel3() {
		String input = "add(16, add(8, add(4, add(1, 2 )))) ";
		Double expected = 31D;
		Double result = calculator.parse(input);
		assertNotNull(result);
		assertEquals(expected, result);
	}
	
	/*@Test
	public void testResursionLevel3Left() {
		String input = "add(add(add(add(1, 2 ),4),8), 16) ";
		Double expected = 31D;
		Double result = calculator.parse(input);
		assertNotNull(result);
		assertEquals(expected, result);
	}*/
}
