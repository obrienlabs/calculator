package tools.obrien.calculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Part of a review of Binary trees - not since Smmalltalk V in 1992
 * @author michaelobrien
 *
 */
public class BinaryTreeImpl implements BinaryTree {

	private Node root;
	
	@Override
	public void insert(Node node, Boolean isLeft) {
		if(null == root) {
			root = node;
		} else {
			if(isLeft) {
				root.setLeft(node);
			} else {
				root.setRight(node);
			}
		}
	}

	@Override
	public List<Node> postOrderTraversal() {
		List<Node> nodes = new ArrayList<>();
		root.postOrderTraversal(nodes);
		return nodes;
	}
	
	public Node getRoot() {
		return root;
	}

}
