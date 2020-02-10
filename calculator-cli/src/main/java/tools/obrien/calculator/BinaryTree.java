package tools.obrien.calculator;

import java.util.List;

public interface BinaryTree {

	void insert(Node node);
	
	List<Node> postOrderTraversal();
}
