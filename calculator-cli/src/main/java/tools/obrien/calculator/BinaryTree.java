package tools.obrien.calculator;

import java.util.List;

public interface BinaryTree {

	void insert(Node node, Boolean isLeft);
	
	List<Node> postOrderTraversal();
	
	Node getRoot();
}
