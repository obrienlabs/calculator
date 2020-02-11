package tools.obrien.calculator;

import java.util.List;

public interface Node {
	
	Node getParent();
	void setParent(Node parent);

	Double getValue();
	
	Node left();
	
	void setLeft(Node left);
	
	Node right();
	
	void setRight(Node right);
	
	Boolean isOperator();
	String getOperator();
	
	//Double operate();
	void postOrderTraversal(List<Node> nodes);
	
}
