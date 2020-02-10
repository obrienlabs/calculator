package tools.obrien.calculator;

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
	}

	@Override
	public void setRight(Node right) {
		this.right = right;
	}
	
	@Override
	public Boolean isOperator() {
		return null != operator;
	}

	/*@Override
	public Double operate() {
		if(isOperator()) {
			switch(operator) {
			case Calculator.ADD:
				// pop last 2 tokens and compute
				return left + right;
				break;
			default:
				return null;
			}
		}
		return null;
	}*/

}
