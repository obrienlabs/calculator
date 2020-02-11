package tools.obrien.calculator;

import java.util.List;

/**
 * Part of a review of Binary trees - not since Smmalltalk V in 1992
 * @author michaelobrien
 *
 */
public class NodeImpl implements Node  {
	
	private Double value;
	private Node left;
	private Node right;
	private String operator = null;
	private Node parent;

	
	public NodeImpl(Double value) {
		this.value = value;
	}
	
	public NodeImpl(String operator) {
		this.operator = operator;
	}
	
	@Override
	public Double getValue() {
		return value;
	}

	@Override
	public Node left() {
		return left;
	}

	@Override
	public Node right() {
		return right;
	}

	@Override
	public void setLeft(Node left) {
		this.left = left;
		left.setParent(this);
	}

	@Override
	public void setRight(Node right) {
		this.right = right;
		right.setParent(this);
	}
	
	@Override
	public Boolean isOperator() {
		return null != operator;
	}
	
	public String getOperator() {
		return operator;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	@Override
	public void postOrderTraversal(List<Node> nodes) {
		if(null != left) {
			left.postOrderTraversal(nodes);
		}
		if(null != right) {
			right.postOrderTraversal(nodes);
		}
		nodes.add(this);
	}

}
