package Node;
// 201402407 ���ؿ� 

public class IdNode implements ValueNode {
	
	String idString;
	
	public IdNode(String text) {
		idString = text;
	}
	
	@Override
	public String toString() {
		return "ID: " + idString;
	}
}
