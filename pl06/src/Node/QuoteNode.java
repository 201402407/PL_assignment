package Node;
// 201402407 ���ؿ�

public class QuoteNode implements Node {
	Node quoted;
	
	public QuoteNode(Node quoted) { // ' ���� ��带 ���ڷ� ����
		this.quoted = quoted;
	}
	
	@Override
	public String toString() {
		return quoted.toString();
	}
	
	public Node nodeInside() {
		// TODO Auto-generated method stub
		return quoted;
	}
}
