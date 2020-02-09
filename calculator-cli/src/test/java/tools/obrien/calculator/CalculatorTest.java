package tools.obrien.calculator;

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
	public void test() {
		// iterate over a fixed range of numerics
		//https://www.deadcoderising.com/2015-05-19-java-8-replace-traditional-for-loops-with-intstreams/
		// print a range of numbers
		System.out.println("\nrange of numbers");
		IntStream.range(1, 9).forEach(i -> System.out.print("," + i)); // 1,2,3,4,5,6,7,8,9
		assertNotNull(calculator);

	}

}
