package tools.obrien.calculator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
	
	// no generics yet
	private BinaryTree tree = new BinaryTreeImpl();
	//private Node rootNode;
	
	public static final String ADD = "add";
	public static final String SUB = "sub";
	public static final String DIV = "div";
	public static final String MULT = "mult";
	public static final String LET = "let";
	public static final String LB = "(";
	public static final String RB = ")";
	public static final String CO = ",";
	
	private Double leftAcc = 0D;
	private Double rightAcc = 0D;
	// flip between left and right operands as we RPN parse
	private boolean positionLeft = true;
	
	/**
	 * The expression is split based on non-alphanumeric characters
	 * IE: add(1,add(2,3)) splits as add ( 1 , add ( 2 , 3 ) )
	 * where ( = left subtree
	 * , sibling right branch
	 * ) = traversal to parent
	 * 
	 * @param expression
	 * @return
	 */
	public Double parse(String expression) {
		LOGGER.log(Level.INFO, "expression: " + expression);
		String filteredExpression = removeWhitespace(expression);
		
		// split based on comma and brackets - then use RPN to evaluate in reverse
		List<String> splitList = splitByDelimiter(filteredExpression);//, "(),");
		// tokenize elements
		//splitList.stream().forEach(splitConsumer);
		// reverse list for RPN
		//Collections.reverse(splitCopyOnWriteArrayList);
		// insert binary tree nodes
		//splitList.stream().forEach(splitConsumer);
		tokenize(splitList);
		
		// evaluate operations
		//splitCopyOnWriteArrayList.stream().forEach(calcConsumer);
		Double result = evaluateTree();
		//Double result = leftAcc;
		LOGGER.log(Level.INFO, "result: " + result);
		return result;
	}
	
	private Double evaluateTree() {
		//Double result = 0D;
		List<Node> nodes = tree.postOrderTraversal();
		//nodes.stream().forEach(i -> System.out.print("," + i.getValue()));
		Double left = 0D;
		Double right = 0D;
		boolean isLeft = true;
		Double acc = 0D;
		// iterate until we get to an op, then replace last 3 with eval
		List<Double> values = new ArrayList<>();
		for(Node node : nodes) {
			if(node.isOperator()) {
				left = values.get(values.size() - 2);
				right = values.get(values.size() - 1);
				switch(node.getOperator()) {
				case ADD:
					acc = left + right;
					//left = acc;
					break;
				case SUB:
					acc = left - right;
					//left = acc;
					break;
				case MULT:
					acc = left * right;
					//left = acc;
					break;
				case DIV:
					acc = left / right;
					//left = acc;
					break;
				default:
				}
				// truncate/replace list
				values = values.subList(0, values.size() - 2);
				values.add(acc);
			} else {
				/*if(isLeft) {
					left = node.getValue();
				} else {
					right = node.getValue();
					isLeft = true;
				}*/
				values.add(node.getValue());
			}
		}
		
		return values.get(0);
	}
	
	Consumer<String> calcConsumer = new Consumer<String>() {
		public void accept(String token) {
			switch(token) {
			case ADD:
				// pop last 2 tokens and compute
				leftAcc = rightAcc + leftAcc;
				// after a left side calc - the next token is on the right
				positionLeft = false;
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
	
	/*Consumer<String> splitConsumer = new Consumer<String>() {
		public void accept(String line) {
			tokenize(line); 
		}
	};*/
	
	
	private Node insertNode(Node node, Node currentNode, boolean isDirective, boolean navLeft, boolean navRight) {
		//Node nextNode = currentNode;
		if(null == currentNode) {
			tree.insert(node, true);
			//nextNode = node;
		} else {
			if(navLeft) {
				currentNode.setLeft(node);
				//nextNode = node;
			}
			if(navRight) {
				currentNode.getParent().setRight(node);
				//nextNode = node;
			}
		}
		return node;
	}
	
	//private void tokenize(String pos) {
	private void tokenize(List<String> list) {
		Node currentNode = tree.getRoot();
		
		boolean navLeft = false;
		boolean navRight = false;
		
		for(String pos : list) {
		switch(pos) {
		case ADD:
		case SUB:
		case MULT:
		case DIV:
			currentNode = insertNode(new NodeImpl(pos), currentNode, false, navLeft, navRight);
			break;
		case LB:
			navLeft = true;
			navRight = false;			
			break;
		case RB:
			currentNode = currentNode.getParent();
			navLeft = false;
			navRight = false;
			break;
		case CO:
			navRight = true;
			navLeft = false;
			break;
		default:
			currentNode = insertNode(new NodeImpl(Double.valueOf(pos)), currentNode, false, navLeft, navRight);
			break;
		}
		}
	}
	
	
	/*private void tokenize(String line) {
		if(null != line) {
			// remove closing brackets
			String truncLine = line.replaceAll("\\)", "");
			StringTokenizer tokenizer = new StringTokenizer(truncLine, "(");
			while(tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				LOGGER.log(Level.FINE, "token: " + token);
				splitCopyOnWriteArrayList.add(token);
			}
		}
	}*/
	
	private String removeWhitespace(String expression) {
		return expression.replaceAll(" ", "");
	}
	
	private List<String> splitByDelimiter(String expression) {//, String delimiter) {
		/*return Stream.of(expression.split(delimiter))
				.map(elem -> new String(elem))
				.collect(Collectors.toList());*/
		List<String> list = new ArrayList<>();
		//Collections.addAll(list, expression.split("\(,)"));
		

		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<expression.length(); i++) {
			char ch = expression.charAt(i);
			switch (ch) {
				case '(': // insert subtree right
				case ')': // back up to parent
				case ',': // insert adjacent right
					if(buffer.length() > 0) {
						list.add(buffer.toString());
						buffer = new StringBuffer();
					}
					// in all cases even after a ) - write a ,
					list.add(String.valueOf(ch));
					break;
				default:
					buffer.append(ch);
			}
		}
		return list;	
		
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
