package Node;
//201402407 ÀÌÇØ¿ø

public class IntNode implements ValueNode {
	
	private Integer value;
	
	@Override
	public String toString() {
		return "INT: " + value;
	}
	
	public IntNode(String text) {
		this.value = new Integer(text);
	}
	
	public Integer getIntValue() {
		return value;
	}
}
