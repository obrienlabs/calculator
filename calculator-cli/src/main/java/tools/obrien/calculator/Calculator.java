package tools.obrien.calculator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
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
	
	private final static Logger LOGGER = Logger.getLogger(Calculator.class.getName());

	/** parallelStreams need thread safe HashMaps */
	private Map<Long, String> concurrentHashMap = new ConcurrentHashMap<>();
    /** parallelStreams need thread safe ArrayLists */
	private List<String> splitCopyOnWriteArrayList = new CopyOnWriteArrayList<>();
	
	public static final String ADD = "add";
	public static final String SUB = "sub";
	public static final String DIV = "div";
	public static final String MULT = "mult";
	public static final String LET = "let";
	
	private Double leftAcc = 0D;
	private Double rightAcc = 0D;
	// flip between left and right operands as we RPN parse
	private boolean positionLeft = true;
	
	public Double parse(String expression) {
		LOGGER.log(Level.INFO, "expression: " + expression);
		String filteredExpression = removeWhitespace(expression);
		
		// split based on comma - then use RPN to evaluate in reverse
		List<String> splitList = splitByDelimiter(filteredExpression, ",");
		// tokenize elements
		splitList.stream().forEach(splitConsumer);
		// reverse list for RPN
		Collections.reverse(splitCopyOnWriteArrayList);
		// evaluate operations
		splitCopyOnWriteArrayList.stream().forEach(calcConsumer);
		Double result = leftAcc;
		LOGGER.log(Level.INFO, "result: " + result);
		return result;
	}
	
	Consumer<String> calcConsumer = new Consumer<String>() {
		public void accept(String token) {
			switch(token) {
			case ADD:
				// pop last 2 tokens and compute
				leftAcc = rightAcc + leftAcc;
				break;
			default:
				if(positionLeft) {
					positionLeft = false;
					leftAcc = Double.parseDouble(token);
				} else {
					positionLeft = true;
					rightAcc = Double.parseDouble(token);
				}
			}
		}
	};	
	
	Consumer<String> splitConsumer = new Consumer<String>() {
		public void accept(String line) {
			tokenize(line); 
		}
	};
	
	private void tokenize(String line) {
		if(null != line) {
			// remove closing brackets
			String truncLine = line.replaceAll("\\)", "");
			StringTokenizer tokenizer = new StringTokenizer(truncLine, "(");
			while(tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				LOGGER.log(Level.FINEST, "token: " + token);
				splitCopyOnWriteArrayList.add(token);
			}
		}
	}
	
	private String removeWhitespace(String expression) {
		return expression.replaceAll(" ", "");
	}
	
	private List<String> splitByDelimiter(String expression, String delimiter) {
		return Stream.of(expression.split(delimiter))
				.map(elem -> new String(elem))
				.collect(Collectors.toList());
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
		Double result = calculator.parse(expression);
		LOGGER.log(Level.INFO, "Result: " + result);
	}
}
