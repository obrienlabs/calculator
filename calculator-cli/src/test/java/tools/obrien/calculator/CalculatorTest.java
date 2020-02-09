package tools.obrien.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

}
