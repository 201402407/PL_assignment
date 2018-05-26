package Node;
//201402407 ���ؿ�

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
