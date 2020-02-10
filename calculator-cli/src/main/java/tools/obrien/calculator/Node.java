package tools.obrien.calculator;

public interface Node {

	Double getValue();
	
	Node left();
	
	void setLeft(Node left);
	
	Node right();
	
	void setRight(Node right);
	
	Boolean isOperator();
	
	//Double operate();
	
}
