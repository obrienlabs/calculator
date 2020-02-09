package tools.obrien.calculator;

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

/**
 * This class implements a lexical CLI calculator
 * Code:  https://github.com/obrienlabs/calculator
 * Build: http://jenkins.obrienlabs.cloud/job/calculator-mvn-java/
 * 
 * Performance: 
 *  - not optimized for memory space - strings are recreated - no replacement in place
 *  - partially optimized for speed - through the use of parallel streams
 *  
 * @author michaelobrien
 *
 */
public class Calculator implements ICalculator {

	/** parallelStreams need thread safe HashMaps */
	private Map<Long, String> concurrentHashMap = new ConcurrentHashMap<>();
    /** parallelStreams need thread safe ArrayLists */
	private List<String> splitCopyOnWriteArrayList = new CopyOnWriteArrayList<>();
	
	public static final String ADD = "add";
	public static final String SUB = "sub";
	public static final String DIV = "div";
	public static final String MULT = "mult";
	public static final String LET = "let";
	
	
	public String parse(String expression) {
		String result = null;
		result = removeWhitespace(expression);
		
		return result;
	}
	
	private String removeWhitespace(String expression) {
		return expression.replaceAll(" ", "");
	}
	
	public Map<Long, String> getConcurrentHashMap() {
		return concurrentHashMap;
	}

	public void setConcurrentHashMap(Map<Long, String> concurrentHashMap) {
		this.concurrentHashMap = concurrentHashMap;
	}
	
	public static void main(String[] args) {
		String expression = null;
		// get expression from cli
		if(null != args && args.length > 0) {
			expression = args[0];
		}
		Calculator calculator = new Calculator();
		String result = calculator.parse(expression);
		System.out.print(result);
	}
}
