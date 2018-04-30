package Node;
// 201402407 ÀÌÇØ¿ø

public class BooleanNode implements ValueNode {
	
	Boolean value;
	
	@Override
	public String toString() {
		return value ? "#T" : "#F";
	}
	
	public static BooleanNode FALSE_NODE = new BooleanNode(false);
	public static BooleanNode TRUE_NODE = new BooleanNode(true);
	
	private BooleanNode(Boolean b) {
		value = b;
	}
}
